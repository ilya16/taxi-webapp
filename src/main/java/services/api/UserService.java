package services.api;

import model.beans.User;

public interface UserService {
    User auth(String login, String password);
    User register(String login, String firstName, String lastName, String password);
    User getUser(Integer userId);
}
