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
@Table(name = "farms")
public class Farm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "factory_id", referencedColumnName = "id")
    private Factory factory;

    @ManyToOne()
    @JoinColumn(name = "animal_id", referencedColumnName = "id")
    private Animal animal;

    @ManyToOne()
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;
}