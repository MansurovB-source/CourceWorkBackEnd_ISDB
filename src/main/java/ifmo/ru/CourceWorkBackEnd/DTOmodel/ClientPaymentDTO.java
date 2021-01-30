package ifmo.ru.CourceWorkBackEnd.DTOmodel;

import lombok.Data;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Behruz Mansurov
 */

@Data
public class ClientPaymentDTO {
        Date deptDate;
        int sum;
        boolean paying;
        Date payingDate;

        public ClientPaymentDTO(Date deptDate, int sum, boolean paying, Date payingDate) {
                this.deptDate = deptDate;
                this.sum = sum;
                this.paying = paying;
                this.payingDate = payingDate;
        }

}
