package ifmo.ru.CourceWorkBackEnd.controller;

import ifmo.ru.CourceWorkBackEnd.model.User;
import ifmo.ru.CourceWorkBackEnd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Behruz Mansurov
 */

@RestController
@CrossOrigin
public class EntryController {
    private UserService userService;

    @Autowired
    public EntryController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/login")
    public ResponseEntity<?> login(Principal principal) {
        User user;
        if (principal == null) {
            System.out.println("bitch");
            return new ResponseEntity<>("Wrong username or password, bitch", HttpStatus.UNAUTHORIZED);
        } else {
            System.out.println(principal.getName());
            user = userService.findByUsername(principal.getName());
        }
        return new ResponseEntity<>(user.getId(), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registration(@RequestBody User user) {
        Pattern pattern = Pattern.compile(
                "[" +                   //начало списка допустимых символов
                        "а-яА-ЯёЁ" +    //буквы русского алфавита
                        "\\p{Punct}" +  //знаки пунктуации
                        "]" +                   //конец списка допустимых символов
                        "*");                   //допускается наличие указанных символов в любом количестве
        if (pattern.matcher(user.getUsername()).matches()) {
            return new ResponseEntity<>("Unacceptable symbols", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        } else {
            if (userService.findByUsername(user.getUsername()) != null) {
                //System.out.println("Iloveyou");
                return new ResponseEntity<>(new RuntimeException("User with username " + user.getUsername() + " already exist"), HttpStatus.CONFLICT);
            }
        }
        return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
    }
}
