package ifmo.ru.CourceWorkBackEnd.repository;

import ifmo.ru.CourceWorkBackEnd.model.Client;
import ifmo.ru.CourceWorkBackEnd.model.ClientPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Behruz Mansurov
 */
public interface ClientPaymentRepository extends JpaRepository<ClientPayment, Long> {
    List<ClientPayment> findAllByClient(Client client);
}
