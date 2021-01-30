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
@Table(name = "factories")
public class Factory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    private Long worker_num;

    @ManyToMany
    @JoinTable(name = "factory_sausages",
            joinColumns = @JoinColumn(name = "factory_id"),
            inverseJoinColumns = @JoinColumn(name = "sausage_id"))
    private Collection<Sausage> sausages;
}