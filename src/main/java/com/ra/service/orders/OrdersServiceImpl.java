package com.ra.service.orders;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.OrdersRequestDTO;
import com.ra.model.dto.request.UserRequestDTO;
import com.ra.model.dto.response.HistoryResponseDTO;
import com.ra.model.dto.response.OrdersResponseDTO;
import com.ra.model.entity.*;
import com.ra.repository.CartItemRepository;
import com.ra.repository.CartRepository;
import com.ra.repository.OrderDetailRepository;
import com.ra.repository.OrdersRepository;
import com.ra.service.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrdersServiceImpl implements OrdersService {
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public List<OrdersResponseDTO> findAll() {
        List<Orders> ordersList = ordersRepository.findAll();
        return ordersList.stream().map(OrdersResponseDTO::new).toList();
    }

    @Override
    public List<OrdersResponseDTO> getListOrderByUser(User user) {
        List<Orders> ordersList = ordersRepository.findByUser(user);
        return ordersList.stream().map(OrdersResponseDTO::new).toList();
    }

//    @Override
//    public List<HistoryResponseDTO> findHistory() {
//        List<Orders> ordersList = ordersRepository.findAll();
//        return ordersList.stream().map(HistoryResponseDTO::new).toList();
//    }


    @Override
    public OrdersResponseDTO findById(Long id) {
        Optional<Orders> ordersOptional = ordersRepository.findById(id);
        return ordersOptional.map(OrdersResponseDTO::new).orElse(null);
    }

//    @Override
//    public OrdersResponseDTO save(OrdersRequestDTO ordersRequestDTO) {
//        Orders orders = new Orders();
//        orders.setId(ordersRequestDTO.getId());
//        orders.setAddress(ordersRequestDTO.getAddress());
//        orders.setPhone(ordersRequestDTO.getPhone());
////        orders.setNote(ordersRequestDTO.getNote());
//        orders.setTotal(orders.getTotal());
//        orders.setStatus(orders.getStatus());
//        orders.setCreateAt(orders.getCreateAt());
//        orders.setUser(orders.getUser());
//        orders.setOrderDetails(orders.getOrderDetails());
//        orders = ordersRepository.save(orders);
//        OrdersResponseDTO ordersResponseDTO = new OrdersResponseDTO();
//        ordersResponseDTO.setStatus(orders.getStatus());
//        return ordersResponseDTO;
//    }


    @Override
    public Page<OrdersResponseDTO> getAll(Pageable pageable) {
        Page<Orders> ordersPage = ordersRepository.findAll(pageable);
        return ordersPage.map(OrdersResponseDTO::new);
    }

    @Override
    public Orders changeStatus(Long id, int status) throws CustomException {
        Orders orders = ordersRepository.findById(id).orElse(null);
        if (orders.getStatus() == 0) {
            throw new CustomException("Order had been cancel, status can not be change");
        }
        if (orders.getStatus() == 2) {
            throw new CustomException("Order had been confirm, status can not be change");
        }
        if (orders.getStatus() == 1) {
            orders.setStatus(status);
            ordersRepository.save(orders);
        }
        return orders;
    }


    @Override
    public List<Orders> getListOrderByStatus(int status) {
        return ordersRepository.findAllByStatus(status);
    }

    @Override
    public List<HistoryResponseDTO> getOrders() {
        List<Orders> dto = ordersRepository.findAll();
        return (List<HistoryResponseDTO>) dto.stream().map(HistoryResponseDTO::new);
    }

    @Override
    public void cancelOrder(Long id) throws CustomException {
        Orders orders = ordersRepository.findById(id).orElse(null);
        if (orders.getStatus() == 0) {
            throw new CustomException("Order had been cancel, status can not be change");
        }
        if (orders.getStatus() == 2) {
            throw new CustomException("Order had been confirm, status can not be change");
        }
        if (orders.getStatus() == 1) {
            orders.setStatus(0);
            ordersRepository.save(orders);
        }
    }


    @Override
    public Orders placeOrder(User user) {
        Cart cart = cartRepository.findByUser(user);
        List<CartItem> cartItemList = cartItemRepository.findAllByCart(cart);
        Orders orders = new Orders();
        orders.setUser(user);
        orders.setCreateAt(new Date());
        ordersRepository.save(orders);

        for (CartItem item : cartItemList) {
            OrderDetail od = new OrderDetail();
            od.setProduct(item.getProduct());
            od.setQuantity(item.getQuantity());
            od.setPrice(item.getPrice());
            od.setOrders(orders);
            orderDetailRepository.save(od);
        }
        orders.setAddress(user.getAddress());
        orders.setPhone(user.getPhone());
        float total = cartItemList.stream().map(cartItem -> cartItem.getPrice() * cartItem.getQuantity()).reduce(Float::sum).orElse(0f);
        orders.setTotal(total);
        ordersRepository.save(orders);
        return orders;
    }


//    @Override
//    public OrdersResponseDTO findOrderById(Long id) {
//        OrdersResponseDTO ordersResponseDTO=ordersRepository.findOrdersById(id);
//        return ordersResponseDTO;
//    }
}
