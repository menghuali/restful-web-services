package aloha.spring.restful_web_services.user;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class UserController {

    private UserDaoService dao;

    public UserController(UserDaoService dao) {
        this.dao = dao;
    }

    @GetMapping("/users")
    public List<User> allUsers() {
        return dao.findAll();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable("id") int id) {
        User user = dao.findOne(id);
        if (user == null) {
            // throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            throw new UserNotFoundException();
        } else {
            return user;
        }
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = dao.save(user);
        // Return the location URI of a created resource.
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) {
        if (!dao.deleteById(id)) {
            throw new UserNotFoundException();
        }
    }
}
