package ifmo.ru.CourceWorkBackEnd.repository;


import ifmo.ru.CourceWorkBackEnd.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Behruz Mansurov
 */
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
}
