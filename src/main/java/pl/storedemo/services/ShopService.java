package pl.storedemo.services;

import org.springframework.stereotype.Service;
import pl.storedemo.entities.Shop;

import java.util.List;

@Service
public interface ShopService {

    void createNewShop(Shop shop);
    List<Shop> getAllShops();
}

