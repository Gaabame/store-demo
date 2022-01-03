package pl.storedemo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class User {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;
    @Column(unique = true)
    private String username;
    private String email;
    private String password;
    private String address;
    private String city;
    @OneToMany (cascade = CascadeType.PERSIST)
    private List<OrderCart> orders;

}