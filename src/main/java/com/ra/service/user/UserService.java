package com.ra.service.user;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.UserRequestDTO;
import com.ra.model.dto.response.UserLoginResponseDTO;
import com.ra.model.dto.response.UserResponseDTO;
import com.ra.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    List<UserResponseDTO> findAll();
    User register(User user);
    UserLoginResponseDTO login(UserRequestDTO userRequestDTO);
    Page<UserResponseDTO> getAll(Pageable pageable);
    Page<UserResponseDTO> searchByName(Pageable pageable,String name);
    void changeStatus(Long id);
    void changeRole(Long id) throws CustomException;
    UserResponseDTO findById(Long id);
    UserResponseDTO addNewRole(UserRequestDTO userRequestDTO) throws CustomException;
//    UserResponseDTO deleteRole(UserRequestDTO userRequestDTO) throws CustomException;
}
