package ifmo.ru.CourceWorkBackEnd.DTOmodel;

import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Behruz Mansurov
 */

@Data
public class OrderFromClientDTO {
    Long sausageId;
    String sausageName;
    Double sausageWeight;

    public OrderFromClientDTO(Long sausageId, String sausageName, Double sausageWeight) {
        this.sausageId = sausageId;
        this.sausageName = sausageName;
        this.sausageWeight = sausageWeight;
    }
}
