package com.ra.model.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserChangePassRequestDTO {
    private String password;
    private String newPass;


}
