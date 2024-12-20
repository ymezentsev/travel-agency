package com.epam.finaltask.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "\"user\"")
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String username;

    @Column
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Voucher> vouchers;

    @Column
    private String phoneNumber;

    @Column
    private Double balance;

    @Column
    private boolean accountStatus;

    @Column
    private String email;

    public User(UUID id, String username, String password,
                Role role, List<Voucher> vouchers, String phoneNumber,
                Double balance, boolean accountStatus) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.vouchers = vouchers;
        this.phoneNumber = phoneNumber;
        this.balance = balance;
        this.accountStatus = accountStatus;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountStatus;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
