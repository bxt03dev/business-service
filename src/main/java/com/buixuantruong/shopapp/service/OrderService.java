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
    Order getOrderById(Long id);
    Order updateOrder(Long id, OrderDTO orderDTO);
    Void deleteOrder(Long id);
    List<Order> getAllOrders(Long userId);
}
