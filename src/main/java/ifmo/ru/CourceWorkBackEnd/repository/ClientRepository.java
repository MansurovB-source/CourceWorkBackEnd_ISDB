package ifmo.ru.CourceWorkBackEnd.repository;

import ifmo.ru.CourceWorkBackEnd.model.Client;
import ifmo.ru.CourceWorkBackEnd.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.Collection;
import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Behruz Mansurov
 */
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findClientsByUser(User user);
}
