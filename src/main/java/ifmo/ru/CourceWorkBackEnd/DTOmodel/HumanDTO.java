package ifmo.ru.CourceWorkBackEnd.DTOmodel;

import lombok.Data;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Behruz Mansurov
 */

@Data
public class HumanDTO {
    private String name;
    private String surname;
    private Date birth_date;
    private String contacts;
    private String address;

    public HumanDTO(String name, String surname, Date birth_date, String contacts, String address) {
        this.name = name;
        this.surname = surname;
        this.birth_date = birth_date;
        this.contacts = contacts;
        this.address = address;
    }
}
