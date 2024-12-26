package com.epam.finaltask.service;

import com.epam.finaltask.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {
    UserDTO register(UserDTO userDTO);

    UserDTO updateUser(String username, UserDTO userDTO);

    UserDTO getUserByUsername(String username);

    UserDTO changeAccountStatus(UserDTO userDTO);
    UserDTO changeRole(UserDTO userDTO);

    UserDTO getUserById(UUID id);

    UserDTO updateBalance(UserDTO userDTO);

    void changePassword(ChangePasswordRequestDto requestDto, UUID userId);

    void resetPassword(String newPassword, String token);

    Page<UserDTO> getAllUsers(Pageable pageable);
    Page<UserDTO> search(UserSearchParamsDto params, Pageable pageable);
}
