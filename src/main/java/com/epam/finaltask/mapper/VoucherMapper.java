package com.epam.finaltask.mapper;

import com.epam.finaltask.dto.VoucherDTO;
import com.epam.finaltask.model.Voucher;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface VoucherMapper {
    VoucherMapper INSTANCE = Mappers.getMapper(VoucherMapper.class);

    Voucher toVoucher(VoucherDTO voucherDTO);
    VoucherDTO toVoucherDTO(Voucher voucher);
}
