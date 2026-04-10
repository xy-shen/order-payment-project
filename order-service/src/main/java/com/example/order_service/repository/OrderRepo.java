package com.example.order_service.repository;

import com.example.order_service.entity.Order;
import com.example.order_service.util.OrderStatus;
import java.util.List;
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

    public Order findById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        Order order = mongoTemplate.findOne(query, Order.class);

        if (order == null) {
            throw new RuntimeException("Order not found");
        }
        return order;
    }

    public List<Order> findAll() {
        return mongoTemplate.findAll(Order.class);
    }

    public Order updateById(String id, OrderStatus status) {
        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update().set("status", status);

        return mongoTemplate.findAndModify(
            query,
            update,
            FindAndModifyOptions.options().returnNew(true),
            Order.class
        );
    }
}
