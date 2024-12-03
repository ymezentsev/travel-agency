package com.epam.finaltask.service;

import com.epam.finaltask.dto.UserDTO;
import com.epam.finaltask.exception.EntityAlreadyExistsException;
import com.epam.finaltask.exception.EntityNotFoundException;
import com.epam.finaltask.mapper.UserMapper;
import com.epam.finaltask.model.Role;
import com.epam.finaltask.model.User;
import com.epam.finaltask.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void registerUser_Success() {
        // Given
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("TestUser123");
        userDTO.setPassword("Passw0rd");
        userDTO.setRole("USER");
        userDTO.setAccountStatus(false);
        userDTO.setBalance(100.0);
        String encryptedPassword = "$2a$12$Tq7niswv6pFvG/u/Xvic0Oae88eFXKsBsiB8IeLre8QSWB4/lNS32";


        User user = User.builder()
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .role(Role.USER)
                .accountStatus(userDTO.isAccountStatus())
                .balance(userDTO.getBalance())
                .vouchers(null)
                .phoneNumber(null)
                .build();

        when(userMapper.toUser(any(UserDTO.class))).thenReturn(user);
        when(userRepository.existsByUsername(userDTO.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(eq("Passw0rd"))).thenReturn(encryptedPassword);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toUserDTO(any(User.class))).thenReturn(userDTO);

        // When
        UserDTO registeredUserDTO = userService.register(userDTO);

        // Then
        assertNotNull(registeredUserDTO, "Registered UserDTO should not be null");
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode(eq("Passw0rd"));
    }

    @Test
    void checkUserData_UsernameIsAlreadyExistsInDB_ThrowEntityAlreadyExistsException() {
        // Given
        UserDTO userDto = new UserDTO();
        userDto.setUsername("userName");
        userDto.setPassword("passwordTT19");
        String errorMessage = "This username is already exist";
        String errorCode = "DUPLICATE_USERNAME";

        // When
        when(userRepository.existsByUsername(userDto.getUsername())).thenReturn(true);

        // Then
        EntityAlreadyExistsException exception = assertThrows(EntityAlreadyExistsException.class, () ->
                userService.register(userDto));

        assertEquals(EntityAlreadyExistsException.class, exception.getClass());
        assertEquals(errorMessage, exception.getMessage());
        assertEquals(errorCode, exception.getErrorCode());
    }

    @Test
    void updateUser_Success() {
        // Given
        String existingUsername = "existingUser";

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("UpdatedUser");
        userDTO.setRole("ADMIN");
        userDTO.setAccountStatus(true);
        userDTO.setBalance(200.0);

        User existingUser = new User();
        existingUser.setUsername("existingUser");
        existingUser.setRole(Role.USER);
        existingUser.setAccountStatus(false);
        existingUser.setBalance(100.0);

        User updatedUser = new User();
        updatedUser.setUsername(userDTO.getUsername());
        updatedUser.setRole(Role.valueOf(userDTO.getRole()));
        updatedUser.setAccountStatus(userDTO.isAccountStatus());
        updatedUser.setBalance(userDTO.getBalance());

        when(userRepository.findUserByUsername(existingUsername)).thenReturn(Optional.of(existingUser));
        when(userMapper.toUser(any(UserDTO.class))).thenReturn(updatedUser);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(userMapper.toUserDTO(any(User.class))).thenReturn(userDTO);

        // When
        UserDTO result = userService.updateUser(existingUsername, userDTO);

        // Then
        assertNotNull(result, "The returned UserDTO should not be null");
        assertEquals(userDTO.getUsername(), result.getUsername());
        assertEquals(userDTO.getRole(), result.getRole());
        assertEquals(userDTO.isAccountStatus(), result.isAccountStatus());
        assertEquals(userDTO.getBalance(), result.getBalance());

        verify(userRepository, times(1)).findUserByUsername(existingUsername);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void getUserByUsername_UserExists_Success() {
        // Given
        String username = "existingUser";
        User user = new User();
        user.setUsername(username);

        UserDTO expectedUserDTO = new UserDTO();
        expectedUserDTO.setUsername(username);

        when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));
        when(userMapper.toUserDTO(any(User.class))).thenReturn(expectedUserDTO);

        // When
        UserDTO result = userService.getUserByUsername(username);

        // Then
        assertNotNull(result, "The returned UserDTO should not be null");
        assertEquals(expectedUserDTO.getUsername(), result.getUsername(), "The username should match the expected value");

        verify(userRepository, times(1)).findUserByUsername(username);
        verify(userMapper, times(1)).toUserDTO(any(User.class));
    }

    @Test
    void getUserByUsername_UserDoesNotExist_ThrowsEntityNotFoundException() {
        // Given
        String username = "nonExistentUser";
        when(userRepository.findUserByUsername(username)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            userService.getUserByUsername(username);
        }, "Expected EntityNotFoundException to be thrown if user is not found");

        verify(userRepository, times(1)).findUserByUsername(username);
        verify(userMapper, never()).toUserDTO(any(User.class));
    }

    @Test
    void changeAccountStatus_UserExist_Success() {
        // Given
        String userId = UUID.randomUUID().toString();
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setAccountStatus(true);

        User user = new User();
        user.setId(UUID.fromString(userId));
        user.setAccountStatus(false);

        User updatedUser = new User();
        updatedUser.setId(UUID.fromString(userId));
        updatedUser.setAccountStatus(true);

        when(userRepository.findById(UUID.fromString(userId))).thenReturn(Optional.of(user));
        when(userMapper.toUser(any(UserDTO.class))).thenReturn(updatedUser);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(userMapper.toUserDTO(any(User.class))).thenReturn(userDTO);

        // When
        UserDTO resultDTO = userService.changeAccountStatus(userDTO);

        // Then
        assertNotNull(resultDTO, "The returned UserDTO should not be null");
        assertTrue(resultDTO.isAccountStatus(), "The account status should be updated to true");

        verify(userRepository, times(1)).findById(UUID.fromString(userId));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void changeAccountStatus_UserDoesNotExist_ThrowEntityNotFoundException() {
        // Given
        String userId = UUID.randomUUID().toString();
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);

        when(userRepository.findById(UUID.fromString(userId))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            userService.changeAccountStatus(userDTO);
        }, "Expected EntityNotFoundException to be thrown if user is not found");

        verify(userRepository, times(1)).findById(UUID.fromString(userId));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUserById_UserExist_Success() {
        // Given
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setId(id);

        UserDTO expectedUserDTO = new UserDTO();
        expectedUserDTO.setId(id.toString());

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.toUserDTO(any(User.class))).thenReturn(expectedUserDTO);

        // When
        UserDTO resultDTO = userService.getUserById(id);

        // Then
        assertNotNull(resultDTO, "The returned UserDTO should not be null");
        assertEquals(expectedUserDTO.getId(), resultDTO.getId(), "The user ID should match the expected value");

        verify(userRepository, times(1)).findById(id);
        verify(userMapper, times(1)).toUserDTO(any(User.class));
    }

    @Test
    void getUserById_UserDoesNotExist_ThrowEntityNotFoundException() {
        // Given
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            userService.getUserById(id);
        }, "Expected EntityNotFoundException to be thrown if user is not found");

        verify(userRepository, times(1)).findById(id);
        verify(userMapper, never()).toUserDTO(any(User.class)); // Ensure the mapper is not called
    }
}
