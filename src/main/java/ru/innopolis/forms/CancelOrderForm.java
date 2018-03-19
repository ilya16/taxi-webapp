package ru.innopolis.forms;

/**
 * Form for cancelling the order.
 * Stores an identifier of the order to be cancelled.
 *
 * @author      Ilya Borovik
 * @version     1.0
 */
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
