package com.ra.model.dto.response;

import com.ra.model.entity.OrderDetail;
import com.ra.model.entity.Orders;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class OrdersResponseDTO {
    private Long id;
    private String address;
    private String phone;
//    private String note;
    private float total;
    private int status=1;
    private Date createAt;
    private Long userId;
    private List<OrderDetailResponseDTO> orders;

    public OrdersResponseDTO(Orders orders) {
        this.id = orders.getId();
        this.address = orders.getAddress();
        this.phone = orders.getPhone();
//        this.note = orders.getNote();
        this.total = orders.getTotal();
        this.status = orders.getStatus();
        this.createAt = orders.getCreateAt();
        this.userId = orders.getUser().getId();
        this.orders = orders.getOrderDetails().stream().map((OrderDetailResponseDTO::new)).toList();
    }
}
