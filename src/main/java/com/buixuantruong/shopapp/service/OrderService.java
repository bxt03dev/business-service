package com.buixuantruong.shopapp.service;

import com.buixuantruong.shopapp.dto.ApiResponse;
import com.buixuantruong.shopapp.dto.OrderDTO;
import com.buixuantruong.shopapp.exception.DataNotFoundException;
import com.buixuantruong.shopapp.model.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    ApiResponse<Object> createOrder(OrderDTO orderDTO) throws DataNotFoundException;
    ApiResponse<Object> getOrderById(Long id) throws DataNotFoundException;
    ApiResponse<Object> updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException;
    ApiResponse<Object> deleteOrder(Long id);
    ApiResponse<Object> getOrderByUserId(Long userId);
}
