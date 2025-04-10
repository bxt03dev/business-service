package com.buixuantruong.shopapp.controller;

import com.buixuantruong.shopapp.dto.response.ApiResponse;
import com.buixuantruong.shopapp.dto.OrderDTO;
import com.buixuantruong.shopapp.exception.DataNotFoundException;
import com.buixuantruong.shopapp.service.OrderService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class OrderController {

    OrderService orderService;

    @GetMapping("")
    public ResponseEntity<String> getOrders() {
        return ResponseEntity.ok("Orders List");
    }

    @PostMapping("")
    public ApiResponse<Object> addOrder(@RequestBody @Valid OrderDTO orderDTO,
                                BindingResult bindingResult) {

        try{
            if(bindingResult.hasErrors()){
                List<String> errorMessage = bindingResult.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ApiResponse.builder()
                        .message(String.join(", ", errorMessage))
                        .build();
            }
            return ApiResponse.builder()
                    .result(orderService.createOrder(orderDTO))
                    .build();
        }
        catch(Exception e){
            return ApiResponse.builder()
                    .message(e.getMessage())
                    .build();
        }
    }

    @GetMapping("/user/{user_id}")
    public ApiResponse<Object> getOrdersByUserId(@PathVariable("user_id") Long userId) {
        return orderService.getOrderByUserId(userId);
    }

    @GetMapping("/{id}")
    public ApiResponse<Object> getOrderById(@PathVariable("id") Long orderId) throws DataNotFoundException {
        return orderService.getOrderById(orderId);
    }

    @PutMapping("{id}")
    public ApiResponse<Object> updateOrder(@PathVariable("id") @Valid Long id, @RequestBody @Valid OrderDTO order) throws DataNotFoundException {
        return orderService.updateOrder(id, order);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Object> deleteOrder(@PathVariable("id") Long id) {
        return orderService.deleteOrder(id);
    }
}
