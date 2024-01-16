package com.ra.model.dto.response;

import com.ra.model.entity.Role;
import com.ra.model.entity.User;
import jakarta.persistence.Column;
import lombok.*;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserInfoResponseDTO {
    private Long id;
    private String userName;
    private Boolean status=true;
    private String email;
    private Boolean gender;
    private String phone;
    private int age;
    private String address;
//    private Set<String> roles;

    public UserInfoResponseDTO(User user) {
//        this.id = user.getId();
        this.userName = user.getUserName();
//        this.status = user.getStatus();
        this.email = user.getEmail();
        this.gender = user.getGender();
        this.phone = user.getPhone();
        this.age = user.getAge();
        this.address = user.getAddress();
//        this.roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
    }
}
