package com.epam.finaltask.service;

import com.epam.finaltask.dto.ChangePasswordRequest;
import com.epam.finaltask.dto.UserDTO;
import com.epam.finaltask.dto.UserSearchParamsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {
    UserDTO register(UserDTO userDTO);

    UserDTO updateUser(UUID userId, UserDTO userDTO);

    UserDTO getUserByUsername(String username);

    UserDTO getUserById(UUID userId);

    Page<UserDTO> getAllUsers(Pageable pageable);

    Page<UserDTO> search(UserSearchParamsDto params, Pageable pageable);

    UserDTO changeAccountStatus(UserDTO userDTO);

    UserDTO changeRole(UserDTO userDTO);

    UserDTO updateBalance(UserDTO userDTO);

    void changePassword(ChangePasswordRequest requestDto, UUID userId);

    void resetPassword(String newPassword, String token);
}
