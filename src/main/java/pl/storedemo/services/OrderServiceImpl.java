package pl.storedemo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.storedemo.entities.CartItem;
import pl.storedemo.entities.OrderCart;
import pl.storedemo.repositories.OrderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public void createNewOrder(OrderCart order) {
        orderRepository.save(order);
    }

    @Override
    public List<OrderCart> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public OrderCart getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public String getCityFromOrder(OrderCart order) {
        List<String> city = new ArrayList<>();
        for (CartItem cartItem : order.getCartItems()){
            String city1 = cartItem.getProduct().getShop().getCity();
            city.add(city1);
        }
        return city.get(0);
    }
}
