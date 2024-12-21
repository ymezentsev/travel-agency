package com.epam.finaltask.mapper;

import com.epam.finaltask.dto.VoucherDTO;
import com.epam.finaltask.model.Voucher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface VoucherMapper {
    @Mappings({
            @Mapping(target = "user", ignore = true)
    })
    Voucher toVoucher(VoucherDTO voucherDTO);

    @Mappings({
            @Mapping(target = "userId", source = "user.id"),
            @Mapping(target = "username", source = "user.username"),
            @Mapping(target = "isHot", source = "hot")
    })
    VoucherDTO toVoucherDTO(Voucher voucher);
}
