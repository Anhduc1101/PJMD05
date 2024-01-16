package com.ra.controller.admin;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.UserRequestDTO;
import com.ra.model.dto.response.UserResponseDTO;
import com.ra.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/admin")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/users")
    public ResponseEntity<?> getUsers(){
        List<UserResponseDTO> userResponseDTOS=userService.findAll();
        return new ResponseEntity<>(userResponseDTOS,HttpStatus.OK);
    }

    @GetMapping("/users/search")
    public ResponseEntity<Page<UserResponseDTO>> search(@RequestParam(name = "page", defaultValue = "0") int page,
                                                        @RequestParam(name = "limit", defaultValue = "5") int limit,
                                                        @RequestParam(name = "sort", defaultValue = "id") String sort,
                                                        @RequestParam(name = "order", defaultValue = "asc") String order,
                                                        @RequestParam(name = "search") String search) {
        Pageable pageable;
        if (order.equals("asc")) {
            pageable = PageRequest.of(page, limit, Sort.by(sort).ascending());
        } else {
            pageable = PageRequest.of(page, limit, Sort.by(sort).descending());
        }
        Page<UserResponseDTO> userResponseDTOS = userService.searchByName(pageable, search);
        return new ResponseEntity<>(userResponseDTOS, HttpStatus.OK);
    }

    @GetMapping("/users/sort+pagination")
    public ResponseEntity<?> getListUsers(@RequestParam(name = "page", defaultValue = "0") int page,
                                          @RequestParam(name = "limit", defaultValue = "5") int limit,
                                          @RequestParam(name = "sort", defaultValue = "id") String sort,
                                          @RequestParam(name = "order", defaultValue = "asc") String order) {
        Pageable pageable;
        if (order.equals("asc")) {
            pageable = PageRequest.of(page, limit, Sort.by(sort).ascending());
        } else {
            pageable = PageRequest.of(page, limit, Sort.by(sort).descending());
        }
        Page<UserResponseDTO> userResponseDTOS = userService.getAll(pageable);
        return new ResponseEntity<>(userResponseDTOS, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") Long id){
        UserResponseDTO userResponseDTO=userService.findById(id);
        if (userResponseDTO!=null){
            return new ResponseEntity<>(userResponseDTO,HttpStatus.OK);
        }
        return new ResponseEntity<>("Not Found",HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/users/{id}/change-status")
    public ResponseEntity<?> blockOrUnblock(@PathVariable("id") Long id){
        userService.changeStatus(id);
        UserResponseDTO userResponseDTO=userService.findById(id);
        return new ResponseEntity<>(userResponseDTO,HttpStatus.OK);
    }

    @PatchMapping("/users/{id}/change-role")
    public ResponseEntity<?> changeRole(@PathVariable("id") Long id) throws CustomException {
        userService.changeRole(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/users/{id}/add-role")
    public ResponseEntity<?> addNewRole(@PathVariable("id")Long id, @ModelAttribute UserRequestDTO userRequestDTO) throws CustomException {
        UserResponseDTO userResponseDTO=userService.findById(id);
        System.out.println(userResponseDTO);
        if (userResponseDTO!=null){
            userService.addNewRole(userRequestDTO, userRequestDTO.getId());
            return new ResponseEntity<>(userResponseDTO,HttpStatus.OK);
        }
        return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
    }

}
