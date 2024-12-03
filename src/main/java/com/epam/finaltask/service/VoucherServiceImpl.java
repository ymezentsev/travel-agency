package com.epam.finaltask.service;

import com.epam.finaltask.dto.VoucherDTO;
import com.epam.finaltask.exception.EntityNotFoundException;
import com.epam.finaltask.mapper.VoucherMapper;
import com.epam.finaltask.model.*;
import com.epam.finaltask.repository.VoucherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {
    private final VoucherMapper voucherMapper;
    private final VoucherRepository voucherRepository;

    private static final String VOUCHER_IS_NOT_EXISTS = "Such voucher not exists";

    @Override
    public VoucherDTO create(VoucherDTO voucherDTO) {
        return voucherMapper.toVoucherDTO(
                voucherRepository.save(
                        voucherMapper.toVoucher(voucherDTO)));
    }

    @Override
    public VoucherDTO update(String id, VoucherDTO voucherDTO) {
        Voucher voucherToUpdate = voucherRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new EntityNotFoundException(VOUCHER_IS_NOT_EXISTS));

        Voucher newVoucherData = voucherMapper.toVoucher(voucherDTO);

        voucherToUpdate.setTitle(newVoucherData.getTitle());
        voucherToUpdate.setDescription(newVoucherData.getDescription());
        voucherToUpdate.setPrice(newVoucherData.getPrice());
        voucherToUpdate.setTourType(newVoucherData.getTourType());
        voucherToUpdate.setTransferType(newVoucherData.getTransferType());
        voucherToUpdate.setHotelType(newVoucherData.getHotelType());
        voucherToUpdate.setStatus(newVoucherData.getStatus());
        voucherToUpdate.setArrivalDate(newVoucherData.getArrivalDate());
        voucherToUpdate.setEvictionDate(newVoucherData.getEvictionDate());
        voucherToUpdate.setHot(newVoucherData.isHot());
        return voucherMapper.toVoucherDTO(voucherRepository.save(voucherToUpdate));
    }

    @Override
    public void delete(String voucherId) {
        voucherRepository.findById(UUID.fromString(voucherId))
                .orElseThrow(() -> new EntityNotFoundException(VOUCHER_IS_NOT_EXISTS));

        //  checkIfUserHasAuthorityToDeleteReservation(reservationForDeleting.get());
        voucherRepository.deleteById(UUID.fromString(voucherId));
    }

    @Override
    public VoucherDTO changeHotStatus(String id, VoucherDTO voucherDTO) {
        Voucher voucherToChangeStatus = voucherRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new EntityNotFoundException(VOUCHER_IS_NOT_EXISTS));

        Voucher newVoucherData = voucherMapper.toVoucher(voucherDTO);

        voucherToChangeStatus.setHot(newVoucherData.isHot());
        voucherToChangeStatus.setStatus(VoucherStatus.REGISTERED);
        return voucherMapper.toVoucherDTO(voucherRepository.save(voucherToChangeStatus));
    }

    @Override
    public List<VoucherDTO> findAll() {
        return voucherRepository.findAll().stream()
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

        return voucherRepository.findAllByTourType(tourTypeValue).stream()
                .map(voucherMapper::toVoucherDTO)
                .toList();
    }

    @Override
    public List<VoucherDTO> findAllByTransferType(String transferType) {
        TransferType transferTypeValue = Enum.valueOf(TransferType.class, transferType.toUpperCase().strip());

        return voucherRepository.findAllByTransferType(transferTypeValue).stream()
                .map(voucherMapper::toVoucherDTO)
                .toList();
    }

    @Override
    public List<VoucherDTO> findAllByHotelType(String hotelType) {
        HotelType hotelTypeValue = Enum.valueOf(HotelType.class, hotelType.toUpperCase().strip());

        return voucherRepository.findAllByHotelType(hotelTypeValue).stream()
                .map(voucherMapper::toVoucherDTO)
                .toList();
    }

    @Override
    public List<VoucherDTO> findAllByPrice(String price) {
        return voucherRepository.findAllByPriceLessThanEqual(Double.parseDouble(price)).stream()
                .map(voucherMapper::toVoucherDTO)
                .toList();
    }

    @Override
    public VoucherDTO order(String id, String userId) {
        return null;
    }






    @Override
    public Page<VoucherDTO> findAllWithPage(Pageable pageable) {
        return voucherRepository.findAll(pageable)
                .map(voucherMapper::toVoucherDTO);
     }
}
