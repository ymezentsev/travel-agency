package com.epam.finaltask.auth;

import com.epam.finaltask.config.JwtService;
import com.epam.finaltask.exception.EntityNotFoundException;
import com.epam.finaltask.model.User;
import com.epam.finaltask.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.epam.finaltask.exception.StatusCodes.INVALID_PASSWORD;
import static com.epam.finaltask.exception.StatusCodes.IRRELEVANT_USERNAME;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = userRepository.findUserByUsername(request.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(IRRELEVANT_USERNAME.name(), "This user does not exist"));

        if (!(passwordEncoder.matches(request.getPassword(), user.getPassword()))) {
            throw new EntityNotFoundException(INVALID_PASSWORD.name(), "Wrong password. Please try again");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }

}
