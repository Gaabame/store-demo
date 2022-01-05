package pl.storedemo.services;

import pl.storedemo.entities.*;
import pl.storedemo.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ComponentScan("pl.storedemo.*")
@SpringBootTest(classes = CartItemServiceImpl.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class CartItemServiceImplTest {

    @MockBean
    CartItemRepository repository;

    @Autowired
    CartItemServiceImpl service;

    Shop shop;
    Shop shop2;
    List<Product> productList;
    List<Shop> shopList;
    Product product1;
    Product product11;
    Product product111;
    CartItem cartItem;
    CartItem cartItem2;
    CartItem cartItem3;
    List<CartItem> cartItemList;

    @BeforeEach
    public void init() {
        product1 = Product.builder().id(1L).name("water").description("sparkling").price(2).amountAvailable(10).build();
        product11 = Product.builder().id(2L).name("milk").description("2.0% fat, 1L").price(4).amountAvailable(10).build();
        product111 = Product.builder().id(3L).name("butter").description("500 g").price(6).amountAvailable(10).build();
        productList = Arrays.asList(product1, product11, product111);
        shop = Shop.builder().id(1L).name("StoreForYou").city("Cracow").address("Starowiślna 8").productList(productList).latitude(50.049683).longitude(19.944544).build();
        shopList = new ArrayList<>();
        shopList.add(shop);

        product1.setShop(shop);
        product11.setShop(shop);
        product111.setShop(shop);

        cartItem = new CartItem(1L, product1, 1);
        cartItem2 = new CartItem(2L, product11, 1);
        cartItem3 = new CartItem(3L, product111, 1);
        cartItemList = Arrays.asList(cartItem, cartItem2, cartItem3);
    }

    @Test
    void createNewCartItem() {
        service.createNewCartItem(cartItem);
        Mockito.verify(repository).save(cartItem);
    }

    @Test
    void getAllItemsFromCart() {
        when(repository.findAll()).thenReturn(cartItemList);
        List<CartItem> result = service.getAllItemsFromCart();
        assertEquals(cartItemList, result);
    }

    @Test
    void findById() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(cartItem));
        //when
        CartItem result = service.findById(1L);
        //then
        assertEquals(cartItem, result);
    }

    @Test
    void countAllItemsInCart() {
        Mockito.when(repository.findAll()).thenReturn(cartItemList);
        //when
        long result = service.countAllItemsInCart();
        //then
        assertEquals(3, result);
    }

    @Test
    void round() {
        double number = 20.95666666;
        double round = service.round(number);
        assertEquals(20.96, round);
    }

    @Test
    void countTotalPriceInCart() {
        Mockito.when(repository.findAll()).thenReturn(cartItemList);
        //when
        double result = service.countTotalPriceInCart();
        //then
        assertEquals(12, result);
    }

    @Test
    void checkIfShopExistsInCart() {
        Mockito.when(repository.findAll()).thenReturn(cartItemList);
        boolean result = service.checkIfShopExistsInCart(1L);
        assertTrue(result);
    }

    @Test
    void checkIfCityExistsInCart() {
        Mockito.when(repository.findAll()).thenReturn(cartItemList);
        boolean result = service.checkIfCityExistsInCart("Cracow");
        assertTrue(result);
    }

    @Test
    void test_returns_false_if_CityDoesNotExistInCart() {
        Mockito.when(repository.findAll()).thenReturn(cartItemList);
        boolean result = service.checkIfCityExistsInCart("Warsaw");
        assertFalse(result);
    }

}