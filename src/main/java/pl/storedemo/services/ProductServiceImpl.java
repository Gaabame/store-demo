package pl.storedemo.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.storedemo.entities.Product;
import pl.storedemo.repositories.ProductRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public void createNewProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public List<Product> getlAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getlAllProductsByStoreId(Long id) {
        return getlAllProducts().stream()
                .filter(product -> product.getShop().getId().equals(id))
                .sorted(Comparator.comparing(Product::getName).thenComparing(Product::getAmountAvailable))
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getlAllProductsByStoreIdWithoutGivenProduct(Long id, Product product) {
        return getlAllProductsByStoreId(id).stream()
                .filter(product1 -> !product1.equals(product))
                .collect(Collectors.toList());
    }

    @Override
    public Product findProductById(Long id) {
        return productRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public void diminishProductAmountAvailable(Long id, int amount) {
        Product product = productRepository.findById(id).orElseThrow(NoSuchElementException::new);
        if (product.getAmountAvailable() >= amount) {
            product.setAmountAvailable(product.getAmountAvailable() - amount);
            productRepository.save(product);
        }
    }

    @Override
    public void increaseProductAmountAvailable(Long id, int amount) {
        Product product = productRepository.findById(id).orElseThrow(NoSuchElementException::new);
        product.setAmountAvailable(product.getAmountAvailable() + amount);
        productRepository.save(product);
    }

    @Override
    public boolean isAvailable(Long id) {
        return findProductById(id).getAmountAvailable() > 0;
    }

    @Override
    public boolean isAvailable(Long id, int amount) {
        return findProductById(id).getAmountAvailable() >= amount;
    }

    @Override
    public boolean IfProductExistsByGivenProductName(String productName, String city) {
        long count = 0;
        List<Product> productListInCity = getlAllProducts().stream()
                .filter(product -> product.getShop().getCity().equals(city))
                .collect(Collectors.toList());
        if (!productListInCity.isEmpty()) {
            List<Product> collect = productListInCity.stream().filter(product -> product.getName().contains(productName)).collect(Collectors.toList());
            if (!collect.isEmpty()) {
                count = 1;
            }
        }
        return count != 0; //true if różne od 0
    }


    @Override
    public List<Product> getProductsInStoresByProductNameAndCity(String productName, String city) {
        return getAllProductsByCity(city).stream()
                .filter(product -> product.getName().contains(productName))
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getProductsInStoresByProductNameAndCityAndAmount(String productName, String city, int amount) {
        return getProductsInStoresByProductNameAndCity(productName, city).stream()
                .filter(product -> product.getAmountAvailable()>=amount)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getAllProductsByCity(String city) {
        return getlAllProducts().stream()
                .filter(product -> product.getShop().getCity().equals(city))
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getCheapestProductsByProductNameAndCity(String productName, String city) {
        List<Product> allProductsByCity = getProductsInStoresByProductNameAndCity(productName, city);

        int indexWithMin = 0;
        List<Product> list = new ArrayList<>();
        for (int i = 0; i < allProductsByCity.size(); i++) {
            if (allProductsByCity.get(i).getPrice() == allProductsByCity.get(indexWithMin).getPrice()) {
                list.add(allProductsByCity.get(i));
            } else if (allProductsByCity.get(indexWithMin).getPrice() > allProductsByCity.get(i).getPrice()) {
                list.clear();
                list.add(allProductsByCity.get(i));
                indexWithMin = i;
            }
        }
        return list;
    }

    @Override
    public List<Product> getCheapestProductsByProductNameAndCityAndAmount(String productName, String city, int amount) {
        List<Product> allProductsByCity = getProductsInStoresByProductNameAndCityAndAmount(productName, city, amount);
        int indexWithMin = 0;
        List<Product> list = new ArrayList<>();
        for (int i = 0; i < allProductsByCity.size(); i++) {
            if (allProductsByCity.get(i).getPrice() == allProductsByCity.get(indexWithMin).getPrice()) {
                list.add(allProductsByCity.get(i));
            } else if (allProductsByCity.get(indexWithMin).getPrice() > allProductsByCity.get(i).getPrice()) {
                list.clear();
                list.add(allProductsByCity.get(i));
                indexWithMin = i;
            }
        }
        return list;
    }
}

