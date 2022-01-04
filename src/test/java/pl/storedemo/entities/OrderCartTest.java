package pl.storedemo.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = OrderCart.class)
class OrderCartTest {

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
        cartItem = new CartItem(1L, product1, 5);
        cartItemList.add(cartItem);
        orderList = new ArrayList<>();
        user = new User(1L, "john", "john@gmail.com", "password", "Krucza 2", "Cracow", orderList);

        order = new OrderCart(1L, cartItemList);
        order.setUser(user);
        orderList.add(order);
    }

    @Test
    void test_getTotalPrice_should_return4_if_cart_has_product_with_price_2_and_amount5() {
        double totalPrice = order.getTotalPrice();
        assertEquals(10.0, totalPrice);
    }

    @Test
    void test_round_number() {
        double totalPrice = 23.999;
        double round = order.round(totalPrice);
        assertEquals(24.00, round);
    }

    @Test
    void round() {
        double totalPrice = 23.855;
        double round = order.round(totalPrice);
        assertEquals(23.86, round);
    }
}