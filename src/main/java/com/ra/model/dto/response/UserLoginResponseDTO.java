package com.ra.model.dto.response;

import lombok.*;

import java.util.Set;
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserLoginResponseDTO {
    private String userName;
    private Set<String> roles;
    private String token;
    private Boolean status;
}
