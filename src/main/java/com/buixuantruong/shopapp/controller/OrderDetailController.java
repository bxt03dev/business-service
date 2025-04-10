package com.buixuantruong.shopapp.controller;

import com.buixuantruong.shopapp.dto.ApiResponse;
import com.buixuantruong.shopapp.dto.OrderDetailDTO;
import com.buixuantruong.shopapp.service.OrderDetailService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order_details")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class OrderDetailController {

    OrderDetailService orderDetailService;

    @GetMapping("")
    public ApiResponse<Object> getOrderDetails(@Valid @PathVariable("id") Long id) {
        return orderDetailService.getOrderDetailById(id);
    }

    @PostMapping("")
    public ApiResponse<Object> addOrderDetail(@RequestBody @Valid OrderDetailDTO dto) {
        return orderDetailService.createOrderDetail(dto);
    }

    @GetMapping("/{id}")
    public ApiResponse<Object> getOrderDetail(@PathVariable @Valid Long id) {
        return orderDetailService.getOrderDetailById(id);
    }

    @GetMapping("/order/{orderId}")
    public ApiResponse<Object> getOrderDetailsByOrderId(@PathVariable @Valid Long orderId) {
        return orderDetailService.getOrderDetailByOrderId(orderId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO,
                                               @PathVariable @Valid Long id) {
        return ResponseEntity.ok("update detail with id " + id + " , newOrderDetailData: " + orderDetailDTO);
    }

    @PutMapping("/order_details/{id}")
    public ResponseEntity<?> deleteOrderDetail(@PathVariable @Valid Long id) {
        return ResponseEntity.noContent().build();
    }
}
