package ifmo.ru.CourceWorkBackEnd.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Behruz Mansurov
 */
@Data
@Entity
@Table(name = "providers_payments")
public class ProviderPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "provider_id", referencedColumnName = "id")
    private Provider provider;

    @ManyToOne
    @JoinColumn(name = "factory_id", referencedColumnName = "id")
    private Factory factory;

    private Integer sum;

    private Date dept_time;

    private Boolean paying;

    private Date payment_date;
}