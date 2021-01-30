package ifmo.ru.CourceWorkBackEnd.service;

import ifmo.ru.CourceWorkBackEnd.model.Client;
import ifmo.ru.CourceWorkBackEnd.model.Human;
import ifmo.ru.CourceWorkBackEnd.model.User;
import ifmo.ru.CourceWorkBackEnd.repository.HumanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Behruz Mansurov
 */
@Service
public class HumanService {
    HumanRepository humanRepository;

    @Autowired
    public HumanService(HumanRepository humanRepository) {
        this.humanRepository = humanRepository;
    }

    public Human save(Human human) {
        return humanRepository.save(human);
    }

    public Human findHumanById(Long id) {
        Optional<Human> optionalHuman = humanRepository.findById(id);
        return optionalHuman.orElse(null);
    }
}
