package ServerNetty.services.impl;

import ServerNetty.components.DBConnection;
import ServerNetty.models.User;
import ServerNetty.services.AuthService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DBAuthService implements AuthService {

    private static DBAuthService INSTANCE;
    DBConnection connection;

    private DBAuthService() {
        connection = new DBConnection();
    }

    public static synchronized DBAuthService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DBAuthService();
        }
        return INSTANCE;
    }

    @Override
    public void addUser(String login, String password) {
        String query = "INSERT INTO performance_schema.users() VALUES(?,?)";

        try {

            PreparedStatement stmt = connection.getConnection().prepareStatement(query);
            stmt.setString(1, login);
            stmt.setString(2, password);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean checkCredentials(String login, String password) {
        boolean result = false;
        try {
            String query = "SELECT * FROM users WHERE login = '%s' AND password = '%s'";
            PreparedStatement stmt = connection.getConnection().prepareStatement(String.format(query, login, password));
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                result =  true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean checkUserExist(String login) {
        boolean result = false;
        try {
            String query = "SELECT * FROM users WHERE login = '%s'";
            PreparedStatement stmt = connection.getConnection().prepareStatement(String.format(query, login));
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                result =  true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<User> getUserList() {
        List<User> users = new ArrayList<>();

        try {
            String query = "SELECT * FROM users";
            PreparedStatement stmt = connection.getConnection().prepareStatement(String.format(query));
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                users.add(new User(resultSet.getString("login"), resultSet.getString("password")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }
}
