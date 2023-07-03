package com.afulvio.booklify.service;

import com.afulvio.booklify.model.Order;
import com.afulvio.booklify.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private OrderRepository orderRepository;

    public List<Order> getAllOrderByEmail(String username) {
        return orderRepository.findAllByEmail(username);
    }

    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
}
