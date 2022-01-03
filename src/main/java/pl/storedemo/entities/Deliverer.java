package pl.storedemo.entities;

public interface Deliverer {

        boolean isBusy();
        void hasNewOrder(OrderCart order);
}

