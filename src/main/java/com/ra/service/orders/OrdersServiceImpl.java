package com.ra.service.orders;

import com.ra.model.dto.request.OrdersRequestDTO;
import com.ra.model.dto.request.UserRequestDTO;
import com.ra.model.dto.response.OrdersResponseDTO;
import com.ra.model.entity.CartItem;
import com.ra.model.entity.Orders;
import com.ra.model.entity.User;
import com.ra.repository.OrdersRepository;
import com.ra.service.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrdersServiceImpl implements OrdersService {
    @Autowired
    private OrdersRepository ordersRepository;

    @Override
    public List<OrdersResponseDTO> findAll() {
        List<Orders> ordersList = ordersRepository.findAll();
        return ordersList.stream().map(OrdersResponseDTO::new).toList();
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public OrdersResponseDTO findById(Long id) {
        Optional<Orders> ordersOptional = ordersRepository.findById(id);
        return ordersOptional.map(OrdersResponseDTO::new).orElse(null);
    }

    @Override
    public OrdersResponseDTO save(OrdersRequestDTO ordersRequestDTO) {
        Orders orders = new Orders();
        orders.setId(ordersRequestDTO.getId());
        orders.setAddress(ordersRequestDTO.getAddress());
        orders.setPhone(ordersRequestDTO.getPhone());
//        orders.setNote(ordersRequestDTO.getNote());
        orders.setTotal(orders.getTotal());
        orders.setStatus(orders.getStatus());
        orders.setCreateAt(orders.getCreateAt());
        orders.setUser(orders.getUser());
        orders.setOrderDetails(orders.getOrderDetails());
        orders = ordersRepository.save(orders);
        OrdersResponseDTO ordersResponseDTO = new OrdersResponseDTO();
        ordersResponseDTO.setStatus(orders.getStatus());
        return ordersResponseDTO;
    }

    @Override
    public Page<OrdersResponseDTO> searchOrdersById(Pageable pageable, Integer id) {
        return null;
    }

    @Override
    public Page<OrdersResponseDTO> getAll(Pageable pageable) {
        Page<Orders> ordersPage = ordersRepository.findAll(pageable);
        return ordersPage.map(OrdersResponseDTO::new);
    }

    @Override
    public void changeStatus(Long id, int status) {
        Orders orders = ordersRepository.findById(id).orElse(null);
        orders.setStatus(status);
        ordersRepository.save(orders);
    }


    @Override
    public List<Orders> getListOrderByStatus(int status) {
        return ordersRepository.findAllByStatus(status);
    }
}
