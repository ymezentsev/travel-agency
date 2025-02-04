package com.epam.finaltask.service.impl;

import com.epam.finaltask.aspect.Loggable;
import com.epam.finaltask.dto.ChangePasswordRequest;
import com.epam.finaltask.dto.UserDTO;
import com.epam.finaltask.dto.UserSearchParamsDto;
import com.epam.finaltask.exception.EntityAlreadyExistsException;
import com.epam.finaltask.exception.EntityNotFoundException;
import com.epam.finaltask.exception.InvalidPasswordException;
import com.epam.finaltask.mapper.UserMapper;
import com.epam.finaltask.model.ResetPasswordToken;
import com.epam.finaltask.model.User;
import com.epam.finaltask.model.enums.Role;
import com.epam.finaltask.repository.UserRepository;
import com.epam.finaltask.service.ResetPasswordTokenService;
import com.epam.finaltask.service.UserService;
import com.epam.finaltask.specification.UserSpecification;
import com.epam.finaltask.util.I18nUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static com.epam.finaltask.model.enums.StatusCodes.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserSpecification userSpecification;
    private final ResetPasswordTokenService resetPasswordTokenService;
    private final I18nUtil i18nUtil;

    @Loggable(hideArgs = true)
    @Override
    public UserDTO register(UserDTO userDTO) {
        if (userRepository.existsByUsernameIgnoreCase(userDTO.getUsername())) {
            throw new EntityAlreadyExistsException(DUPLICATE_USERNAME.name(),
                    i18nUtil.getMessage("error.user-already-exists"));
        }

        User user = userMapper.toUser(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(Role.USER);
        user.setAccountStatus(true);
        user.setBalance(BigDecimal.ZERO);
        user.setVouchers(new ArrayList<>());
        return userMapper.toUserDTO(userRepository.save(user));
    }

    @Loggable
    @Override
    public UserDTO updateUser(UUID userId, UserDTO userDTO) {
        User user = getUser(userId);

        if (userDTO.getPhoneNumber() != null) {
            user.setPhoneNumber(userDTO.getPhoneNumber());
        }
        if (userDTO.getEmail() != null) {
            user.setEmail((userDTO.getEmail()));
        }
        return userMapper.toUserDTO(userRepository.save(user));
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND.name(),
                        i18nUtil.getMessage("error.user-not-found")));
        return userMapper.toUserDTO(user);
    }

    @Override
    public UserDTO getUserById(UUID userId) {
        return userMapper.toUserDTO(getUser(userId));
    }

    @Override
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toUserDTO);
    }

    @Override
    public Page<UserDTO> search(UserSearchParamsDto params, Pageable pageable) {
        return userRepository.findAll(userSpecification.build(params), pageable)
                .map(userMapper::toUserDTO);
    }

    @Loggable
    @Override
    public UserDTO changeAccountStatus(UserDTO userDTO) {
        User user = getUser(UUID.fromString(userDTO.getId()));

        user.setAccountStatus(userDTO.isAccountStatus());
        return userMapper.toUserDTO(userRepository.save(user));
    }

    @Loggable
    @Override
    public UserDTO changeRole(UserDTO userDTO) {
        User user = getUser(UUID.fromString(userDTO.getId()));

        user.setRole(Role.valueOf(userDTO.getRole().toUpperCase().strip()));
        return userMapper.toUserDTO(userRepository.save(user));
    }

    @Loggable
    @Override
    public UserDTO updateBalance(UserDTO userDTO) {
        User user = getUser(UUID.fromString(userDTO.getId()));

        user.setBalance(userDTO.getBalance());
        return userMapper.toUserDTO(userRepository.save(user));
    }

    @Loggable(hideArgs = true)
    @Override
    public void changePassword(ChangePasswordRequest request, UUID userId) {
        User user = getUser(userId);

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new InvalidPasswordException(INVALID_CREDENTIALS.name(),
                    i18nUtil.getMessage("error.invalid-password"));
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Loggable(hideArgs = true)
    @Override
    public void resetPassword(String newPassword, String token) {
        ResetPasswordToken resetPasswordToken = resetPasswordTokenService.getResetPasswordToken(token);
        resetPasswordTokenService.validateResetPasswordToken(resetPasswordToken);

        resetPasswordToken.setConfirmedAt(LocalDateTime.now());
        resetPasswordTokenService.saveResetPasswordToken(resetPasswordToken);

        User user = resetPasswordToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private User getUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND.name(),
                        i18nUtil.getMessage("error.user-not-found")));
    }
}
