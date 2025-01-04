package com.epam.finaltask.token;

import com.epam.finaltask.exception.EntityNotFoundException;
import com.epam.finaltask.repository.UserRepository;
import com.epam.finaltask.util.I18nUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.epam.finaltask.model.enums.StatusCodes.ENTITY_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final I18nUtil i18nUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND.name(),
                        i18nUtil.getMessage("error.cannot-find-user")));
    }
}