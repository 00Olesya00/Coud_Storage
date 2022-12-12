package ServerNetty.services.impl;

import ServerNetty.models.User;
import ServerNetty.services.AuthService;

import java.util.ArrayList;
import java.util.List;

public class SimpleAuthServiceImpl implements AuthService {
    private static List<User> users = new ArrayList<>();
    static{
        users.add(new User("boris", "123456"));
        users.add(new User("anna", "123456"));
        users.add(new User("dima", "123456"));

    }

    @Override
    public void addUser(String login, String password) {
        System.out.println("Простая авторизация");
        users.add(new User(login, password));

    }

    @Override
    public boolean checkCredentials(String login, String password) {
        for (User u : users) {
            if (u.getLogin().equals(login.toLowerCase()) && u.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkUserExist(String login) {
        for (User u : users) {
            if (u.getLogin().toLowerCase().equals(login.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<User> getUserList() {
        return users;
    }
}
