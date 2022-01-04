package pl.storedemo.services;

import pl.storedemo.entities.DelivererImpl;
import pl.storedemo.entities.OrderCart;

import java.util.List;

public interface DeliversService {

    void addNewDeliverer(DelivererImpl deliverer);
    void searchFreeDeliverer(OrderCart order, String city);
    List<DelivererImpl> getAllDeliverers();
    DelivererImpl getDelivererByOrderId(Long orderId);

}
