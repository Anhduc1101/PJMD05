package com.ra.service.orders;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.OrdersRequestDTO;
import com.ra.model.dto.response.HistoryResponseDTO;
import com.ra.model.dto.response.OrdersResponseDTO;
import com.ra.model.entity.Orders;
import com.ra.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface OrdersService {
    List<OrdersResponseDTO> findAll();
    List<OrdersResponseDTO> getListOrderByUser(User user);
//    List<HistoryResponseDTO> findHistory();
    OrdersResponseDTO findById(Long id);
    Page<OrdersResponseDTO> getAll(Pageable pageable);
    Orders changeStatus(Long id, int status) throws CustomException;
    List<Orders> getListOrderByStatus(int status);
    List<HistoryResponseDTO> getOrders();
    void cancelOrder(Long id) throws CustomException;

    Orders placeOrder(User user);
//    OrdersResponseDTO findOrderById(Long id);
    //    void delete(Long id);
    //    OrdersResponseDTO save(OrdersRequestDTO ordersRequestDTO);
}
