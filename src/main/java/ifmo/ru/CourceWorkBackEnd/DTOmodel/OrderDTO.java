package ifmo.ru.CourceWorkBackEnd.DTOmodel;

import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Behruz Mansurov
 */

@Data
public class OrderDTO {
    String providerName;
    String factoryName;
    String sausageName;
    long sausageWeight;

    public OrderDTO(String providerName, String factoryName, String sausageName, long sausageWeight) {
        this.providerName = providerName;
        this.factoryName = factoryName;
        this.sausageName = sausageName;
        this.sausageWeight = sausageWeight;
    }
}