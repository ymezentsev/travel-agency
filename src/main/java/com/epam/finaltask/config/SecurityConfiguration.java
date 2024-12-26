package com.epam.finaltask.config;

import com.epam.finaltask.token.CookieAuthenticationFilter;
import com.epam.finaltask.token.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CookieAuthenticationFilter cookieAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers("/auth/**",
                                        "/users/register",
                                        "/images/**",
                                        "/css/**",

                                        "/v1/auth/**",
                                        "/v1/users/anonymous/**",
                                        "/v1/vouchers/anonymous/**",

                                       /* "/api/v1/users/forgot-password/**",
                                        "/api/v1/users/reset-password/**",
                                        "/api/v1/google/**",
                                        "/error",*/
                                        "/v3/api-docs/**",
                                        "/swagger-ui/**")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, "vouchers/**").permitAll()
                                .requestMatchers("/v1/vouchers/auth-manager/**")
                                .hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN")
                                .requestMatchers("/v1/vouchers/auth-admin/**").hasAuthority("ROLE_ADMIN")
                                .requestMatchers("/v1/users/auth-admin/**").hasAuthority("ROLE_ADMIN")
                                .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(cookieAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(form -> form.loginPage("/v1/vouchers/anonymous/index")
                        .permitAll())
                .logout(logout -> logout.clearAuthentication(true)
                        .deleteCookies("jwtToken")
                        .logoutUrl("/logout"))
                .build();
    }
}
