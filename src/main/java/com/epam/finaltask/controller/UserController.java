package com.epam.finaltask.controller;

import com.epam.finaltask.dto.RemoteResponse;
import com.epam.finaltask.dto.UserDTO;
import com.epam.finaltask.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.epam.finaltask.exception.StatusCodes.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    private static final String STATUS_MESSAGE_CREATED = "User is successfully registered";
    private static final String STATUS_MESSAGE_UPDATED = "User is successfully updated";
    private static final String STATUS_MESSAGE_GET = "User was obtained successfully";
    private static final String STATUS_MESSAGE_CHANGED = "User's account status is successfully changed";

    @PostMapping("/register")
    public ResponseEntity<RemoteResponse> registerUser(@Valid @RequestBody UserDTO userDto) {
        return new ResponseEntity<>(new RemoteResponse(true, OK.name(),
                STATUS_MESSAGE_CREATED, List.of(userService.register(userDto))),
                HttpStatus.CREATED);
    }

    @PatchMapping("/{username}")
    public ResponseEntity<RemoteResponse> updateUser(@PathVariable("username") String username,
                                                        @Valid @RequestBody UserDTO userDto) {
        return new ResponseEntity<>(new RemoteResponse(true, OK.name(),
                STATUS_MESSAGE_UPDATED, List.of(userService.updateUser(username, userDto))),
                HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<RemoteResponse> getUserByUsername(@PathVariable("username") String username) {
        return new ResponseEntity<>(new RemoteResponse(true, OK.name(),
                STATUS_MESSAGE_GET, List.of(userService.getUserByUsername(username))),
                HttpStatus.OK);
    }

    @PatchMapping("/accountStatus")
    public ResponseEntity<RemoteResponse> changeAccountStatus(@Valid @RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(new RemoteResponse(true, OK.name(),
                STATUS_MESSAGE_CHANGED, List.of(userService.changeAccountStatus(userDTO))),
                HttpStatus.OK);
    }
}
