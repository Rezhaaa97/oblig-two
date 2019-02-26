package no.oslomet.obligtwo.service;

import no.oslomet.obligtwo.model.Order;

import java.util.List;

public interface OrderService {

    Order save(Order order);
    List<Order> findAll();
    Order findById(long order_id);

}
