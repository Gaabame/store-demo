package pl.storedemo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.storedemo.entities.OrderCart;

@Repository
public interface OrderRepository extends JpaRepository<OrderCart, Long> {
}
