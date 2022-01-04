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
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ComponentScan("pl.storedemo.*")
@SpringBootTest(classes = DelivererServiceImpl.class)
@ExtendWith(SpringExtension.class)
class DelivererServiceImplTest {

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
    DelivererServiceImpl service;

    DelivererImpl deliverer1;
    DelivererImpl deliverer2;
    DelivererImpl deliverer3;
    List<DelivererImpl> delivererList;
    OrderCart order;
    List<CartItem> cartItemList;

    @BeforeEach
    void init(){
        cartItemList = new ArrayList<>();
        order = OrderCart.builder().id(1L).cartItems(cartItemList).build();
        deliverer1 = new DelivererImpl(1L, "Matt", "Cracow");
        deliverer2 = new DelivererImpl(2L, "Zoey", "Warsaw");
        deliverer3 = new DelivererImpl(3L, "Sam", "Cracow", true, 1L);
        delivererList = Arrays.asList(deliverer1, deliverer2, deliverer3);
        order.setDelivererId(deliverer3.getDelivererId());
        orderRepository.save(order);
        deliverer3.setOrderId(1L);
    }

    @Test
    void getAllDeliverers() {
        Mockito.when(delivererRepository.findAll()).thenReturn(delivererList);
        List<DelivererImpl> allDeliverers = service.getAllDeliverers();
        assertEquals(3, allDeliverers.size());
        assertEquals(delivererList, allDeliverers);
    }

    @Test
    void searchFreeDeliverer() {
        Mockito.when(delivererRepository.findAll()).thenReturn(delivererList);
        service.searchFreeDeliverer(order, "Cracow");
        assertTrue(deliverer1.isBusy());
        assertFalse(deliverer2.isBusy());
    }

//    @Test
//    void getDelivererByOrderId() {
//        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
//        DelivererImpl result = service.getDelivererByOrderId(1L);
//        assertEquals("Sam", result.getName());
//    }

    @Test
    void addNewDeliverer() {
        service.addNewDeliverer(deliverer1);
        Mockito.verify(delivererRepository).save(deliverer1);
    }
}