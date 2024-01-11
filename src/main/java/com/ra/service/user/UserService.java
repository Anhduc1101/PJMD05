package com.ra.service.user;

import com.ra.model.dto.request.UserRequestDTO;
import com.ra.model.dto.response.UserResponseDTO;
import com.ra.model.entity.User;

public interface UserService {
    User register(User user);
    UserResponseDTO login(UserRequestDTO userRequestDTO);
}
