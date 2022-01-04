package pl.storedemo.services;

import org.springframework.stereotype.Service;
import pl.storedemo.entities.OrderCart;
import pl.storedemo.entities.User;

import java.util.List;

@Service
public interface UserService {

    void createUser(User user);
    List<User> getAlUsers();
    User getUserByUserName(String username);
    List<OrderCart> getlUserOrders(String username);
}
