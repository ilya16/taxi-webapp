package model.beans;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private String login;
    private String firstName;
    private String lastName;
    private String password;
    private boolean isBlocked;

    public User(long id, String login, String firstName, String lastName, String password, boolean isBlocked) {
        this.id = id;
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.isBlocked = isBlocked;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }


    @Override
    public String toString() {
        return "User {" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", isBlocked='" + isBlocked + '\'' +
                '}';
    }
}
