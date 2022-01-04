package pl.storedemo.services;

import org.springframework.stereotype.Service;
import pl.storedemo.entities.CartItem;

import java.util.List;


@Service
public interface CartItemService {

    void createNewCartItem(CartItem cartItem);
    List<CartItem> getAllItemsFromCart();
    CartItem findById(Long id);
    long countAllItemsInCart();
    double round(double value);
    double countTotalPriceInCart();
    boolean checkIfShopExistsInCart(Long id);
    boolean checkIfCityExistsInCart(String city);

}
