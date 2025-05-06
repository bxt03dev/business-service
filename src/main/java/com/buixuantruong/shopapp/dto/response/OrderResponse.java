package com.buixuantruong.shopapp.dto.response;


import com.buixuantruong.shopapp.model.Order;
import com.buixuantruong.shopapp.model.OrderDetail;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    Long id;
    Long userId;
    String fullName;
    String phoneNumber;
    String email;
    String address;
    String note;
    Date orderDate;
    String status;
    double totalMoney;
    String shippingMethod;
    String shippingAddress;
    LocalDate shippingDate;
    String paymentMethod;
    List<OrderDetail> orderDetails;

    public static OrderResponse fromOrder(Order order) {
        OrderResponse orderResponse = OrderResponse
                .builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .fullName(order.getFullName())
                .phoneNumber(order.getPhoneNumber())
                .email(order.getEmail())
                .address(order.getAddress())
                .note(order.getNote())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .totalMoney(order.getTotalMoney())
                .shippingMethod(order.getShippingMethod())
                .shippingAddress(order.getShippingAddress())
                .shippingDate(order.getShippingDate())
                .paymentMethod(order.getPaymentMethod())
                .orderDetails(order.getOrderDetails())
                .build();
        return orderResponse;
    }
}
