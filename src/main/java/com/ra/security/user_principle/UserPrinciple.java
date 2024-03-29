package com.ra.security.user_principle;

import com.ra.model.entity.Cart;
import com.ra.model.entity.Orders;
import com.ra.model.entity.User;
import com.ra.model.entity.WishList;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserPrinciple implements UserDetails {
    private Long id;
    private String userName;
    private String password;
    private String email;
    private String phone;
    private Boolean status;
    private int age;
    private Boolean gender;
    private String address;
    private Set<Orders> orders=new HashSet<>();
    private Cart cart ;
    private WishList wishList;
    private Collection<? extends GrantedAuthority> authorities;

    public static UserDetails build(User user) {
        return UserPrinciple.builder().
                id(user.getId()).
                userName(user.getUserName()).
                password(user.getPassword()).
                email(user.getEmail()).
                phone(user.getPhone()).
                status(user.getStatus()).
                age(user.getAge()).
                gender(user.getGender()).
                address(user.getAddress()).
                orders(user.getOrders()).
                cart(user.getCart()).
                wishList(user.getWishList()).
                authorities(user.getRoles().stream().map(item -> new SimpleGrantedAuthority(item.getName())).toList()).build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
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