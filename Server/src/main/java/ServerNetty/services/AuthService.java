package ServerNetty.services;

import ServerNetty.models.User;

import java.util.List;

public interface AuthService {

    public void addUser(String login, String password);

    public boolean checkCredentials(String login, String password);

    public boolean checkUserExist(String login);

    public List<User> getUserList();



}
