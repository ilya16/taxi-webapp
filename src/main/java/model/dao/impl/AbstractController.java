package model.dao.impl;

import model.dao.interfaces.DAO;
//import org.postgresql.jdbc2.optional.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public abstract class AbstractController<E, K> implements DAO {
    private Connection connection;
//    private ConnectionPool connectionPool;

    public AbstractController() {
//        connectionPool = ConnectionPool.getConnectionPool();
//        connection = connectionPool.getConnection();
    }

    // Возвращения экземпляра Connection в пул соединений
    public void returnConnectionInPool() {
//        connectionPool.returnConnection(connection);
    }

    // Получение экземпляра PrepareStatement
    public PreparedStatement getPreparedStatement(String sql) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ps;
    }

    // Закрытие PreparedStatement
    public void closePreparedStatement(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
