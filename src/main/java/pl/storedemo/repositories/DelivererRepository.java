package pl.storedemo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.storedemo.entities.DelivererImpl;

@Repository
public interface DelivererRepository extends JpaRepository<DelivererImpl, Long> {

}
