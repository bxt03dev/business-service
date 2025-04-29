package com.buixuantruong.shopapp.service.impl;

import com.buixuantruong.shopapp.dto.response.ApiResponse;
import com.buixuantruong.shopapp.dto.OrderDTO;
import com.buixuantruong.shopapp.exception.DataNotFoundException;
import com.buixuantruong.shopapp.exception.StatusCode;
import com.buixuantruong.shopapp.mapper.OrderMapper;
import com.buixuantruong.shopapp.model.Order;
import com.buixuantruong.shopapp.utils.fiels.OrderStatusField;
import com.buixuantruong.shopapp.model.User;
import com.buixuantruong.shopapp.repository.OrderRepository;
import com.buixuantruong.shopapp.repository.UserRepository;
import com.buixuantruong.shopapp.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {

    UserRepository userRepository;
    OrderRepository orderRepository;
    OrderMapper orderMapper;

    @Override
    public ApiResponse<Object> createOrder(OrderDTO orderDTO) throws DataNotFoundException {
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = orderMapper.toOrder(orderDTO);
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatusField.PENDING);

        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();
        if(shippingDate.isBefore(LocalDate.now())) {
            throw new DataNotFoundException("Shipping date cannot be null or in the past");
        }

        order.setShippingDate(shippingDate);
        order.setActive(true);
        orderRepository.save(order);

        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(StatusCode.SUCCESS.getMessage())
                .result(order)
                .build();
    }


    @Override
    public ApiResponse<Object> getOrderById(Long id) throws DataNotFoundException {
        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(StatusCode.SUCCESS.getMessage())
                .result(orderRepository.findById(id)
                        .orElseThrow(() -> new DataNotFoundException("Order not found")))
                .build();
    }

    @Override
    @Transactional
    public ApiResponse<Object> updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Order not found"));
        User existingUser = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        order = orderMapper.toOrder(orderDTO);
        order.setUser(existingUser);
        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(StatusCode.SUCCESS.getMessage())
                .result(orderRepository.save(order))
                .build();
    }

    @Override
    @Transactional
    public ApiResponse<Object> deleteOrder(Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if(optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setActive(false);
            orderRepository.save(order);
            return ApiResponse.builder()
                    .code(StatusCode.SUCCESS.getCode())
                    .message(StatusCode.SUCCESS.getMessage())
                    .result("Order deleted successfully")
                    .build();
        }
        return null;
    }

    @Override
    public ApiResponse<Object> getOrderByUserId(Long userId) {
        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(StatusCode.SUCCESS.getMessage())
                .result(orderRepository.findByUserId(userId))
                .build();
    }
}
