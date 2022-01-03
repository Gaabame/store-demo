package pl.storedemo.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class Product {

	  @Id
	  @GeneratedValue
	  @Column(name = "product_id")
	  private Long id;
	  private String name;
	  private double price;
	  private int amountAvailable;
	  private String description;
	  @OneToOne (cascade = CascadeType.PERSIST)
	  private Shop shop;

	  public Product(Long id, String name, double price, int amountAvailable, String description) {
	        this.id = id;
	        this.name = name;
	        this.price = price;
	        this.amountAvailable = amountAvailable;
	        this.description = description;
	    }
	
}
