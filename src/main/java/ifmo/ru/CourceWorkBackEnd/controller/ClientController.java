package ifmo.ru.CourceWorkBackEnd.controller;

import ifmo.ru.CourceWorkBackEnd.DTOmodel.ClientPaymentDTO;
import ifmo.ru.CourceWorkBackEnd.DTOmodel.HumanDTO;
import ifmo.ru.CourceWorkBackEnd.DTOmodel.OrderDTO;
import ifmo.ru.CourceWorkBackEnd.DTOmodel.OrderFromClientDTO;
import ifmo.ru.CourceWorkBackEnd.model.*;
import ifmo.ru.CourceWorkBackEnd.repository.*;
import ifmo.ru.CourceWorkBackEnd.service.ClientService;
import ifmo.ru.CourceWorkBackEnd.service.HumanService;
import ifmo.ru.CourceWorkBackEnd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.script.ScriptException;
import java.security.Principal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Behruz Mansurov
 */

@RestController
public class ClientController {
    private final UserService userService;
    private final ClientService clientService;
    private final HumanService humanService;
    private final SubscriptionRepository subscriptionRepository;
    private final DeliveryPlaceRepository deliveryPlaceRepository;
    private final SausageRepository sausageRepository;
    private final OrderRepository orderRepository;
    private final ClientPaymentRepository clientPaymentRepository;

    @Autowired
    public ClientController(UserService userService, ClientService clientService, HumanService humanService, SubscriptionRepository subscriptionRepository, DeliveryPlaceRepository deliveryPlaceRepository, SausageRepository sausageRepository, OrderRepository orderRepository, ClientPaymentRepository clientPaymentRepository) {
        this.userService = userService;
        this.clientService = clientService;
        this.humanService = humanService;
        this.subscriptionRepository = subscriptionRepository;
        this.deliveryPlaceRepository = deliveryPlaceRepository;
        this.sausageRepository = sausageRepository;
        this.orderRepository = orderRepository;
        this.clientPaymentRepository = clientPaymentRepository;
    }

    @CrossOrigin
    @PostMapping("/human")
    public ResponseEntity<String> setHuman(@RequestBody HumanDTO humanDTO, Principal principal) {
        try {
            Human human = new Human(null, humanDTO.getName(), humanDTO.getSurname(), humanDTO.getBirth_date(), humanDTO.getContacts(), humanDTO.getAddress());
            User n_user = userService.findByUsername(principal.getName());
            if (clientService.findClientByUser(n_user) != null) {
                throw new Exception("This user has information about himself");
            }
            DeliveryPlace deliveryPlace = deliveryPlaceRepository.findById(1L).orElseThrow(() -> new Exception("Delivery place not found"));
            Subscription subscription = subscriptionRepository.findById(1L).orElseThrow(() -> new Exception("Subscription not found"));
            Client client = new Client(null, human, deliveryPlace, subscription, n_user);
            humanService.save(human);
            clientService.save(client);
            return new ResponseEntity<>("Saved", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/human")
    public HumanDTO getHuman(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Client client = clientService.findClientByUser(user);
        HumanDTO humanDTO = new HumanDTO(client.getHuman().getName(), client.getHuman().getSurname(), client.getHuman().getBirth_date(), client.getHuman().getContacts(), client.getHuman().getAddress());
        return humanDTO;

    }

    @CrossOrigin
    @GetMapping("/products")
    public Collection<Sausage> sausages() {
        return sausageRepository.findAll();
    }

    @GetMapping("/orders")
    public List<OrderDTO> getOrder(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Client client = clientService.findClientByUser(user);
        List<Order> orders = orderRepository.findAllBy_from_IdAndOrd_timeGreaterThan(client.getId(),
                Date.valueOf(LocalDate.now().minusDays(5)));
        List<OrderDTO> ordersDTO = new ArrayList<>();
        for (Order o : orders) {
            OrderDTO orderDTO = new OrderDTO(o.get_to().getHuman().getName(), o.get_to().getFactory().getName(),
                    o.getSausage().getName(), o.getSausages_weight().longValue());
            ordersDTO.add(orderDTO);
        }
        return ordersDTO;
    }

    @PostMapping("/orders")
    public ResponseEntity<String> setOrder(@RequestBody Collection<OrderFromClientDTO> orders, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Client client = clientService.findClientByUser(user);
        for (OrderFromClientDTO o : orders) {
            Order order = new Order(null, client, null,
                    sausageRepository.findById(o.getSausageId()).orElseThrow(
                            () -> new EntityNotFoundException("We don't have sausage with " + o.getSausageId())), o.getSausageWeight(),
                    null, false);
            try {
                orderRepository.save(order);
            } catch (DataAccessException e) {

                return new ResponseEntity<>("We do not have this product in our storage, it would be soon", HttpStatus.OK);
            } catch (EntityNotFoundException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity<>("All orders saved in the database", HttpStatus.CREATED);
    }

    @GetMapping("/payment")
    public List<ClientPaymentDTO> getPayment(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Client client = clientService.findClientByUser(user);
        List<ClientPayment> clientPaymentList = clientPaymentRepository.findAllByClient(client);
        List<ClientPaymentDTO> clientPaymentDTOList = new ArrayList<>();
        for (ClientPayment clientPayment : clientPaymentList) {
            ClientPaymentDTO clientPaymentDTO = new ClientPaymentDTO(clientPayment.getDept_time(),
                    clientPayment.getSum(), clientPayment.getPaying(), clientPayment.getPayment_date());
            clientPaymentDTOList.add(clientPaymentDTO);
        }
        return clientPaymentDTOList;
    }


//    @GetMapping("/subscriptions")
//    Collection<Subscription> getSubscription

}


