package aloha.spring.restful_web_services.user;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class UserDaoService {

    private final static List<User> users;
    static {
        users = new ArrayList<>();
        users.addAll(Arrays.asList(
                new User(1, "Adam", LocalDate.of(1980, 1, 1)),
                new User(2, "Eve", LocalDate.of(1990, 7, 4)),
                new User(3, "Jim", LocalDate.of(2010, 10, 23))));
    }

    private int idSeq = 4;

    public List<User> findAll() {
        return users;
    }

    public User findOne(int id) {
        // Optional<User> user = users.stream().filter(u -> u.getId() == id).findFirst();
        // return user.isPresent() ? user.get() : null;
        return users.stream().filter(u -> u.getId() == id).findFirst().orElse(null);
    }

    public User save(User user) {
        user.setId(idSeq++);
        users.add(user);
        return user;
    }

    public boolean deleteById(int id) {
        return users.removeIf(u -> u.getId() == id);
    }

}
