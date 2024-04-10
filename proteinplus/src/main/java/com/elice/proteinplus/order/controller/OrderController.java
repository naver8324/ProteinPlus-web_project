package com.elice.proteinplus.order.controller;

import com.elice.proteinplus.order.dto.DeliveryDto;
import com.elice.proteinplus.order.dto.OrderDto;
import com.elice.proteinplus.order.dto.OrderHistDto;
import com.elice.proteinplus.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;

    // 특정 사용자의 주문 목록을 조회합니다.
    @GetMapping("/user/{userId}/mypage/orderlist")
    public ResponseEntity<Page<OrderHistDto>> getUserOrders(@PathVariable Long userId, Pageable pageable) {
        Page<OrderHistDto> orders = orderService.getOrders(userId, pageable);
        return ResponseEntity.ok(orders);
    }

    // 특정 사용자의 취소된 주문 목록을 조회합니다.
    @GetMapping("/user/{userId}/mypage/cancellist")
    public ResponseEntity<Page<OrderHistDto>> getCancelledUserOrders(@PathVariable Long userId, Pageable pageable) {
        Page<OrderHistDto> cancelledOrders = orderService.getCancelledOrders(userId, pageable);
        return ResponseEntity.ok(cancelledOrders);
    }

    // 주문을 생성합니다.
//    @PostMapping("/order/order")
//    public ResponseEntity<Long> addOrder(@RequestBody OrderDto orderDto,
//                                           @RequestBody DeliveryDto deliveryDto,
//                                           @PathVariable Long userId) {
//        Long orderId = orderService.order(orderDto, deliveryDto, userId);
//        return ResponseEntity.status(HttpStatus.CREATED).body(orderId);
//    }

    // 주문의 배송 정보를 업데이트합니다.
    @PutMapping("/user/{userId}/mypage/orderlist/{orderId}/edit")
    public ResponseEntity<Void> updateDeliveryInfo(@PathVariable Long orderId,
                                                   @RequestBody DeliveryDto deliveryDto) {
        boolean updated = orderService.updateDeliveryInfo(orderId, deliveryDto);
        if (updated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 주문을 취소합니다.
    @PostMapping("/user/{userId}/mypage/orderlist/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        boolean cancelled = orderService.cancelOrder(orderId);
        if (cancelled) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}