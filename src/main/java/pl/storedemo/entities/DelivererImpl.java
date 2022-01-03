package pl.storedemo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class DelivererImpl implements Deliverer {

    @Id
    @GeneratedValue
    @Column(name = "deliverer_id")
    private Long delivererId;
    private String name;
    private String city;
    private boolean isBusy;
    private Long orderId;

    public DelivererImpl(Long delivererId, String name, String city) {
        this.delivererId = delivererId;
        this.name = name;
        this.city = city;
        this.isBusy = false;
    }

    @Override
    public boolean isBusy() {
        return isBusy;
    }

    @Override
    public void hasNewOrder(OrderCart order) {
        isBusy = true;
    }
}
