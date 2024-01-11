package com.ra.model.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Data
public class UserRequestDTO {
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

}
