package com.epam.finaltask.service;

import com.epam.finaltask.dto.VoucherDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VoucherService {
    VoucherDTO create(VoucherDTO voucherDTO);
    VoucherDTO update(String id, VoucherDTO voucherDTO);
    void delete(String voucherId);
    VoucherDTO changeHotStatus(String id, VoucherDTO voucherDTO);
    List<VoucherDTO> findAll();
    List<VoucherDTO> findAllByUserId(String userId);
    List<VoucherDTO> findAllByTourType(String tourType);
    List<VoucherDTO> findAllByTransferType(String transferType);
    List<VoucherDTO> findAllByPrice(String price);
    List<VoucherDTO> findAllByHotelType(String hotelType);
    VoucherDTO order(String id, String userId);



    Page<VoucherDTO> findAllWithPage(Pageable pageable);
}
