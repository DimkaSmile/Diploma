package ru.netology.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBHelper {
    private final static QueryRunner queryRunner = new QueryRunner();
    private final static Connection conn = connection();

    @SneakyThrows
    private static Connection connection() {
        String url = System.getProperty("db");
        String user = "user";
        String password = "pass";
        return DriverManager.getConnection(url, user, password);
    }

    // DEBIT CARD

    @SneakyThrows
    public static Integer getAmountDebitCard() {
        return queryRunner.query(conn, "select amount from payment_entity " +
                        "order by created desc",
                new ScalarHandler<>());
    }

    @SneakyThrows
    public static String getStatusDebitCard() {
        return queryRunner.query(conn, "SELECT status FROM payment_entity " +
                        "ORDER BY created DESC",
                new ScalarHandler<>());
    }

    @SneakyThrows
    public static String getTransactionIdDebitCard() {
        return queryRunner.query(conn, "select transaction_id from payment_entity pe " +
                        "order by created desc",
                new ScalarHandler<>());
    }

    // CREDIT CARD

    @SneakyThrows
    public static String getBankIdCreditCard() {
        return queryRunner.query(conn, "select bank_id from credit_request_entity cre " +
                        "order by created desc",
                new ScalarHandler<>());

    }

    @SneakyThrows
    public static String getStatusCreditCard() {
        return queryRunner.query(conn, "SELECT status FROM credit_request_entity " +
                        "ORDER BY created DESC",
                new ScalarHandler<>());
    }

    @SneakyThrows
    public static String getCreditId() {
        return queryRunner.query(conn, "select credit_id from order_entity oe " +
                        "order by created desc",
                new ScalarHandler<>());
    }

    @SneakyThrows
    public static String getPaymentId() {
        return queryRunner.query(conn, "select payment_id from order_entity oe " +
                        "order by created desc",
                new ScalarHandler<>());
    }

    @SneakyThrows
    public static void deleteAllDB() {
        queryRunner.update(conn, "DELETE FROM credit_request_entity");
        queryRunner.update(conn, "DELETE FROM order_entity");
        queryRunner.update(conn, "DELETE FROM payment_entity");
    }
}
