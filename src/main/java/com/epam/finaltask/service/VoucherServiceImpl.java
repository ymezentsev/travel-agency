package com.epam.finaltask.service;

import com.epam.finaltask.dto.VoucherDTO;
import com.epam.finaltask.dto.VoucherSearchParametersDto;
import com.epam.finaltask.exception.EntityAlreadyExistsException;
import com.epam.finaltask.exception.EntityNotFoundException;
import com.epam.finaltask.exception.InvalidVoucherOperationException;
import com.epam.finaltask.mapper.UserMapper;
import com.epam.finaltask.mapper.VoucherMapper;
import com.epam.finaltask.model.*;
import com.epam.finaltask.repository.VoucherRepository;
import com.epam.finaltask.repository.searchcriteria.SpecificationBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.epam.finaltask.exception.StatusCodes.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {
    private final VoucherMapper voucherMapper;
    private final VoucherRepository voucherRepository;
    private final UserService userService;
    private final UserMapper userMapper;
    private final SpecificationBuilder<Voucher> specificationBuilder;

    private static final String VOUCHER_NOT_EXISTS = "Voucher not exists";
    private static final String VOUCHER_ALREADY_ORDERED = "Voucher is already ordered";
    private static final String INVALID_HOT_CHANGE = "'HOT' change is allowed only for vouchers with the 'AVAILABLE' status";
    private static final String INVALID_STATUS_CHANGE = "Status '%s' cannot be set for a voucher with status '%s'";
    private static final String INVALID_DELETE = "Voucher with status '%s' cannot be deleted";
    private static final String INVALID_UPDATE = "Status '%s' cannot be set by an update operation";


    @Override
    public VoucherDTO create(VoucherDTO voucherDTO) {
        voucherDTO.setTourType(voucherDTO.getTourType().toUpperCase().strip());
        voucherDTO.setTransferType(voucherDTO.getTransferType().toUpperCase().strip());
        voucherDTO.setHotelType(voucherDTO.getHotelType().toUpperCase().strip());
        voucherDTO.setStatus(VoucherStatus.AVAILABLE.name());

        return voucherMapper.toVoucherDTO(
                voucherRepository.save(
                        voucherMapper.toVoucher(voucherDTO)));
    }

    //todo for tests remove check if voucher has status paid
    @Override
    public VoucherDTO update(String id, VoucherDTO voucherDTO) {
        Voucher voucher = getVoucherById(id);

        voucherDTO.setTourType(voucherDTO.getTourType().toUpperCase().strip());
        voucherDTO.setTransferType(voucherDTO.getTransferType().toUpperCase().strip());
        voucherDTO.setHotelType(voucherDTO.getHotelType().toUpperCase().strip());
        voucherDTO.setStatus(voucherDTO.getStatus().toUpperCase().strip());

        Voucher newVoucher = voucherMapper.toVoucher(voucherDTO);
        voucher.setTitle(newVoucher.getTitle());
        voucher.setDescription(newVoucher.getDescription());
        voucher.setPrice(newVoucher.getPrice());
        voucher.setTourType(newVoucher.getTourType());
        voucher.setTransferType(newVoucher.getTransferType());
        voucher.setHotelType(newVoucher.getHotelType());
        voucher.setArrivalDate(newVoucher.getArrivalDate());
        voucher.setEvictionDate(newVoucher.getEvictionDate());
        voucher.setHot(newVoucher.isHot());

        if (newVoucher.getStatus().equals(VoucherStatus.PAID)
                || newVoucher.getStatus().equals(VoucherStatus.REGISTERED)) {
            throw new InvalidVoucherOperationException(INVALID_VOUCHER_OPERATION.name(),
                    String.format(INVALID_UPDATE, newVoucher.getStatus()));
        }
        voucher.setStatus(newVoucher.getStatus());
        return voucherMapper.toVoucherDTO(voucherRepository.save(voucher));
    }

    //todo for tests remove check if voucher has status paid
    @Override
    public void delete(String voucherId) {
        Voucher voucher = getVoucherById(voucherId);

        if (voucher.getStatus().equals(VoucherStatus.PAID)
                || voucher.getStatus().equals(VoucherStatus.REGISTERED)) {
            throw new InvalidVoucherOperationException(INVALID_VOUCHER_OPERATION.name(),
                    String.format(INVALID_DELETE, voucher.getStatus()));
        }
        voucherRepository.deleteById(UUID.fromString(voucherId));
    }

    //todo for tests remove check if voucher has status available
    @Override
    public VoucherDTO changeHotStatus(String id, VoucherDTO voucherDTO) {
        Voucher voucher = getVoucherById(id);
        Voucher newVoucher = voucherMapper.toVoucher(voucherDTO);

        if (!voucher.getStatus().equals(VoucherStatus.AVAILABLE)) {
            throw new InvalidVoucherOperationException(INVALID_VOUCHER_OPERATION.name(), INVALID_HOT_CHANGE);
        }
        voucher.setHot(newVoucher.isHot());
        return voucherMapper.toVoucherDTO(voucherRepository.save(voucher));
    }

    @Override
    public VoucherDTO changeStatus(String id, VoucherDTO voucherDTO) {
        Voucher voucher = getVoucherById(id);
        VoucherStatus voucherStatus = VoucherStatus.valueOf(voucherDTO.getStatus().toUpperCase().strip());

        if (!voucher.getStatus().equals(VoucherStatus.REGISTERED)
                || !(voucherStatus.equals(VoucherStatus.PAID) || voucherStatus.equals(VoucherStatus.CANCELED))) {
            throw new InvalidVoucherOperationException(INVALID_VOUCHER_OPERATION.name(),
                    String.format(INVALID_STATUS_CHANGE, voucherStatus, voucher.getStatus()));
        }

        voucher.setStatus(voucherStatus);
        return voucherMapper.toVoucherDTO(voucherRepository.save(voucher));
    }

    @Override
    public List<VoucherDTO> findAll() {
        return voucherRepository.findAll().stream()
                .map(voucherMapper::toVoucherDTO)
                .toList();
    }

    @Override
    public VoucherDTO findById(String id) {
        return voucherMapper.toVoucherDTO(getVoucherById(id));
    }

    @Override
    public List<VoucherDTO> findAllOrderByIsHot() {
        return voucherRepository.findAllOrderByIsHotDesc().stream()
                .map(voucherMapper::toVoucherDTO)
                .toList();
    }

    @Override
    public List<VoucherDTO> findAllByUserId(String userId) {
        return voucherRepository.findAllByUserId(UUID.fromString(userId)).stream()
                .map(voucherMapper::toVoucherDTO)
                .toList();
    }

    @Override
    public List<VoucherDTO> findAllByTourType(String tourType) {
        TourType tourTypeValue = Enum.valueOf(TourType.class, tourType.toUpperCase().strip());

        return voucherRepository.findAllByTourTypeOrderByIsHotDesc(tourTypeValue).stream()
                .map(voucherMapper::toVoucherDTO)
                .toList();
    }

    @Override
    public List<VoucherDTO> findAllByTransferType(String transferType) {
        TransferType transferTypeValue = Enum.valueOf(TransferType.class, transferType.toUpperCase().strip());

        return voucherRepository.findAllByTransferTypeOrderByIsHotDesc(transferTypeValue).stream()
                .map(voucherMapper::toVoucherDTO)
                .toList();
    }

    @Override
    public List<VoucherDTO> findAllByHotelType(String hotelType) {
        HotelType hotelTypeValue = Enum.valueOf(HotelType.class, hotelType.toUpperCase().strip());

        return voucherRepository.findAllByHotelTypeOrderByIsHotDesc(hotelTypeValue).stream()
                .map(voucherMapper::toVoucherDTO)
                .toList();
    }

    @Override
    public List<VoucherDTO> findAllByPrice(double price) {
        return voucherRepository.findAllByPriceLessThanEqualOrderByIsHotDesc(price).stream()
                .map(voucherMapper::toVoucherDTO)
                .toList();
    }

    @Override
    public VoucherDTO order(String id, String userId) {
        Voucher voucher = getVoucherById(id);
        User user = userMapper.toUser(userService.getUserById(UUID.fromString(userId)));

        if (voucher.getUser() != null) {
            throw new EntityAlreadyExistsException(DUPLICATE_ORDER.name(), VOUCHER_ALREADY_ORDERED);
        }

        voucher.setUser(user);
        voucher.setStatus(VoucherStatus.REGISTERED);

        return voucherMapper.toVoucherDTO(voucherRepository.save(voucher));
    }

    @Override
    public Page<VoucherDTO> search(VoucherSearchParametersDto params, Pageable pageable) {
        Specification<Voucher> voucherSpecification = specificationBuilder.build(params);
        return voucherRepository.findAll(voucherSpecification, pageable)
                .map(voucherMapper::toVoucherDTO);
    }

    @Override
    public Page<VoucherDTO> findAllByUserId(String userId, Pageable pageable) {
        return voucherRepository.findAllByUserId(UUID.fromString(userId), pageable)
                .map(voucherMapper::toVoucherDTO);
    }

    @Override
    public Page<VoucherDTO> findAll(Pageable pageable) {
        return voucherRepository.findAll(pageable)
                .map(voucherMapper::toVoucherDTO);
    }

    private Voucher getVoucherById(String id) {
        return voucherRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND.name(),
                        VOUCHER_NOT_EXISTS));
    }
}
