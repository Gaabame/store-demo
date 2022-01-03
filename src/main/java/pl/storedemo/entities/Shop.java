package pl.storedemo.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class Shop {

    @Id
    @Column (name = "store_id")
    private Long id;
    @Column (nullable = false)
    private String name;
    private String address;
    private String city;
    private double longitude;
    private double latitude;
    @OneToMany (cascade = CascadeType.PERSIST)
    private List<Product> productList;

}
