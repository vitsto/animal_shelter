package pro.sky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.entity.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
