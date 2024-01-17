package com.ra.service.order_detail;

import com.ra.model.dto.response.OrderDetailResponseDTO;

import java.util.List;

public interface OrderDetailService {
    List<OrderDetailResponseDTO> getListOrderDetail();
    OrderDetailResponseDTO findById(Long id);
}
