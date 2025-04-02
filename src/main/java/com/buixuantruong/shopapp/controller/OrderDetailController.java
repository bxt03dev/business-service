package com.buixuantruong.shopapp.controller;

import com.buixuantruong.shopapp.dto.OrderDetailDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order_details")
public class OrderDetailController {
    @GetMapping("")
    public ResponseEntity<String> getOrderDetails() {
        return ResponseEntity.ok("Order details");
    }

    @PostMapping("")
    public ResponseEntity<?> addOrderDetail(@RequestBody @Valid OrderDetailDTO dto) {
        return ResponseEntity.ok("Order details");
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getOrderDetail(@PathVariable @Valid Long id) {
        return ResponseEntity.ok("Order details " + id);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetailsByOrderId(@PathVariable @Valid Long orderId) {

        return ResponseEntity.ok("get order detail with id " + orderId);
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
