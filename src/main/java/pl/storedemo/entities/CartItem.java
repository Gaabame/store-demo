package pl.storedemo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@NoArgsConstructor
@Entity
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue
    @Column(name = "carttItem_id")
    private Long id;
    @OneToOne (cascade = CascadeType.PERSIST)
    private Product product;
    private int amount;
    private double totalPrice;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderCart order;

    public CartItem(Product product, int amount) {
        super();
        this.product = product;
        this.amount = amount;
        this.totalPrice = round(amount * product.getPrice());
    }

    public CartItem(Long id, Product product, int amount) {
        this.id = id;
        this.product = product;
        this.amount = amount;
        this.totalPrice = round(amount * product.getPrice());
    }

    public double round(double value) {
        int precision = 2;
        BigDecimal bigDecimal = new BigDecimal(value);
        bigDecimal = bigDecimal.setScale(precision, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }
}
