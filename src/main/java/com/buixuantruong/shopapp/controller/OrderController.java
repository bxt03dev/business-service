package com.buixuantruong.shopapp.controller;

import com.buixuantruong.shopapp.dtos.OrderDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @GetMapping("")
    public ResponseEntity<String> getOrders() {
        return ResponseEntity.ok("Orders List");
    }

    @PostMapping("")
    public ResponseEntity<?> addOrder(@RequestBody @Valid OrderDTO order,
                                           BindingResult bindingResult) {

        try{
            if(bindingResult.hasErrors()){
                List<String> errorMessage = bindingResult.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            return ResponseEntity.ok("Order added");
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<?> getOrderById(@PathVariable("user_id") long user_id) {
        try{
            return ResponseEntity.ok("get order information");
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateOrder(@PathVariable("id") @Valid long id, @RequestBody @Valid OrderDTO order) {
        return ResponseEntity.ok("Order updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable("id") long id) {
        return ResponseEntity.ok("Order deleted");
    }
}
