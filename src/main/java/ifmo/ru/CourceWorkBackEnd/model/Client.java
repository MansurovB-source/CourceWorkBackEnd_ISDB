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
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "human_id", referencedColumnName = "id")
    private Human human;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_place_id", referencedColumnName = "id")
    private DeliveryPlace delivery_place;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id", referencedColumnName = "id")
    private Subscription subscription;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public Client() {
    }

    public Client(Long id, Human human, DeliveryPlace delivery_place, Subscription subscription, User user) {
        this.id = id;
        this.human = human;
        this.delivery_place = delivery_place;
        this.subscription = subscription;
        this.user = user;
    }
}