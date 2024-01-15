package com.ra.model.dto.response;

import com.ra.model.entity.*;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Data
public class UserResponseDTO {
    private Long id;
    private String userName;
    @Column(columnDefinition = "Boolean default true")
    private Boolean status=true;
    private String email;
    private Boolean gender;
    private String phone;
    private int age;
    private String address;
    private Set<String> roles;
    private Set<Orders> orders;
    private Cart cart;
    private WishList wishList;
    private String token;



    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.userName = user.getUserName();
        this.status = user.getStatus();
        this.email=user.getEmail();
        this.gender=user.getGender();
        this.phone=user.getPhone();
        this.age= user.getAge();
        this.address=user.getAddress();
        this.orders=user.getOrders();
        this.cart=user.getCart();
        this.wishList= user.getWishList();
        this.roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
    }
}
