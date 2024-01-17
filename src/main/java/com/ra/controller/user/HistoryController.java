package com.ra.controller.user;

import com.ra.exception.CustomException;
import com.ra.model.dto.response.HistoryResponseDTO;
import com.ra.model.dto.response.OrdersResponseDTO;
import com.ra.model.entity.User;
import com.ra.repository.UserRepository;
import com.ra.security.user_principle.UserDetailService;
import com.ra.service.orders.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/user")
public class HistoryController {
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/history")
    public ResponseEntity<?> getHistory(Authentication authentication){
        Long userId=userDetailService.getUserIdFromAuthentication(authentication);
        User user=userRepository.findById(userId).orElse(null);
        if (user!=null){
        List<OrdersResponseDTO> list=ordersService.getListOrderByUser(user);
        return new ResponseEntity<>(list, HttpStatus.OK);
        }
        return new ResponseEntity<>("Not found",HttpStatus.OK);
    }

    @PutMapping("/history/{id}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long id) throws CustomException {
        try{
            ordersService.cancelOrder(id);
        return new ResponseEntity<>("You canceled order",HttpStatus.OK);
        }catch (NumberFormatException e){
        return new ResponseEntity<>("Insert number is invalid !!!",HttpStatus.BAD_REQUEST);
        }
    }
}
