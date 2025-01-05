package com.epam.finaltask.service;

import com.epam.finaltask.dto.VoucherDTO;
import com.epam.finaltask.dto.VoucherSearchParamsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VoucherService {
    VoucherDTO create(VoucherDTO voucherDTO);

    VoucherDTO update(String voucherId, VoucherDTO voucherDTO);

    void delete(String voucherId);

    VoucherDTO changeHotStatus(String voucherId, VoucherDTO voucherDTO);

    VoucherDTO changeStatus(String voucherId, VoucherDTO voucherDTO);

    VoucherDTO findById(String voucherId);

    VoucherDTO order(String voucherId, String userId);

    Page<VoucherDTO> findAll(Pageable pageable);

    Page<VoucherDTO> findAllByUserId(String userId, Pageable pageable);

    Page<VoucherDTO> search(VoucherSearchParamsDto params, Pageable pageable);

    VoucherDTO cancelOrder(String voucherId);

    VoucherDTO payVoucher(String voucherId);
}
