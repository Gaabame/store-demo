package pl.storedemo.controller;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.storedemo.entities.*;
import pl.storedemo.repositories.*;
import pl.storedemo.services.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ComponentScan("pl.storedemo.*")
@SpringBootTest(classes = ShopController.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class ShopControllerTest {

    @MockBean
    ProductRepository productRepository;

    @MockBean
    ProductServiceImpl productService;

    @MockBean
    ShopRepository shopRepository;

    @MockBean
    ShopServiceImpl shopService;

    @MockBean
    CartItemRepository cartItemRepository;

    @MockBean
    CartItemServiceImpl cartItemService;

    @MockBean
    OrderRepository orderRepository;

    @MockBean
    OrderServiceImpl orderService;

    @MockBean
    DelivererServiceImpl delivererService;

    @MockBean
    DelivererRepository delivererRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    UserServiceImpl userService;

    @Autowired
    MockMvc mockMvc;

    List<Shop> shops;
    List<Product> products;
    List<Product> allProducts;
    List<Product> searchedProducts;
    List<User> users;
    List<OrderCart> orders;
    List<CartItem> cartItems;
    List<DelivererImpl> deliverers;
    long count;
    Product product;
    Shop shop;
    CartItem cartItem;
    OrderCart order;
    User user;
    String message;
    String city;
    int amount;
    double totalPrice;

    @BeforeEach
    public void init() {
        shop = new Shop();
        shops = new ArrayList<>();
        cartItems = new ArrayList<>();
        products = new ArrayList<>();
        allProducts = new ArrayList<>();
        searchedProducts = new ArrayList<>();
        orders = new ArrayList<>();
        users = new ArrayList<>();
        deliverers = new ArrayList<>();
        count = 0;
        cartItem = new CartItem();
        totalPrice = 0;
        product = new Product();
        order = new OrderCart();
        message = "message";
        city = "Cracow";
        amount = 0;
        user = new User();
        product.setShop(shop);
    }

    @Test
    public void test_gotToIndex_statusOk() throws Exception {
        Mockito.when(cartItemService.countAllItemsInCart()).thenReturn(count);
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("cartItemsCount", count));
    }

    @Test
    public void test_getOrders_statusOk() throws Exception {
        Mockito.when(orderService.getAllOrders()).thenReturn(orders);
        Mockito.when(userService.getAlUsers()).thenReturn(users);
        Mockito.when(delivererService.getAllDeliverers()).thenReturn(deliverers);
        mockMvc.perform(get("/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("check"))
                .andExpect(model().attribute("orders", orders))
                .andExpect(model().attribute("users", users))
                .andExpect(model().attribute("deliverers", deliverers));
    }

    @Test
    public void test_showAllStores_statusOk() throws Exception {
        Mockito.when(shopService.getAllShops()).thenReturn(shops);
        Mockito.when(cartItemService.countAllItemsInCart()).thenReturn(count);

        mockMvc.perform(get("/allStores"))
                .andExpect(status().isOk())
                .andExpect(view().name("allStoresView"))
                .andExpect(model().attribute("shops", shops))
                .andExpect(model().attribute("cartItemsCount", count));
    }

    @Test
    public void test_showAllProducts_statusOk() throws Exception {
        Mockito.when(productService.getlAllProducts()).thenReturn(products);
        Mockito.when(cartItemService.countAllItemsInCart()).thenReturn(count);
        mockMvc.perform(get("/allProducts"))
                .andExpect(status().isOk())
                .andExpect(view().name("allProductsView.html"))
                .andExpect(model().attribute("products", products))
                .andExpect(model().attribute("cartItemsCount", count));
    }

//    @Test
//    public void test_searchForCheapestProduct_statusOk() throws Exception {
//        Mockito.when(cartItemService.countAllItemsInCart()).thenReturn(count);
//
//        mockMvc.perform(get("/search")) //add params!
//                .andExpect(status().isOk())
//                .andExpect(view().name("productWithMinPrice"))
//                .andExpect(model().attribute("cartItemsCount", count));
//    }

//    @Test
//    public void test_goToStore_statusOk() throws Exception {
//        Mockito.when(cartItemService.countAllItemsInCart()).thenReturn(count);
//        Mockito.when(productService.findProductById(1L)).thenReturn(product);
//        Mockito.when(productService.getlAllProductsByStoreIdWithoutGivenProduct(product.getShop().getId(), product)).thenReturn(allProducts);
//        Mockito.when(productService.getCheapestProductsByProductNameAndCityAndAmount(product.getName(), city, amount)).thenReturn(searchedProducts);
//
//        mockMvc.perform(get("/store")) //add params!
//                .andExpect(status().isOk())
//                .andExpect(view().name("store"))
//                .andExpect(model().attribute("cartItemsCount", count))
//                .andExpect(model().attribute("searchedProducts", searchedProducts))
//                .andExpect(model().attribute("allProducts", allProducts))
//                .andExpect(model().attribute("message", message));
//    }

    @Test
    public void test_goToStoreProduct_statusOk() throws Exception {
        Mockito.when(cartItemService.countAllItemsInCart()).thenReturn(count);
        Mockito.when(productService.findProductById(1L)).thenReturn(product);
        Mockito.when(productService.getlAllProductsByStoreId(product.getShop().getId())).thenReturn(allProducts);

        mockMvc.perform(get("/storeProduct?id=1"))
                .andExpect(status().isOk())
                .andExpect(view().name("store"))
                .andExpect(model().attribute("allProducts", allProducts))
                .andExpect(model().attribute("cartItemsCount", count));
    }


//    @Test
//    public void test_addProductToCart_statusOk() throws Exception {
//        Mockito.when(cartItemService.countAllItemsInCart()).thenReturn(count);
//        Mockito.when(cartItemService.getAllItemsFromCart()).thenReturn(cartItems);
//        Mockito.when(cartItemService.countTotalPriceInCart()).thenReturn(totalPrice);
//
//        mockMvc.perform(get("/addToCart"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("store"))
//                .andExpect(model().attribute("cartItemsCount", count))
//                .andExpect(model().attribute("disable", cartItems.isEmpty()))
//                .andExpect(model().attribute("cart", cartItems))
//                .andExpect(model().attribute("total", totalPrice));
//    }

    @Test
    public void test_addProductInCart_statusOk() throws Exception {
        Mockito.when(cartItemService.countAllItemsInCart()).thenReturn(count);
        Mockito.when(cartItemService.getAllItemsFromCart()).thenReturn(cartItems);
        Mockito.when(cartItemService.countTotalPriceInCart()).thenReturn(totalPrice);

        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(view().name("cartView"))
                .andExpect(model().attribute("cartItemsCount", count))
                .andExpect(model().attribute("disable", cartItems.isEmpty()))
                .andExpect(model().attribute("cart", cartItems))
                .andExpect(model().attribute("total", totalPrice));
    }

    @Test
    public void test_addAmountOfProductInCart_statusOk() throws Exception {

        Product searchedProduct = new Product();
        searchedProduct.setId(10L);
        searchedProduct.setAmountAvailable(15);
        cartItem.setProduct(searchedProduct);

        Mockito.when(cartItemService.findById(1L)).thenReturn(cartItem);
        Mockito.when(productService.findProductById(cartItem.getProduct().getId())).thenReturn(searchedProduct);

        Mockito.when(productService.isAvailable(10L)).thenReturn(true);
        cartItems.add(cartItem);
        Mockito.when(cartItemService.countAllItemsInCart()).thenReturn(count);
        Mockito.when(cartItemService.getAllItemsFromCart()).thenReturn(cartItems);
        Mockito.when(cartItemService.countTotalPriceInCart()).thenReturn(totalPrice);

        mockMvc.perform(get("/add?id=1"))
                .andExpect(status().isOk())
                .andExpect(view().name("cartView"))
                .andExpect(model().attribute("cartItemsCount", count))
                .andExpect(model().attribute("disable", cartItems.isEmpty()))
                .andExpect(model().attribute("cart", cartItems))
                .andExpect(model().attribute("total", totalPrice));
        Mockito.verify(productService).diminishProductAmountAvailable(10L, 1);
    }

//    @Test
//    public void test_removeAmountOfProductInCart_statusOk() throws Exception {
//        Mockito.when(cartItemService.findById(1L)).thenReturn(cartItem);
//        Mockito.when(productService.findProductById(1L)).thenReturn(product);
//        Mockito.when(cartItemService.countAllItemsInCart()).thenReturn(count);
//
//        Mockito.when(cartItemService.countAllItemsInCart()).thenReturn(count);
//        Mockito.when(cartItemService.getAllItemsFromCart()).thenReturn(cartItems);
//        Mockito.when(cartItemService.countTotalPriceInCart()).thenReturn(totalPrice);
//
//        mockMvc.perform(get("/remove?id=1")) //add params!
//                .andExpect(status().isOk())
//                .andExpect(view().name("cartView"))
//                .andExpect(model().attribute("cartItemsCount", count))
//                .andExpect(model().attribute("disable", cartItems.isEmpty()))
//                .andExpect(model().attribute("cart", cartItems))
//                .andExpect(model().attribute("total", totalPrice));
//    }
//
//    @Test
//    public void test_deleteProductFromCart_statusOk() throws Exception {
//        Mockito.when(cartItemService.findById(1L)).thenReturn(cartItem);
//        Mockito.when(cartItemService.countAllItemsInCart()).thenReturn(count);
//        mockMvc.perform(get("/delete?id=1")) //add params!
//                .andExpect(status().isOk())
//                .andExpect(view().name("cartView"))
//                .andExpect(model().attribute("cartItemsCount", count));
//    }

    @Test
    public void test_emptyCart_statusOk() throws Exception {
        String info = "Your cart is empty";
        mockMvc.perform(get("/emptyCart"))
                .andExpect(status().isOk())
                .andExpect(view().name("cartViewEmpty"))
                .andExpect(model().attribute("info", info));
    }

    @Test
    public void test_confirmCart_statusOk() throws Exception {
        cartItems.add(cartItem);
        Mockito.when(cartItemService.getAllItemsFromCart()).thenReturn(cartItems);
        OrderCart order1 = new OrderCart(cartItems);
        orderService.createNewOrder(order1);

        Mockito.when(orderService.getOrderById(1L)).thenReturn(order1);
        mockMvc.perform(get("/confirm"))
                .andExpect(status().isOk())
                //.andExpect(model().attribute("order", order1))
                .andExpect(view().name("confirmCart"));
        //Mockito.verify(orderService).createNewOrder(1, ca);
    }

    @Test
    public void test_continueVew_statusOk() throws Exception {
        Mockito.when(orderService.getOrderById(1L)).thenReturn(order);
        Mockito.when(cartItemService.countTotalPriceInCart()).thenReturn(totalPrice);
        Mockito.when(cartItemService.getAllItemsFromCart()).thenReturn(cartItems);
        Mockito.when(cartItemService.countAllItemsInCart()).thenReturn(count);

        mockMvc.perform(get("/register?id=1"))
                .andExpect(status().isOk())
                .andExpect(view().name("registerView"))
                .andExpect(model().attribute("order", order))
                .andExpect(model().attribute("cartItemsCount", count))
                .andExpect(model().attribute("cart", cartItems))
                .andExpect(model().attribute("total", totalPrice));
    }

//    @Test
//    public void test_goToRegister_statusOk() throws Exception {
//        Mockito.when(cartItemService.countAllItemsInCart()).thenReturn(count);
//        mockMvc.perform(post("/register")) //add params!
//                .andExpect(status().isOk())
//                .andExpect(view().name("view"))
//                .andExpect(model().attribute("cartItemsCount", count));
//    }
//
//    @Test
//    public void test_delivery_statusOk() throws Exception {
//
//        Mockito.when(orderService.getOrderById(1L)).thenReturn(order);
//        Mockito.when(userService.getUserByUserName("sam")).thenReturn(user);
//        Mockito.when(cartItemService.countAllItemsInCart()).thenReturn(count);
//        Mockito.when(orderService.getCityFromOrder(order)).thenReturn(city);
//
//        mockMvc.perform(get("/delivery?id=1&username=sam"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("deliveryView"));
////                .andExpect(model().attribute("user", user))
////                .andExpect(model().attribute("order", order))
////                .andExpect(model().attribute("cartItemsCount", count));
//
//        verify(delivererService, times(1)).searchFreeDeliverer(order, city);
//    }

}