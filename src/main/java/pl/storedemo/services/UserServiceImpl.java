package pl.storedemo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.storedemo.entities.OrderCart;
import pl.storedemo.entities.User;
import pl.storedemo.repositories.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void createUser(User user) {
        userRepository.save(user);
    }

    @Override
    public List<User> getAlUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByUserName(String username) {
        return userRepository.findByUsername(username).orElseThrow((NoSuchElementException::new));
    }

    @Override
    public List<OrderCart> getlUserOrders(String username) {
        return getUserByUserName(username).getOrders();
    }

}

