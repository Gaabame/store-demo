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
@SpringBootTest(classes = ShopServiceImpl.class)
@ExtendWith(SpringExtension.class)
class ShopServiceImplTest {

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
    ShopServiceImpl service;

    List<Product> productList;
    Product product1;
    Shop shop1;
    List<Shop> shopList;

    @BeforeEach
    void init() {
        productList = new ArrayList<>();
        product1 = Product.builder().name("water").description("sparkling").price(0.99).amountAvailable(10).build();
        productList.add(product1);
        shopList = new ArrayList<>();
        shop1 = Shop.builder().id(1L).name("StoreForYou").city("Cracow").address("Starowiślna 8").productList(productList).latitude(50.049683).longitude(19.944544).build();
        shopList.add(shop1);
        product1.setShop(shop1);
    }

    @Test
    void createNewShop() {
        service.createNewShop(shop1);
        Mockito.verify(shopRepository).save(shop1);
    }

    @Test
    void getAllShops() {
        Mockito.when(shopRepository.findAll()).thenReturn(shopList);
        List<Shop> result = service.getAllShops();
        assertEquals(1, result.size());
    }
}