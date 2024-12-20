package com.epam.finaltask.mapper;

import com.epam.finaltask.dto.UserDTO;
import com.epam.finaltask.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {VoucherMapper.class})
public interface UserMapper {
    User toUser(UserDTO userDTO);

    @Mapping(target = "password", ignore = true)
    UserDTO toUserDTO(User user);
}
