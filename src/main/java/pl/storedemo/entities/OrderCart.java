package pl.storedemo.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class OrderCart {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;
    @OneToMany(fetch=FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.PERSIST)
    private List<CartItem> cartItems;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @ManyToOne (cascade = CascadeType.PERSIST)
    private User user;
    private double totalCost;
    private Long delivererId;

    public OrderCart(List<CartItem> cartItems) {
        super();
        this.cartItems = cartItems;
        this.date = LocalDate.now();
    }

    public OrderCart(Long id, List<CartItem> cartItems) {
        this.id = id;
        this.cartItems = cartItems;
        this.date = LocalDate.now();
    }

    public double getTotalPrice(){
        double total = 0;
        return round(cartItems.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum());
    }

    public double round(double value) {
        int precision = 2;
        BigDecimal bigDecimal = new BigDecimal(value);
        bigDecimal = bigDecimal.setScale(precision, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }
}
