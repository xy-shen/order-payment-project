package com.example.order_service.repository;

import com.example.order_service.entity.Order;
import com.example.order_service.util.OrderStatus;
import java.time.LocalDateTime;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.*;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepo {
    private final MongoTemplate mongoTemplate;

    public Order create(Order order) {
        return mongoTemplate.insert(order);
    }

    public Optional<Order> findById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        return Optional.ofNullable(mongoTemplate.findOne(query, Order.class));
    }

    public List<Order> findAll() {
        return mongoTemplate.findAll(Order.class);
    }

    public Order updateById(String id, OrderStatus status) {
        Query query = new Query(Criteria.where("_id").is(id));

        Update update = new Update()
            .set("status", status)
            .set("updatedAt", LocalDateTime.now()); // 🔥 fix

        return mongoTemplate.findAndModify(
            query,
            update,
            FindAndModifyOptions.options().returnNew(true),
            Order.class
        );
    }
}
