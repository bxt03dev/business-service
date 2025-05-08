package com.buixuantruong.shopapp.repository;

import com.buixuantruong.shopapp.dto.response.OrderResponse;
import com.buixuantruong.shopapp.model.Order;
import com.buixuantruong.shopapp.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);

    Page<Order> findAll(Pageable pageable);

//    @Query("SELECT o FROM Order o WHERE o.active = true AND (:keyword IS NULL OR :keyword = '' OR " +
//            "o.fullName LIKE %:keyword% " +
//            "OR o.address LIKE %:keyword% " +
//            "OR o.note LIKE %:keyword% " +
//            "OR o.email LIKE %:keyword%)")
//    Page<Order> findByKeyword(Pageable pageable);
}
