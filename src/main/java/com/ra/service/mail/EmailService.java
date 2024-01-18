package com.ra.service.mail;

import com.ra.model.dto.response.OrdersResponseDTO;
import com.ra.model.entity.Orders;
import com.ra.model.entity.User;

public interface EmailService {
    String sendMail( String email, Orders orders);
    String sendAcceptMail(Orders orders);
    String sendDenyMail(Orders orders);
}
