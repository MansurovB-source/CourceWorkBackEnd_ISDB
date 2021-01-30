package ifmo.ru.CourceWorkBackEnd.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Behruz Mansurov
 */
@Data
@Entity
@Table(name = "order_schedule")
public class OrderSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "provider_id", referencedColumnName = "id")
    private Provider provider;

    @ManyToOne
    @JoinColumn(name = "sausage_id", referencedColumnName = "id")
    private Sausage sausage;

    private Double sausages_weight;

    private Date del_time;

    @ManyToMany
    @JoinTable(name = "car_schedule",
            joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "car_id", referencedColumnName = "id"))
    private Collection<Car> cars;
}