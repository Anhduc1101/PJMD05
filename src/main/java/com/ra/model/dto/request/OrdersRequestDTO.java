package com.ra.model.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ra.model.entity.OrderDetail;
import com.ra.model.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.util.Set;

public class OrdersRequestDTO {
    private Long id;
    private String address;
    private String phone;
    private String note;
    private float total;

    private int status=1;


    private Long userId;

    private Set<OrderDetail> orderDetails;
}
