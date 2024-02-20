package com.epam.finaltask.service;

import com.epam.finaltask.dto.VoucherDTO;
import com.epam.finaltask.exception.EntityNotFoundException;
import com.epam.finaltask.mapper.VoucherMapper;
import com.epam.finaltask.model.*;
import com.epam.finaltask.repository.UserRepository;
import com.epam.finaltask.repository.VoucherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.epam.finaltask.exception.StatusCodes.ENTITY_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoucherServiceImpl implements VoucherService{

    private final VoucherRepository voucherRepository;
    private final UserRepository userRepository;
    private final VoucherMapper voucherMapper;

    @Override
    public VoucherDTO create(VoucherDTO voucherDTO) {
        Voucher voucher = voucherMapper.toVoucher(voucherDTO);
        return voucherMapper.toVoucherDTO(voucherRepository.save(voucher));
    }

    @Override
    public VoucherDTO order(String id, String userId) {
        Voucher voucher = voucherRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND.name(), "Voucher not found"));

        User user = userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND.name(), "User not found"));

        voucher.setUser(user);
        voucher.setStatus(VoucherStatus.REGISTERED);

        return voucherMapper.toVoucherDTO(voucherRepository.save(voucher));
    }

    @Override
    public VoucherDTO update(String id, VoucherDTO voucherDTO) {
        Voucher voucher = voucherRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND.name(), "Voucher not found"));

        Voucher newVoucher = voucherMapper.toVoucher(voucherDTO);

        voucher.setTitle(newVoucher.getTitle());
        voucher.setDescription(newVoucher.getDescription());
        voucher.setPrice(newVoucher.getPrice());
        voucher.setTourType(newVoucher.getTourType());
        voucher.setTransferType(newVoucher.getTransferType());
        voucher.setHotelType(newVoucher.getHotelType());
        voucher.setStatus(newVoucher.getStatus());
        voucher.setArrivalDate(newVoucher.getArrivalDate());
        voucher.setEvictionDate(newVoucher.getEvictionDate());
        voucher.setUser(newVoucher.getUser());
        voucher.setHot(newVoucher.isHot());

        return voucherMapper.toVoucherDTO(voucherRepository.save(voucher));
    }

    @Override
    public void delete(String voucherId) {
        Voucher voucher = voucherRepository.findById(UUID.fromString(voucherId))
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND.name(), "Voucher not found"));

        voucherRepository.deleteById(UUID.fromString(voucherId));
    }

    @Override
    public VoucherDTO changeHotStatus(String id, VoucherDTO voucherDTO) {
        Voucher voucher = voucherRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND.name(), "Voucher not found"));

        Voucher newVoucher = voucherMapper.toVoucher(voucherDTO);

        voucher.setHot(newVoucher.isHot());
        voucher.setStatus(newVoucher.getStatus());

        return voucherMapper.toVoucherDTO(voucherRepository.save(voucher));
    }

    @Override
    public List<VoucherDTO> findAllByUserId(String userId) {
        log.info("Request for finding all vouchers by userId");
        return voucherRepository.findAllByUserId(UUID.fromString(userId)).stream()
                .map(voucherMapper::toVoucherDTO).collect(Collectors.toList());
    }

    @Override
    public List<VoucherDTO> findAllByTourType(String tourType) {
        log.info("Request for finding all vouchers by tourType");
        return voucherRepository.findAllByTourType(TourType.valueOf(tourType)).stream()
                .map(voucherMapper::toVoucherDTO).collect(Collectors.toList());
    }

    @Override
    public List<VoucherDTO> findAllByTransferType(String transferType) {
        log.info("Request for finding all vouchers by transferType");
        return voucherRepository.findAllByTransferType(TransferType.valueOf(transferType)).stream()
                .map(voucherMapper::toVoucherDTO).collect(Collectors.toList());
    }

    @Override
    public List<VoucherDTO> findAllByPrice(String price) {
        log.info("Request for finding all vouchers by price");
        return voucherRepository.findAllByPrice(Double.valueOf(price)).stream()
                .map(voucherMapper::toVoucherDTO).collect(Collectors.toList());
    }

    @Override
    public List<VoucherDTO> findAllByHotelType(String hotelType) {
        log.info("Request for finding all vouchers by hotelType");
        return voucherRepository.findAllByHotelType(HotelType.valueOf(hotelType)).stream()
                .map(voucherMapper::toVoucherDTO).collect(Collectors.toList());
    }

    @Override
    public List<VoucherDTO> findAll() {
        log.info("Request for finding all existed vouchers");
        return voucherRepository.findAll().stream()
                .map(voucherMapper::toVoucherDTO).collect(Collectors.toList());
    }
}
