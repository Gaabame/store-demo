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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ComponentScan("pl.storedemo.*")
@SpringBootTest(classes = OrderServiceImpl.class)
@ExtendWith(SpringExtension.class)
class OrderServiceImplTest {

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
    OrderServiceImpl service;

    OrderCart order;
    User user;
    CartItem cartItem;
    List<CartItem> cartItemList;
    List<Product> productList;
    List<OrderCart> orderList;
    Product product1;
    Shop shop;

    @BeforeEach
    void init() {
        productList = new ArrayList<>();
        product1 = Product.builder().name("water").description("sparkling").price(2).amountAvailable(10).build();
        productList.add(product1);
        shop = Shop.builder().id(1L).name("StoreForYou").city("Cracow").address("Starowiślna 8").productList(productList).latitude(50.049683).longitude(19.944544).build();
        product1.setShop(shop);
        cartItemList = new ArrayList<>();
        cartItem = new CartItem(1L, product1, 1);
        cartItemList.add(cartItem);
        orderList = new ArrayList<>();
        user = new User(1L, "john", "john@gmail.com", "password", "Krucza 2", "Cracow", orderList);

        order = new OrderCart(1L, cartItemList);
        order.setUser(user);
        orderList.add(order);
    }

    @Test
    void createNewOrder() {
        service.createNewOrder(order);
        Mockito.verify(orderRepository).save(order);
    }

    @Test
    void getAllOrders() {
        Mockito.when(orderRepository.findAll()).thenReturn(orderList);
        List<OrderCart> result = service.getAllOrders();
        assertEquals(1, result.size());
    }

    @Test
    void getOrderById() {
        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        OrderCart result = service.getOrderById(1L);
        assertEquals(order, result);
    }

    @Test
    void getCityFromOrder() {
        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        String result = service.getCityFromOrder(order);
        assertEquals("Cracow", result);
    }
}