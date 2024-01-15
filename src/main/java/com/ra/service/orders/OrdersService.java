package com.ra.service.orders;

import com.ra.model.dto.request.OrdersRequestDTO;
import com.ra.model.dto.response.OrdersResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrdersService {
    List<OrdersResponseDTO> findAll();
    void delete(Long id);
    OrdersResponseDTO findById(Long id);

    OrdersResponseDTO save(OrdersRequestDTO ordersRequestDTO);
    Page<OrdersResponseDTO> searchOrdersById(Pageable pageable, Integer id);
    Page<OrdersResponseDTO> getAll(Pageable pageable);
}
