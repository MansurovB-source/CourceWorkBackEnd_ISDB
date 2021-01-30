package ifmo.ru.CourceWorkBackEnd.repository;


import ifmo.ru.CourceWorkBackEnd.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Behruz Mansurov
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String name);
}
