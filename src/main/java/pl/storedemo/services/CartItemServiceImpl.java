package pl.storedemo.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.storedemo.entities.CartItem;
import pl.storedemo.repositories.CartItemRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;

    @Override
    public void createNewCartItem(CartItem cartItem) {
        cartItemRepository.save(cartItem);
    }

    @Override
    public List<CartItem> getAllItemsFromCart() {
        return cartItemRepository.findAll();
    }

    @Override
    public CartItem findById(Long id) {
        return cartItemRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public long countAllItemsInCart() {
        return getAllItemsFromCart().stream()
                .mapToLong(CartItem::getAmount)
                .sum();
    }

    @Override
    public double round(double value) {
        int precision = 2;
        BigDecimal bigDecimal = new BigDecimal(value);
        bigDecimal = bigDecimal.setScale(precision, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    @Override
    public double countTotalPriceInCart() {
        return round(getAllItemsFromCart().stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum());
    }

    @Override
    public boolean checkIfShopExistsInCart(Long id) {
        List<CartItem> items = getAllItemsFromCart().stream()
                .filter(cartItem -> cartItem.getProduct().getShop().getId().equals(id))
                .collect(Collectors.toList());
        return !items.isEmpty();
    }


    @Override
    public boolean checkIfCityExistsInCart(String city) {
        List<CartItem> items = getAllItemsFromCart().stream()
                .filter(cartItem -> cartItem.getProduct().getShop().getCity().equals(city))
                .collect(Collectors.toList());
        return !items.isEmpty();
    }

}
