package ifmo.ru.CourceWorkBackEnd.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Behruz Mansurov
 */
@Data
@Entity
@Table(name = "subscriptions")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer price;

    private Integer discount;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "subs_sausages",
            joinColumns = @JoinColumn(name = "subscription_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "sausage_id", referencedColumnName = "id"))
    private Collection<Sausage> sausages;
}