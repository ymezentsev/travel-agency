package com.epam.finaltask.mapper;

import com.epam.finaltask.dto.VoucherDTO;
import com.epam.finaltask.model.Voucher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface VoucherMapper {

    Voucher toVoucher(VoucherDTO voucherDTO);

    @Mappings({
            @Mapping(target = "userId", source = "user.id"),
            @Mapping(target = "isHot", source = "hot")
    })
    VoucherDTO toVoucherDTO(Voucher voucher);
}
