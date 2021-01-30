package ifmo.ru.CourceWorkBackEnd.repository;


import ifmo.ru.CourceWorkBackEnd.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Behruz Mansurov
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
}
