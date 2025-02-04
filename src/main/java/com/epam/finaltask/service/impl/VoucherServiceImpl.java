package com.epam.finaltask.service.impl;

import com.epam.finaltask.aspect.Loggable;
import com.epam.finaltask.dto.VoucherDTO;
import com.epam.finaltask.dto.VoucherSearchParamsDto;
import com.epam.finaltask.exception.EntityAlreadyExistsException;
import com.epam.finaltask.exception.EntityNotFoundException;
import com.epam.finaltask.exception.InvalidVoucherOperationException;
import com.epam.finaltask.mapper.UserMapper;
import com.epam.finaltask.mapper.VoucherMapper;
import com.epam.finaltask.model.User;
import com.epam.finaltask.model.Voucher;
import com.epam.finaltask.model.enums.HotelType;
import com.epam.finaltask.model.enums.TourType;
import com.epam.finaltask.model.enums.TransferType;
import com.epam.finaltask.model.enums.VoucherStatus;
import com.epam.finaltask.repository.VoucherRepository;
import com.epam.finaltask.service.AuthenticationService;
import com.epam.finaltask.service.EmailSenderService;
import com.epam.finaltask.service.UserService;
import com.epam.finaltask.service.VoucherService;
import com.epam.finaltask.specification.VoucherSpecification;
import com.epam.finaltask.util.I18nUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.epam.finaltask.model.enums.StatusCodes.*;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {
    private final VoucherMapper voucherMapper;
    private final VoucherRepository voucherRepository;
    private final UserService userService;
    private final UserMapper userMapper;
    private final VoucherSpecification voucherSpecification;
    private final EmailSenderService emailSenderService;
    private final AuthenticationService authenticationService;
    private final I18nUtil i18nUtil;

    @Loggable
    @Override
    public VoucherDTO create(VoucherDTO voucherDTO) {
        voucherDTO.setTourType(voucherDTO.getTourType().toUpperCase().strip());
        voucherDTO.setTransferType(voucherDTO.getTransferType().toUpperCase().strip());
        voucherDTO.setHotelType(voucherDTO.getHotelType().toUpperCase().strip());
        voucherDTO.setStatus(VoucherStatus.AVAILABLE.name());

        return voucherMapper.toVoucherDTO(voucherRepository.save(voucherMapper.toVoucher(voucherDTO)));
    }

    @Loggable
    @Override
    public VoucherDTO update(String voucherId, VoucherDTO voucherDTO) {
        Voucher voucher = getVoucherById(voucherId);

        VoucherStatus newStatus = VoucherStatus.valueOf(voucherDTO.getStatus().toUpperCase().strip());
        if (newStatus.equals(VoucherStatus.PAID) || newStatus.equals(VoucherStatus.REGISTERED)) {
            throw new InvalidVoucherOperationException(INVALID_VOUCHER_OPERATION.name(),
                    i18nUtil.getMessage("error.invalid-update", newStatus.name()));
        }

        voucher.setTitle(voucherDTO.getTitle());
        voucher.setDescription(voucherDTO.getDescription());
        voucher.setPrice(voucherDTO.getPrice());
        voucher.setTourType(TourType.valueOf(voucherDTO.getTourType().toUpperCase().strip()));
        voucher.setTransferType(TransferType.valueOf(voucherDTO.getTransferType().toUpperCase().strip()));
        voucher.setHotelType(HotelType.valueOf(voucherDTO.getHotelType().toUpperCase().strip()));
        voucher.setArrivalDate(voucherDTO.getArrivalDate());
        voucher.setEvictionDate(voucherDTO.getEvictionDate());
        voucher.setHot(Boolean.parseBoolean(voucherDTO.getIsHot()));

        if ((newStatus.equals(VoucherStatus.AVAILABLE) || newStatus.equals(VoucherStatus.NOT_SOLD))
                && voucher.getUser() != null) {
            voucher.setUser(null);
        }
        voucher.setStatus(newStatus);
        return voucherMapper.toVoucherDTO(voucherRepository.save(voucher));
    }

    @Loggable
    @Override
    public void delete(String voucherId) {
        Voucher voucher = getVoucherById(voucherId);

        if (voucher.getStatus().equals(VoucherStatus.PAID)
                || voucher.getStatus().equals(VoucherStatus.REGISTERED)) {
            throw new InvalidVoucherOperationException(INVALID_VOUCHER_OPERATION.name(),
                    i18nUtil.getMessage("error.invalid-delete", voucher.getStatus().name()));
        }
        voucherRepository.deleteById(UUID.fromString(voucherId));
    }

    @Loggable
    @Override
    public VoucherDTO changeHotStatus(String voucherId, VoucherDTO voucherDTO) {
        Voucher voucher = getVoucherById(voucherId);

        if (!voucher.getStatus().equals(VoucherStatus.AVAILABLE)) {
            throw new InvalidVoucherOperationException(INVALID_VOUCHER_OPERATION.name(),
                    i18nUtil.getMessage("error.invalid-hot-change"));
        }
        voucher.setHot(Boolean.parseBoolean(voucherDTO.getIsHot()));
        return voucherMapper.toVoucherDTO(voucherRepository.save(voucher));
    }

    @Loggable
    @Override
    public VoucherDTO changeStatus(String id, VoucherDTO voucherDTO) {
        Voucher voucher = getVoucherById(id);
        VoucherStatus newStatus = VoucherStatus.valueOf(voucherDTO.getStatus().toUpperCase().strip());

        if (!voucher.getStatus().equals(VoucherStatus.REGISTERED)
                || !(newStatus.equals(VoucherStatus.PAID) || newStatus.equals(VoucherStatus.CANCELED))) {
            throw new InvalidVoucherOperationException(INVALID_VOUCHER_OPERATION.name(),
                    i18nUtil.getMessage("error.invalid-status-change",
                            newStatus.name(), voucher.getStatus().name()));
        }
        voucher.setStatus(newStatus);

        if (newStatus.equals(VoucherStatus.PAID)) {
            emailSenderService.sendPaymentConfirmationEmail(voucher);
        }
        if (newStatus.equals(VoucherStatus.CANCELED)) {
            emailSenderService.sendOrderCanceledEmail(voucher, voucher.getUser());
        }
        return voucherMapper.toVoucherDTO(voucherRepository.save(voucher));
    }

    @Override
    public VoucherDTO findById(String voucherId) {
        return voucherMapper.toVoucherDTO(getVoucherById(voucherId));
    }

    @Loggable
    @Override
    public VoucherDTO order(String voucherId, String userId) {
        Voucher voucher = getVoucherById(voucherId);
        User user = userMapper.toUser(userService.getUserById(UUID.fromString(userId)));

        if (!voucher.getStatus().equals(VoucherStatus.AVAILABLE)) {
            throw new InvalidVoucherOperationException(INVALID_VOUCHER_OPERATION.name(),
                    i18nUtil.getMessage("error.invalid-order"));
        }

        if (voucher.getUser() != null) {
            throw new EntityAlreadyExistsException(DUPLICATE_ORDER.name(),
                    i18nUtil.getMessage("error.voucher-already-ordered"));
        }

        voucher.setUser(user);
        voucher.setStatus(VoucherStatus.REGISTERED);

        emailSenderService.sendOrderConfirmationEmail(voucher);
        return voucherMapper.toVoucherDTO(voucherRepository.save(voucher));
    }

    @Override
    public Page<VoucherDTO> findAll(Pageable pageable) {
        return voucherRepository.findAll(pageable)
                .map(voucherMapper::toVoucherDTO);
    }

    @Override
    public Page<VoucherDTO> findAllByUserId(String userId, Pageable pageable) {
        return voucherRepository.findAllByUserId(UUID.fromString(userId), pageable)
                .map(voucherMapper::toVoucherDTO);
    }

    @Override
    public Page<VoucherDTO> search(VoucherSearchParamsDto params, Pageable pageable) {
        return voucherRepository.findAll(voucherSpecification.build(params), pageable)
                .map(voucherMapper::toVoucherDTO);
    }

    @Loggable
    @Override
    public VoucherDTO cancelOrder(String voucherId) {
        Voucher voucher = getVoucherById(voucherId);

        if (voucher.getUser() == null || !authenticationService.isCurrentUser(voucher.getUser().getId())) {
            throw new AccessDeniedException(FORBIDDEN.name());
        }

        if (!voucher.getStatus().equals(VoucherStatus.REGISTERED)) {
            throw new InvalidVoucherOperationException(INVALID_VOUCHER_OPERATION.name(),
                    i18nUtil.getMessage("error.invalid-cancel-order", voucher.getStatus().name()));
        }

        User user = voucher.getUser();
        voucher.setStatus(VoucherStatus.AVAILABLE);
        voucher.setUser(null);

        emailSenderService.sendOrderCanceledEmail(voucher, user);
        return voucherMapper.toVoucherDTO(voucherRepository.save(voucher));
    }

    @Loggable
    @Override
    public VoucherDTO payVoucher(String voucherId) {
        Voucher voucher = getVoucherById(voucherId);
        User user = voucher.getUser();

        if (user == null || !authenticationService.isCurrentUser(user.getId())) {
            throw new AccessDeniedException(FORBIDDEN.name());
        }

        if (!voucher.getStatus().equals(VoucherStatus.REGISTERED)) {
            throw new InvalidVoucherOperationException(INVALID_VOUCHER_OPERATION.name(),
                    i18nUtil.getMessage("error.invalid-pay-voucher", voucher.getStatus().name()));
        }

        if (user.getBalance().compareTo(voucher.getPrice()) < 0) {
            throw new InvalidVoucherOperationException(INVALID_VOUCHER_OPERATION.name(),
                    i18nUtil.getMessage("error.not-enough-balance"));
        }

        user.setBalance(user.getBalance().subtract(voucher.getPrice()));
        voucher.setStatus(VoucherStatus.PAID);

        emailSenderService.sendPaymentConfirmationEmail(voucher);
        return voucherMapper.toVoucherDTO(voucherRepository.save(voucher));
    }

    private Voucher getVoucherById(String id) {
        return voucherRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND.name(),
                        i18nUtil.getMessage("error.voucher-not-exists")));
    }
}
