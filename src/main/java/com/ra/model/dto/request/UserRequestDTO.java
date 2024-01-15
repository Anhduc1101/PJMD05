package com.ra.model.dto.request;

import com.ra.model.entity.Cart;
import com.ra.model.entity.Orders;
import com.ra.model.entity.Role;
import com.ra.model.entity.WishList;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Data
public class UserRequestDTO {
    private Long id;
    @NotEmpty(message = "fill in the blank")
    private String userName;
    @Size(min = 3, message = "password at least 3 letters")
    private String password;
    @Column(columnDefinition = "Boolean default true")
    private Boolean status = true;
    @Email(message = "Invalid format email")
    private String email;
    private Boolean gender;
    @Pattern(regexp = "(0[3|5|7|8|9])+([0-9]{8})\\b", message = "Enter the Vietnamese phone")
    private String phone;
    private int age;
    private String address;
    private Set<String> roles;
    private Set<Orders> orders;
    private Cart cart ;
    private WishList wishList;
}
