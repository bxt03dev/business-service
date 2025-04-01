package com.buixuantruong.shopapp.models;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderStatus {
    static String PENDING = "pending";
    static String PROCESSING = "processing";
    static String SHIPPED = "shipped";
    static String DELIVERD = "delivered";
    static String CANCELLED = "cancelled";
}
