package pl.storedemo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pl.storedemo.entities.DelivererImpl;
import pl.storedemo.entities.Product;
import pl.storedemo.entities.Shop;
import pl.storedemo.repositories.DelivererRepository;
import pl.storedemo.repositories.ProductRepository;
import pl.storedemo.repositories.ShopRepository;
import pl.storedemo.repositories.UserRepository;
import pl.storedemo.services.ProductService;
import pl.storedemo.services.ShopService;
import pl.storedemo.services.UserServiceImpl;

import java.util.Arrays;
import java.util.List;

@Component
public class ShopsBuild implements ApplicationRunner {

    @Autowired
    ShopService shopService;

    @Autowired
    ShopRepository shopRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    DelivererRepository delivererRepository;


    @Override
    public void run(ApplicationArguments args) throws Exception {


        DelivererImpl deliverer = new DelivererImpl(1L, "Matt", "Cracow");
        DelivererImpl deliverer1 = new DelivererImpl(2L, "Andrew", "Cracow");
        DelivererImpl deliverer2 = new DelivererImpl(3L, "Zoey", "Warsaw");

        delivererRepository.save(deliverer);
        delivererRepository.save(deliverer1);
        delivererRepository.save(deliverer2);

        Product product1 = Product.builder().name("water").description("sparkling").price(0.99).amountAvailable(10).build();
        Product product11 = Product.builder().name("milk").description("2.0% fat, 1L").price(2.99).amountAvailable(10).build();
        Product product111 = Product.builder().name("butter").description("500 g").price(4.59).amountAvailable(10).build();

        productRepository.save(product1);
        productRepository.save(product11);
        productRepository.save(product111);

        List<Product> productsOfStoreWithID1 = Arrays.asList(product1, product11, product111);

        Product product2 = Product.builder().name("mineral water").description("sparkling").price(1.29).amountAvailable(20).build();
        Product product22 = Product.builder().name("milk").description("2.0% fat, 1L").price(2.39).amountAvailable(20).build();
        Product product222 = Product.builder().name("butter").description("500 g").price(5.29).amountAvailable(20).build();

        productRepository.save(product2);
        productRepository.save(product22);
        productRepository.save(product222);

        List<Product> productsOfStoreWithID2 = Arrays.asList(product2, product22, product222);

        Product product3 = Product.builder().name("water").description("sparkling").price(1.19).amountAvailable(30).build();
        Product product33 = Product.builder().name("milk").description("2.0% fat, 1L").price(1.99).amountAvailable(30).build();
        Product product333 = Product.builder().name("butter").description("500 g").price(4.99).amountAvailable(30).build();

        List<Product> productsOfStoreWithID3 = Arrays.asList(product3, product33, product333);

        productRepository.save(product3);
        productRepository.save(product33);
        productRepository.save(product333);

        Shop shop1 = Shop.builder().id(1L).name("StoreForYou").city("Cracow").address("Starowiślna 8").productList(productsOfStoreWithID1).latitude(50.049683).longitude(19.944544).build();
        Shop shop2 = Shop.builder().id(2L).name("Store").city("Cracow").address("Grodzka 87").productList(productsOfStoreWithID2).latitude(50.048).longitude(19.94000).build();
        Shop shop3 = Shop.builder().id(3L).name("StoreForYou").city("Warsaw").address("Bracka 12").productList(productsOfStoreWithID3).latitude(52.237049).longitude(21.017532).build();

        shopRepository.save(shop1);
        shopRepository.save(shop2);
        shopRepository.save(shop3);

        product1.setShop(shop1);
        product11.setShop(shop1);
        product111.setShop(shop1);
        product2.setShop(shop2);
        product22.setShop(shop2);
        product222.setShop(shop2);
        product3.setShop(shop3);
        product33.setShop(shop3);
        product333.setShop(shop3);

        productRepository.save(product1);
        productRepository.save(product11);
        productRepository.save(product111);
        productRepository.save(product2);
        productRepository.save(product22);
        productRepository.save(product222);
        productRepository.save(product3);
        productRepository.save(product33);
        productRepository.save(product333);

    }
}
