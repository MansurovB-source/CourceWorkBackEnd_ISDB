package ifmo.ru.CourceWorkBackEnd.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Behruz Mansurov
 */
@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_from", referencedColumnName = "id")
    private Client _from;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_to", referencedColumnName = "id")
    private Provider _to;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sausage_id", referencedColumnName = "id")
    private Sausage sausage;

    private Double sausages_weight;

    private LocalDateTime ord_time;

    private Boolean special;

    public Order(Long id, Client _from, Provider _to, Sausage sausage, Double sausages_weight, LocalDateTime ord_time, Boolean special) {
        this.id = id;
        this._from = _from;
        this._to = _to;
        this.sausage = sausage;
        this.sausages_weight = sausages_weight;
        this.ord_time = ord_time;
        this.special = special;
    }

    public Order() {
    }
}