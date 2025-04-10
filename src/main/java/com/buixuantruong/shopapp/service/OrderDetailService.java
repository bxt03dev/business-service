package com.buixuantruong.shopapp.service;

import com.buixuantruong.shopapp.dto.ApiResponse;
import com.buixuantruong.shopapp.dto.OrderDetailDTO;
import org.springframework.stereotype.Service;

@Service
public interface OrderDetailService {
    ApiResponse<Object> createOrderDetail(OrderDetailDTO orderDetailDTO);
    ApiResponse<Object> getOrderDetailById(Long id);
    ApiResponse<Object> updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO);
    ApiResponse<Object> deleteOrderDetail(Long id);
    ApiResponse<Object> getOrderDetailByOrderId(Long orderId);
}
