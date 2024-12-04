package com.epam.finaltask.config;

import com.epam.finaltask.token.AuthEntryPointJwt;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
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
    private final AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
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
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .httpBasic(withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers("/auth/**",
                                        "/users/register"/*,
                                        "/api/v1/users/forgot-password/**",
                                        "/api/v1/users/reset-password/**",
                                        "/api/v1/google/**",
                                        "/error",
                                        "/v3/api-docs/**",
                                        "/swagger-ui/**"*/)
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, "vouchers/**").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "vouchers/**").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.PATCH, "vouchers/**", "vouchers/change/**")
                                .hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.PATCH, "vouchers/**").hasAuthority("MANAGER")
                                .requestMatchers("/users/accountStatus",
                                        "vouchers/create").hasAuthority("ADMIN")
                                .anyRequest().authenticated()
                        //  .anyRequest().permitAll()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                /*.formLogin(form -> form.loginPage("/V2/urls")
                        .permitAll())
                .logout(logout -> logout
                        .deleteCookies("jwtToken")
                        .logoutUrl("/logout"))*/
                .logout(logout -> logout.clearAuthentication(true)
                        .logoutUrl("/logout"))
                .build();
    }
}
