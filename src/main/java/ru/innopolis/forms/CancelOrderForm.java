package ru.innopolis.forms;

public class CancelOrderForm {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CancelOrderForm{" +
                "id=" + id +
                '}';
    }
}
