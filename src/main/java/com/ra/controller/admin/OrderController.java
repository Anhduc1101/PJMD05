package com.ra.controller.admin;

import com.ra.exception.CustomException;
import com.ra.model.dto.response.OrdersResponseDTO;
import com.ra.model.entity.Orders;
import com.ra.model.entity.User;
import com.ra.repository.OrdersRepository;
import com.ra.repository.UserRepository;
import com.ra.service.mail.EmailService;
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
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;

    //    danh sach tat ca don hang
    @GetMapping("/orders")
    public ResponseEntity<?> getAllOrders() {
        List<OrdersResponseDTO> ordersResponseDTOList = ordersService.findAll();
        return new ResponseEntity<>(ordersResponseDTOList, HttpStatus.OK);
    }

//    lay don hang theo id
//    @GetMapping("/orders/{id}")
//    public ResponseEntity<?> getOrderById(@PathVariable("id") Long id){
//        OrdersResponseDTO ordersResponseDTO=ordersService.findById(id);
//        if (ordersResponseDTO!=null){
//            return new ResponseEntity<>(ordersResponseDTO,HttpStatus.OK);
//        }
//        return new ResponseEntity<>("Not found",HttpStatus.NOT_FOUND);
//    }

    //    cap nhat trang thai don hang
    @PatchMapping("/orders/{id}")
    public ResponseEntity<?> changeOrderStatus(@PathVariable("id") Long id, @RequestParam(name = "status") int status) throws CustomException {
        try {
            if (status < 0 || status > 2) {
                throw new CustomException("Invalid order status");
            }
            if (status==2){
               Orders orders= ordersService.changeStatus(id, status);
               emailService.sendAcceptMail(orders);
            }
            if (status==0){
                Orders orders= ordersService.changeStatus(id, status);
                emailService.sendDenyMail(orders);
            }
            return new ResponseEntity<>("Status had been changed !!!", HttpStatus.OK);


        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Please insert valid number !!!", HttpStatus.BAD_REQUEST);
        }
    }

    //    lay danh sach don hang theo trang thai
    @GetMapping("/orders/orderStatus")
    public ResponseEntity<?> getListOrdersByStatus(@RequestParam int status) {
        List<Orders> ordersList = ordersService.getListOrderByStatus(status);
        return new ResponseEntity<>(ordersList, HttpStatus.OK);
    }

}
