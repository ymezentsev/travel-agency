package com.epam.finaltask.service;

import com.epam.finaltask.dto.*;
import com.epam.finaltask.exception.EntityAlreadyExistsException;
import com.epam.finaltask.exception.EntityNotFoundException;
import com.epam.finaltask.exception.InvalidPasswordException;
import com.epam.finaltask.mapper.UserMapper;
import com.epam.finaltask.model.Role;
import com.epam.finaltask.model.User;
import com.epam.finaltask.repository.UserRepository;
import com.epam.finaltask.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

import static com.epam.finaltask.exception.StatusCodes.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserSpecification userSpecification;

    private static final String USER_NOT_FOUND = "User not found";
    private static final String USER_ALREADY_EXISTS = "This username is already exist";
    private static final String INVALID_PASSWORD = "Invalid current password";

    @Override
    public UserDTO register(UserDTO userDTO) {
//        userDTO.setUsername(userDTO.getUsername().toLowerCase().strip());
        checkIfUserExists(userDTO.getUsername());

        User user = userMapper.toUser(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(Role.USER);
        user.setAccountStatus(true);
        user.setBalance(0.0);
        user.setVouchers(new ArrayList<>());
        return userMapper.toUserDTO(userRepository.save(user));
    }

    @Override
    public UserDTO updateUser(String username, UserDTO userDTO) {
//        username = username.toLowerCase().strip();
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND.name(), USER_NOT_FOUND));

        if (userDTO.getRole() != null) {
            userDTO.setRole(userDTO.getRole().toUpperCase().strip());
        }
        userMapper.toUser(userDTO);

//        userDTO.setUsername(userDTO.getUsername().toLowerCase().strip());
        if (!username.equals(userDTO.getUsername()) && userDTO.getUsername() != null) {
            checkIfUserExists(userDTO.getUsername());
        }

        if (userDTO.getUsername() != null) {
            user.setUsername(userDTO.getUsername());
        }
        if (userDTO.getRole() != null) {
            //userDTO.setRole(userDTO.getRole().toUpperCase().strip());
            user.setRole(Role.valueOf(userDTO.getRole()));
        }
        if (userDTO.getPhoneNumber() != null) {
            user.setPhoneNumber(userDTO.getPhoneNumber());
        }
        if (userDTO.getBalance() != null) {
            user.setBalance(userDTO.getBalance());
        }
        if (userDTO.getEmail() != null) {
            user.setEmail((userDTO.getEmail()));
        }
        //todo delete setAccountStatus from method and test
        user.setAccountStatus(userDTO.isAccountStatus());

        return userMapper.toUserDTO(userRepository.save(user));
    }

    @Override
    public UserDTO getUserByUsername(String username) {
//        username = username.toLowerCase().strip();
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND.name(), USER_NOT_FOUND));
        return userMapper.toUserDTO(user);
    }

    @Override
    public UserDTO changeAccountStatus(UserDTO userDTO) {
        User user = userRepository.findById(UUID.fromString(userDTO.getId()))
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND.name(), USER_NOT_FOUND));
        userMapper.toUser(userDTO);

        user.setAccountStatus(userDTO.isAccountStatus());
        return userMapper.toUserDTO(userRepository.save(user));
    }

    @Override
    public UserDTO changeRole(UserDTO userDTO) {
        User user = userRepository.findById(UUID.fromString(userDTO.getId()))
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND.name(), USER_NOT_FOUND));
        userMapper.toUser(userDTO);

        user.setRole(Role.valueOf(userDTO.getRole()));
        return userMapper.toUserDTO(userRepository.save(user));
    }

    @Override
    public UserDTO getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND.name(), USER_NOT_FOUND));

        return userMapper.toUserDTO(user);
    }

    @Override
    public UserDTO updateBalance(UserDTO userDTO) {
        User user = userRepository.findById(UUID.fromString(userDTO.getId()))
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND.name(), USER_NOT_FOUND));

        user.setBalance(userDTO.getBalance());
        return userMapper.toUserDTO(userRepository.save(user));
    }

    @Override
    public void changePassword(ChangePasswordRequestDto request, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND.name(), USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new InvalidPasswordException(INVALID_CREDENTIALS.name(), INVALID_PASSWORD);
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
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

    private void checkIfUserExists(String username) {
//        username = username.toLowerCase().strip();
        if (userRepository.existsByUsername(username)) {
            throw new EntityAlreadyExistsException(DUPLICATE_USERNAME.name(), USER_ALREADY_EXISTS);
        }
    }
}
