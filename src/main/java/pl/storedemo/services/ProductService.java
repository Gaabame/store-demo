package pl.storedemo.services;

import org.springframework.stereotype.Service;
import pl.storedemo.entities.Product;

import java.util.List;

@Service
public interface ProductService {

    void createNewProduct(Product product);
    List<Product> getlAllProducts();
    List<Product> getlAllProductsByStoreId(Long id);
    List<Product> getlAllProductsByStoreIdWithoutGivenProduct(Long id, Product product);
    List<Product> getAllProductsByCity(String city);
    Product findProductById(Long id);
    void diminishProductAmountAvailable(Long id, int amount);
    void increaseProductAmountAvailable(Long id, int amount);
    boolean isAvailable(Long id);
    boolean isAvailable(Long id, int amount);
    boolean IfProductExistsByGivenProductName(String productName, String city);
    List<Product> getProductsInStoresByProductNameAndCityAndAmount(String productName, String city, int amount);
    List<Product> getProductsInStoresByProductNameAndCity(String productName, String city);
    List<Product> getCheapestProductsByProductNameAndCity(String productName, String city);
    List<Product> getCheapestProductsByProductNameAndCityAndAmount(String productName, String city, int amount);
}
