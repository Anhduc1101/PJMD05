package com.ra.model.dto.request;

import com.ra.model.entity.Role;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

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
    private String phone;
    private int age;
    private String address;
    private Set<String> roles;
}
