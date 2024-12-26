package com.epam.finaltask.token;

import com.epam.finaltask.exception.EntityNotFoundException;
import com.epam.finaltask.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.epam.finaltask.exception.StatusCodes.ENTITY_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        username = username.toLowerCase().strip();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND.name(), "Can't find user by username"));
    }
}