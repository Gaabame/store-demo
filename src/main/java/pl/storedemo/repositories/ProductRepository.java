package pl.storedemo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.storedemo.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
