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
@SpringBootTest(classes = ProductServiceImpl.class)
@ExtendWith(SpringExtension.class)
class ProductServiceImplTest {

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
    ProductServiceImpl service;

    Product product1;
    Product product2;
    Product product3;
    Product product4;
    Product product5;
    Product product6;
    Shop shop1;
    Shop shop2;
    List<Product> productList;
    List<Product> shop1Products;
    List<Product> shop2Products;

    @BeforeEach
    void init() {
        productList = new ArrayList<>();
        shop1Products = new ArrayList<>();
        shop2Products = new ArrayList<>();
        product1 = Product.builder().id(1L).name("mineral water").description("sparkling").price(1.29).amountAvailable(20).build();
        product2 = Product.builder().id(2L).name("milk").description("2.0% fat, 1L").price(2.39).amountAvailable(20).build();
        product3 = Product.builder().id(3L).name("water").description("sparkling").price(0.29).amountAvailable(20).build();

        product4 = Product.builder().id(4L).name("mineral water").description("sparkling").price(1.29).amountAvailable(20).build();
        product5 = Product.builder().id(5L).name("milk").description("2.0% fat, 1L").price(2.39).amountAvailable(20).build();
        product6 = Product.builder().id(6L).name("water").description("500 g").price(1.29).amountAvailable(30).build();

        productList.add(product1);
        productList.add(product2);
        productList.add(product3);
        productList.add(product4);
        productList.add(product5);
        productList.add(product6);

        shop1Products.add(product1);
        shop1Products.add(product2);
        shop1Products.add(product3);

        shop2Products.add(product4);
        shop2Products.add(product5);
        shop2Products.add(product6);

        shop1 = Shop.builder().id(10L).name("StoreForYou").city("Cracow").address("Starowiślna 8").productList(shop1Products).latitude(50.049683).longitude(19.944544).build();
        shop2 = Shop.builder().id(20L).name("StoreForYou").city("Warsaw").address("Starowiślna 8").productList(shop2Products).latitude(50.049683).longitude(19.944544).build();

        product1.setShop(shop1);
        product2.setShop(shop1);
        product3.setShop(shop1);

        product4.setShop(shop2);
        product5.setShop(shop2);
        product6.setShop(shop2);

    }

    @Test
    void createNewProduct() {
        service.createNewProduct(product1);
        Mockito.verify(productRepository).save(product1);
    }

    @Test
    void getlAllProducts() {
        Mockito.when(productRepository.findAll()).thenReturn(productList);
        List<Product> result = service.getlAllProducts();
        assertEquals(6, result.size());
    }

    @Test
    void getlAllProductsByStoreId() {
        Mockito.when(productRepository.findAll()).thenReturn(productList);
        List<Product> result = service.getlAllProductsByStoreId(10L);
        assertEquals(3, result.size());
    }

    @Test
    void getlAllProductsByStoreIdWithoutGivenProduct() {
        Mockito.when(productRepository.findAll()).thenReturn(productList);
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        List<Product> result = service.getlAllProductsByStoreIdWithoutGivenProduct(10L, product1);
        assertEquals(2, result.size());
    }

    @Test
    void findProductById() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        Product result = service.findProductById(1L);
        assertEquals(product1, result);
    }

    @Test
    void test_diminishProductAmountAvailable_returns_same_amount_if_desired_amount_is_higher_than_availableAmount() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        service.diminishProductAmountAvailable(1L, 30);
        assertEquals(20, product1.getAmountAvailable());
    }

    @Test
    void test_diminishProductAmountAvailable_returns_decreasedAmountAvailable_by_desiredAmount_if_desired_amount_is_higher_than_availableAmount() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        service.diminishProductAmountAvailable(1L, 5);
        assertEquals(15, product1.getAmountAvailable());
    }

    @Test
    void increaseProductAmountAvailable() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        service.increaseProductAmountAvailable(1L, 5);
        assertEquals(25, product1.getAmountAvailable());
    }

    @Test
    void test_isAvailable_returns_true_if_amountAvailable_is_greater_than_zero() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        boolean result = service.isAvailable(1L);
        assertTrue(result);
    }

    @Test
    void test_isAvailable_returns_true_if_amountAvailable_is_greater_or_equal_to_desiredAmount() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        boolean result = service.isAvailable(1L, 10);
        assertTrue(result);
    }

    @Test
    void test_isAvailable_returns_false_if_amountAvailable_is_lower_than_desiredAmount() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        boolean result = service.isAvailable(1L, 30);
        assertFalse(result);
    }

    @Test
    void test_ifProductExistsByGivenProductName_returns_false_if_product_exists_in_given_City_and_productName() {
        Mockito.when(productRepository.findById(2L)).thenReturn(Optional.of(product2));
        boolean result = service.IfProductExistsByGivenProductName("milk", "Cracow");
        assertFalse(result);
    }

    @Test
    void getProductsInStoresByProductNameAndCity() {
        Mockito.when(productRepository.findAll()).thenReturn(productList);
        List<Product> result = service.getProductsInStoresByProductNameAndCity("water", "Cracow");
        assertEquals(2, result.size());
    }

    @Test
    void test_getProductsInStoresByProductNameAndCityAndAmount_returns_emptyList_if_availability_is_lower_than_desiredAmount() {
        Mockito.when(productRepository.findAll()).thenReturn(productList);
        List<Product> result = service.getProductsInStoresByProductNameAndCityAndAmount("milk", "Warsaw", 100);
        assertTrue(result.isEmpty());
    }

    @Test
    void test_getProductsInStoresByProductNameAndCityAndAmount_returns_oneElementList_if_availability_is_greater_or_equal_to_desiredAmount() {
        Mockito.when(productRepository.findAll()).thenReturn(productList);
        List<Product> result = service.getProductsInStoresByProductNameAndCityAndAmount("milk", "Warsaw", 20);
        assertEquals(1, result.size());
    }

    @Test
    void test_getProductsInStoresByProductNameAndCityAndAmount_returns_twoElementsList_if_availability_is_greater_or_equal_to_desiredAmount() {
        Mockito.when(productRepository.findAll()).thenReturn(productList);
        List<Product> result = service.getProductsInStoresByProductNameAndCityAndAmount("water", "Cracow", 20);
        assertEquals(2, result.size());
    }

    @Test
    void getAllProductsByCity() {
        Mockito.when(productRepository.findAll()).thenReturn(productList);
        List<Product> result = service.getAllProductsByCity("Cracow");
        assertEquals(3, result.size());
    }

    @Test
    void test_getCheapestProductsByProductNameAndCity_should_return_oneElementList_if_one_product_has_theLowest_price() {
        Mockito.when(productRepository.findAll()).thenReturn(productList);
        List<Product> result = service.getCheapestProductsByProductNameAndCity("water", "Cracow");
        assertEquals(1, result.size());
    }

    @Test
    void test_getCheapestProductsByProductNameAndCity_should_return_twoElementList_if_two_products_have_the_same_lowest_price() {
        Mockito.when(productRepository.findAll()).thenReturn(productList);
        List<Product> result = service.getCheapestProductsByProductNameAndCity("water", "Warsaw");
        assertEquals(2, result.size());
    }

    @Test
    void getCheapestProductsByProductNameAndCityAndAmount_should_return_oneElementList_if_two_products_have_the_same_lowest_price_but_condition_of_availability_is_met_by_only_one_product() {
        Mockito.when(productRepository.findAll()).thenReturn(productList);
        List<Product> result = service.getCheapestProductsByProductNameAndCityAndAmount("water", "Warsaw", 30);
        assertEquals(1, result.size());
    }
}