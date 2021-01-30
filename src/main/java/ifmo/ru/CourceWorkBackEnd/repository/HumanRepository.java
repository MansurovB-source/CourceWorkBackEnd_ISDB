package ifmo.ru.CourceWorkBackEnd.repository;


import ifmo.ru.CourceWorkBackEnd.model.Human;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Behruz Mansurov
 */
public interface HumanRepository extends JpaRepository<Human, Long> {

}
