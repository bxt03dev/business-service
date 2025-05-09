package com.buixuantruong.shopapp.service.impl;

import com.buixuantruong.shopapp.dto.CartItemDTO;
import com.buixuantruong.shopapp.dto.OrderDetailDTO;
import com.buixuantruong.shopapp.dto.response.ApiResponse;
import com.buixuantruong.shopapp.dto.OrderDTO;
import com.buixuantruong.shopapp.dto.response.OrderResponse;
import com.buixuantruong.shopapp.dto.response.ProductResponse;
import com.buixuantruong.shopapp.exception.DataNotFoundException;
import com.buixuantruong.shopapp.exception.InvalidParamException;
import com.buixuantruong.shopapp.exception.StatusCode;
import com.buixuantruong.shopapp.mapper.OrderMapper;
import com.buixuantruong.shopapp.model.Order;
import com.buixuantruong.shopapp.model.OrderDetail;
import com.buixuantruong.shopapp.model.Product;
import com.buixuantruong.shopapp.utils.fiels.OrderStatusField;
import com.buixuantruong.shopapp.model.User;
import com.buixuantruong.shopapp.repository.OrderDetailRepository;
import com.buixuantruong.shopapp.repository.OrderRepository;
import com.buixuantruong.shopapp.repository.ProductRepository;
import com.buixuantruong.shopapp.repository.UserRepository;
import com.buixuantruong.shopapp.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {

    UserRepository userRepository;
    OrderRepository orderRepository;
    OrderMapper orderMapper;
    ProductRepository productRepository;
    OrderDetailRepository orderDetailRepository;

    @Override
    @Transactional
    public ApiResponse<Object> createOrder(OrderDTO orderDTO) throws DataNotFoundException {
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if cartItems exist
        if (orderDTO.getCartItems() == null || orderDTO.getCartItems().isEmpty()) {
            throw new DataNotFoundException("Cart items cannot be empty");
        }
        
        // Verify product quantities before creating the order
        List<String> insufficientStockItems = new ArrayList<>();
        for (CartItemDTO cartItem : orderDTO.getCartItems()) {
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new DataNotFoundException("Product not found with id: " + cartItem.getProductId()));
            
            // Check if there's enough quantity in stock
            if (product.getQuantity() < cartItem.getQuantity()) {
                insufficientStockItems.add(String.format("Product '%s' has only %d items in stock, but %d were requested", 
                        product.getName(), product.getQuantity(), cartItem.getQuantity()));
            }
        }
        
        // If any product has insufficient stock, return error
        if (!insufficientStockItems.isEmpty()) {
            return ApiResponse.builder()
                    .code(StatusCode.INVALID_DATA.getCode())
                    .message("Insufficient stock for some products")
                    .result(insufficientStockItems)
                    .build();
        }

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
        Order savedOrder = orderRepository.save(order);
        
        // Create order details and update product quantities
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (CartItemDTO cartItem : orderDTO.getCartItems()) {
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new DataNotFoundException("Product not found with id: " + cartItem.getProductId()));
            
            OrderDetail orderDetail = OrderDetail.builder()
                    .order(savedOrder)
                    .product(product)
                    .numberOfProducts(cartItem.getQuantity())
                    .price(product.getPrice())
                    .totalMoney((long) (product.getPrice() * cartItem.getQuantity()))
                    .build();
            
            orderDetails.add(orderDetailRepository.save(orderDetail));
            
            // Update product quantity
            product.setQuantity(product.getQuantity() - cartItem.getQuantity());
            productRepository.save(product);
        }
        
        savedOrder.setOrderDetails(orderDetails);

        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(StatusCode.SUCCESS.getMessage())
                .result(savedOrder)
                .build();
    }


    @Override
    public Order getOrderById(Long id) throws DataNotFoundException {
        return orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Order not found"));
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

    @Override
    public Page<OrderResponse> getAllUserOrders(PageRequest pageRequest) {
        return orderRepository.findAll(pageRequest).map(OrderResponse::fromOrder);
    }

}
