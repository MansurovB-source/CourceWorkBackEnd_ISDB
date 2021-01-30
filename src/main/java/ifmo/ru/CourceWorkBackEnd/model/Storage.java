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
@Table(name = "storages")
public class Storage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "factory_id", referencedColumnName = "id")
    private Factory factory;

    @ManyToOne
    @JoinColumn(name = "sausage_id", referencedColumnName = "id")
    private Sausage sausage;

    private double sausages_weight;
}
