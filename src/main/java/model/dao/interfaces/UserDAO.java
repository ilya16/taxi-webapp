package model.dao.interfaces;

import model.pojo.User;

public interface UserDAO extends DAO<User, Integer> {
    User findUserByLoginAndPassword(String login, String password);

    User registerUser(String login, String password);
}
