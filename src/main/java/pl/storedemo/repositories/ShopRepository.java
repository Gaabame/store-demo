package pl.storedemo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.storedemo.entities.Shop;

import java.util.List;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {

    Shop findByName (String name);
    List<Shop> findByCity (String city);

}
