package pro.sky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pro.sky.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
