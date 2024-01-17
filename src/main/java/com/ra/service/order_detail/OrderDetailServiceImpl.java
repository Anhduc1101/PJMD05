package com.ra.service.order_detail;

import com.ra.model.dto.response.OrderDetailResponseDTO;
import com.ra.model.entity.OrderDetail;
import com.ra.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class OrderDetailServiceImpl implements OrderDetailService{
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Override
    public List<OrderDetailResponseDTO> getListOrderDetail() {
        List<OrderDetail> list=orderDetailRepository.findAll();
        return list.stream().map(OrderDetailResponseDTO::new).toList();
    }

    @Override
    public OrderDetailResponseDTO findById(Long id) {
    Optional<OrderDetail> orderDetailOptional=orderDetailRepository.findById(id);
        return orderDetailOptional.map(OrderDetailResponseDTO::new).orElse(null);
    }

}
