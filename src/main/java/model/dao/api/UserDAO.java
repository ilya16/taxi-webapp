package model.dao.api;

import model.beans.User;

public interface UserDAO extends DAO<User, Integer> {
    User findUserByLoginAndPassword(String login, String password);
    User registerUser(String login, String firstName, String lastName, String password);
}
