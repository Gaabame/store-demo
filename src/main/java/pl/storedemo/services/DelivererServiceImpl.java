package pl.storedemo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.storedemo.entities.DelivererImpl;
import pl.storedemo.entities.OrderCart;
import pl.storedemo.repositories.DelivererRepository;

import java.util.List;
import java.util.NoSuchElementException;


@Service
public class DelivererServiceImpl implements DeliversService {

    @Autowired
    private DelivererRepository delivererRepository;


    @Override
    public List<DelivererImpl> getAllDeliverers() {
        return delivererRepository.findAll();
    }

    @Override
    public void searchFreeDeliverer(OrderCart order, String city) {
        DelivererImpl deliverer1 = getAllDeliverers().stream()
                .filter(deliverer -> deliverer.getCity().equals(city))
                .filter(deliverer -> !deliverer.isBusy())
                .findFirst().orElseThrow(NoSuchElementException::new);
        deliverer1.hasNewOrder(order);
        deliverer1.setOrderId(order.getId());
        delivererRepository.save(deliverer1);
    }

    @Override
    public DelivererImpl getDelivererByOrderId(Long orderId) {
        return delivererRepository.findAll().stream()
                .filter(deliverer -> deliverer.getOrderId().equals(orderId))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public void addNewDeliverer(DelivererImpl deliverer) {
        delivererRepository.save(deliverer);
    }
}
