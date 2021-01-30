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
@Table(name = "sausages")
public class Sausage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Double price;
}
