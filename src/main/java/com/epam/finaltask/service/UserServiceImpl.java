package com.epam.finaltask.service;

import com.epam.finaltask.dto.UserDTO;
import com.epam.finaltask.exception.EntityAlreadyExistsException;
import com.epam.finaltask.exception.EntityNotFoundException;
import com.epam.finaltask.mapper.UserMapper;
import com.epam.finaltask.model.Role;
import com.epam.finaltask.model.User;
import com.epam.finaltask.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.epam.finaltask.exception.StatusCodes.DUPLICATE_USERNAME;
import static com.epam.finaltask.exception.StatusCodes.ENTITY_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String USER_NOT_FOUND = "User not found";

    @Override
    public UserDTO register(UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new EntityAlreadyExistsException(DUPLICATE_USERNAME.name(),
                    "This username is already exist");
        }
        User user = userMapper.toUser(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return userMapper.toUserDTO(userRepository.save(user));
    }

    @Override
    public UserDTO updateUser(String username, UserDTO userDTO) {
        User user = userMapper.toUser(getUserByUsername(username));

        user.setRole(Role.valueOf(userDTO.getRole()));
        user.setAccountStatus(userDTO.isAccountStatus());
        user.setBalance(userDTO.getBalance());
        user.setPhoneNumber(userDTO.getPhoneNumber());

        return userMapper.toUserDTO(userRepository.save(user));
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND.name(), USER_NOT_FOUND));
        return userMapper.toUserDTO(user);
    }

    @Override
    public UserDTO changeAccountStatus(UserDTO userDTO) {
        User user = userMapper.toUser(
                getUserById(UUID.fromString(userDTO.getId())));

        user.setAccountStatus(userDTO.isAccountStatus());
        return userMapper.toUserDTO(userRepository.save(user));
    }

    @Override
    public UserDTO getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND.name(), USER_NOT_FOUND));

        return userMapper.toUserDTO(user);
    }
}
