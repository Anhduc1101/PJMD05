package com.ra.service.mail;

import com.ra.model.dto.response.OrdersResponseDTO;

public interface EmailService {
    String sendMail( String email, OrdersResponseDTO ordersResponseDTO);
}
