package ifmo.ru.CourceWorkBackEnd.model;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Behruz Mansurov
 */

@Data
@Entity
@Table(name = "providers")
public class Provider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "human_id", referencedColumnName = "id")
    private Human human;

    private Double salary;

    @ManyToOne
    @JoinColumn(name = "factory_id", referencedColumnName = "id")
    private Factory factory;

    @OneToOne
    @JoinColumn(name = "delivery_place_id", referencedColumnName = "id")
    private DeliveryPlace delivery_place;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}