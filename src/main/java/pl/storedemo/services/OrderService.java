package pl.storedemo.services;

import org.springframework.stereotype.Service;
import pl.storedemo.entities.OrderCart;

import java.util.List;

@Service
public interface OrderService {

    void createNewOrder(OrderCart order);
    List<OrderCart> getAllOrders();
    OrderCart getOrderById(Long id);
    String getCityFromOrder(OrderCart order);
}
