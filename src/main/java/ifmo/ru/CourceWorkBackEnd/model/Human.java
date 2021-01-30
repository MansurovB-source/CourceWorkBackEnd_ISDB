package ifmo.ru.CourceWorkBackEnd.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Behruz Mansurov
 */
@Data
@Entity
@Table(name = "humans")
public class Human {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String surname;

    private Date birth_date;

    private String contacts;

    private String address;

    public Human(Long id, String name, String surname, Date birth_date, String contacts, String address) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.birth_date = birth_date;
        this.contacts = contacts;
        this.address = address;
    }

    public Human() {

    }
}
