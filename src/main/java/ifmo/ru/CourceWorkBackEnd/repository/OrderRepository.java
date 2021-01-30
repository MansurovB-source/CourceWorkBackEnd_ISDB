package ifmo.ru.CourceWorkBackEnd.repository;


import ifmo.ru.CourceWorkBackEnd.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Behruz Mansurov
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllBy_from_Id(Long id);
    @Query(nativeQuery = true, value = "SELECT * FROM orders WHERE _from = :client_id AND ord_time > :o_date ")
    List<Order> findAllBy_from_IdAndOrd_timeGreaterThan(@Param("client_id") Long id, @Param("o_date") Date date);
}
