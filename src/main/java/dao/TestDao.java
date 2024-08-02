package dao;

import config.DbConnection;
import org.sql2o.Connection;
import org.sql2o.Sql2oException;

import javax.annotation.Nonnull;
import java.util.Optional;

public class TestDao {
    private static Connection getConnectionAccount() {
        return DbConnection.getConnection("test").beginTransaction();
    }

    public static synchronized TestEntity getTestEntityByLogin(@Nonnull String login) {
        try (Connection connection = getConnectionAccount()) {
            return getTestEntityByLogin(connection, login);
        }
    }

    private static synchronized TestEntity getTestEntityByLogin(@Nonnull Connection connection, @Nonnull String login) {
        return Optional.ofNullable(connection
                        .createQuery("SELECT * from test WHERE login =:login;")
                        .addParameter("login", login)
                        .setAutoDeriveColumnNames(true)
                        .executeAndFetchFirst(TestEntity.class)
                )
                .orElseThrow(() -> new RuntimeException("Не найдено записей с логином " + login));
    }

    public static synchronized void deleteTestEntityByLogin(@Nonnull String login) {
        try (Connection connection = getConnectionAccount()) {
            deleteTestEntityByLogin(connection, login);
        }
    }

    private static synchronized void deleteTestEntityByLogin(@Nonnull Connection connection, @Nonnull String login) {
        try {
            connection
                    .createQuery("DELETE from test WHERE login=:login;")
                    .addParameter("login", login)
                    .executeUpdate()
                    .getResult();
        } catch (Sql2oException e) {
            connection.rollback();
            throw new Sql2oException("Error while deleting row with login " + login + " from table \"test\". Rollback. Original ex: " + e.getMessage());
        }

        try {
            connection.commit();
        } catch (Sql2oException e) {
            if (!e.getMessage().endsWith("Cannot commit when autoCommit is enabled.")) throw e;
        }
    }
}
