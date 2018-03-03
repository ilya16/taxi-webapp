package services;

import model.pojo.User;

public interface UserService {
    User auth(String login, String password);

    User register(String login, String password);
}
