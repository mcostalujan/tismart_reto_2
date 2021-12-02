package maxcosta.reto2.service;

import java.util.List;

import maxcosta.reto2.model.User;

public interface UserService {

    public User register(String name, String username, String email, String password);

    List<User> getUsers();

    User findUserByUsername(String username);

    User findUserByEmail(String email);

    public User addNewUser(String name, String username, String email, String password);

}
