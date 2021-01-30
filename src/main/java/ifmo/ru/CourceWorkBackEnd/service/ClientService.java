package ifmo.ru.CourceWorkBackEnd.service;

import ifmo.ru.CourceWorkBackEnd.model.Client;
import ifmo.ru.CourceWorkBackEnd.model.Human;
import ifmo.ru.CourceWorkBackEnd.model.User;
import ifmo.ru.CourceWorkBackEnd.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Behruz Mansurov
 */
@Service
public class ClientService {
    ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client save(Client client) {
        return clientRepository.save(client);
    }

    public Client findClientByUser(User user) {
        Optional<Client> optionalClient = clientRepository.findClientsByUser(user);
        return optionalClient.orElse(null);
    }
}
