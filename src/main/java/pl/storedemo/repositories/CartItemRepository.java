package pl.storedemo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.storedemo.entities.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

}
