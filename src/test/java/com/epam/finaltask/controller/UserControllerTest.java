package com.epam.finaltask.controller;

import com.epam.finaltask.dto.UserDTO;
import com.epam.finaltask.model.enums.StatusCodes;
import com.epam.finaltask.service.UserService;
import com.epam.finaltask.service.impl.AuthenticationServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureDataJpa
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestPropertySource(properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
public class UserControllerTest {
    @MockBean
    private UserService userService;
    @MockBean
    private AuthenticationServiceImpl authenticationService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerUser_ValidUserData_SuccessfullyRegistered() throws Exception {
        UserDTO userDto = new UserDTO();
        userDto.setUsername("user1");
        userDto.setPassword("PasSw1ord");
        userDto.setPhoneNumber("1234567890");
        userDto.setEmail("email@example.com");

        String expectedStatusCode = "OK";
        String expectedMessage = "User is successfully registered";

        when(userService.register(userDto)).thenReturn(userDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(expectedStatusCode))
                .andExpect(jsonPath("$.statusMessage").value(expectedMessage));
    }

    @Test
    void registerUser_ValidUserData_UnsatisfiedPassword() throws Exception {
        UserDTO userDto = new UserDTO();
        userDto.setUsername("user1");
        userDto.setPassword("cd");
        userDto.setPhoneNumber("1234567890");
        userDto.setEmail("email@example.com");

        String expectedStatusCode = "INVALID_DATA";
        String expectedMessage = "Your password must contain upper and lower case letters and numbers," +
                " at least 7 and maximum 30 characters. Password cannot contain spaces";

        when(userService.register(userDto)).thenReturn(userDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(expectedStatusCode))
                .andExpect(jsonPath("$.statusMessage").value(expectedMessage));
    }

    @Test
    void registerUser_ValidUserData_UnsatisfiedUsername() throws Exception {
        UserDTO userDto = new UserDTO();
        userDto.setUsername("user###1");
        userDto.setPassword("PasSw1ord");
        userDto.setPhoneNumber("1234567890");
        userDto.setEmail("email@example.com");

        String expectedStatusCode = "INVALID_DATA";
        String expectedMessage = "Username must be from 2 to 30 characters long and contains only characters and numbers";

        when(userService.register(userDto)).thenReturn(userDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(expectedStatusCode))
                .andExpect(jsonPath("$.statusMessage").value(expectedMessage));
    }

    @Test
    void registerUser_ValidUserData_UnsatisfiedPhoneNumber() throws Exception {
        UserDTO userDto = new UserDTO();
        userDto.setUsername("user1");
        userDto.setPassword("PasSw1ord");
        userDto.setPhoneNumber("abcdefg");
        userDto.setEmail("email@example.com");

        String expectedStatusCode = "INVALID_DATA";
        String expectedMessage = "Phone number must contain only numbers, at least 10 and maximum 14 characters";

        when(userService.register(userDto)).thenReturn(userDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(expectedStatusCode))
                .andExpect(jsonPath("$.statusMessage").value(expectedMessage));
    }

    @Test
    @WithMockUser
    void updateUser_allDataValid_UpdatedSuccessfully() throws Exception {
        UserDTO userDto = new UserDTO();
        userDto.setId(String.valueOf(UUID.randomUUID()));
        userDto.setPhoneNumber("1234567890");
        userDto.setEmail("email@example.com");

        when(userService.updateUser(UUID.fromString(userDto.getId()), userDto)).thenReturn(userDto);
        when(authenticationService.isCurrentUser(any(UUID.class))).thenReturn(true);
        String expectedStatusCode = StatusCodes.OK.name();
        String expectedMessage = "User is successfully updated";

        mockMvc.perform(put("/users/" + userDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(expectedStatusCode))
                .andExpect(jsonPath("$.statusMessage").value(expectedMessage));
    }

    @Test
    @WithMockUser
    void updateUser_nonValidPhone_BadRequest() throws Exception {
        UserDTO userDto = new UserDTO();
        userDto.setId(String.valueOf(UUID.randomUUID()));
        userDto.setPhoneNumber("qwerty");
        userDto.setEmail("email@example.com");

        when(userService.updateUser(UUID.fromString(userDto.getId()), userDto)).thenReturn(userDto);
        when(authenticationService.isCurrentUser(any(UUID.class))).thenReturn(true);
        String expectedStatusCode = StatusCodes.INVALID_DATA.name();
        String expectedMessage = "Phone number must contain only numbers, at least 10 and maximum 14 characters";

        mockMvc.perform(put("/users/" + userDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(expectedStatusCode))
                .andExpect(jsonPath("$.statusMessage").value(expectedMessage));
    }

    @Test
    @WithMockUser
    void updateUser_nonValidEmail_BadRequest() throws Exception {
        UserDTO userDto = new UserDTO();
        userDto.setId(String.valueOf(UUID.randomUUID()));
        userDto.setPhoneNumber("1234567890");
        userDto.setEmail("emailexample.com");

        when(userService.updateUser(UUID.fromString(userDto.getId()), userDto)).thenReturn(userDto);
        when(authenticationService.isCurrentUser(any(UUID.class))).thenReturn(true);
        String expectedStatusCode = StatusCodes.INVALID_DATA.name();
        String expectedMessage = "Not valid email";

        mockMvc.perform(put("/users/" + userDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(expectedStatusCode))
                .andExpect(jsonPath("$.statusMessage").value(expectedMessage));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void getUserByUsername_RetrievedSuccessfully() throws Exception {
        String username = "admin";

        UserDTO userDto = new UserDTO();
        userDto.setUsername("admin");
        userDto.setPassword("PasSw1ord");

        when(userService.getUserByUsername(username)).thenReturn(userDto);
        String expectedStatusCode = StatusCodes.OK.name();

        mockMvc.perform(get("/users/" + userDto.getUsername())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(expectedStatusCode));
    }

}
