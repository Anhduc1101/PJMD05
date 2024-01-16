package com.ra.controller.admin;

import com.ra.model.dto.request.OrdersRequestDTO;
import com.ra.model.dto.response.OrdersResponseDTO;
import com.ra.model.dto.response.ProductResponseDTO;
import com.ra.model.entity.Orders;
import com.ra.repository.OrdersRepository;
import com.ra.service.orders.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/admin")
public class OrderController {
    @Autowired
    private OrdersService ordersService;
    @GetMapping("/orders")
    public ResponseEntity<?> getAllOrders(){
        List<OrdersResponseDTO> ordersResponseDTOList=ordersService.findAll();
        return new ResponseEntity<>(ordersResponseDTOList, HttpStatus.OK);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable("id") Long id){
        OrdersResponseDTO ordersResponseDTO=ordersService.findById(id);
        if (ordersResponseDTO!=null){
            return new ResponseEntity<>(ordersResponseDTO,HttpStatus.OK);
        }
        return new ResponseEntity<>("Not found",HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/orders/{id}")
    public ResponseEntity<?> changeOrderStatus(@PathVariable("id") String id, @RequestParam int status) {
        try {
            Long idOrder= Long.valueOf(id);
            ordersService.changeStatus(idOrder,status);
            return new ResponseEntity<>("Status had been changed !!!",HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>("Please insert valid number !!!",HttpStatus.BAD_REQUEST);
        }
    }
}
