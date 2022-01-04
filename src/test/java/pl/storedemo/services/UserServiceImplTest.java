package pl.storedemo.services;

import pl.storedemo.entities.*;
import pl.storedemo.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ComponentScan("pl.storedemo.*")
@SpringBootTest(classes = UserServiceImpl.class)
@ExtendWith(SpringExtension.class)
class UserServiceImplTest {

    @MockBean
    CartItemRepository cartItemRepository;

    @MockBean
    ProductRepository productRepository;

    @MockBean
    OrderRepository orderRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    ShopRepository shopRepository;

    @MockBean
    DelivererRepository delivererRepository;

    @Autowired
    UserServiceImpl service;

    User user;
    List<User> userList;
    List<OrderCart> orderList;

    @BeforeEach
    void init(){
        orderList = new ArrayList<>();
        user = new User(1L, "john", "john@gmail.com", "password", "Krucza 2", "Cracow", orderList);
        userList = new ArrayList<>();
        userList.add(user);
    }

    @Test
    void createUser() {
        service.createUser(user);
        Mockito.verify(userRepository).save(user);
    }

    @Test
    void getAlUsers() {
        Mockito.when(userRepository.findAll()).thenReturn(userList);
        List<User> result = service.getAlUsers();
        assertEquals(1, result.size());
    }

    @Test
    void getUserByUserName() {
        Mockito.when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        User result = service.getUserByUserName("john");
        assertEquals(1L, result.getId());
        assertEquals("john@gmail.com", result.getEmail());
    }

    @Test
    void getlUserOrders() {
        Mockito.when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        List<OrderCart> result = service.getlUserOrders("john");
        assertTrue(result.isEmpty());
    }
}