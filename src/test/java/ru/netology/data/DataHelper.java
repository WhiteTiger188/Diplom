package ru.netology.data;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileInputStream;

import lombok.SneakyThrows;
public class DataHelper {
    private static final QueryRunner QUERY_RUNNER = new QueryRunner();
    private DataHelper() {
    }
    private static Connection getConn() throws SQLException {
        Properties props = new Properties();
        try (InputStream input = new FileInputStream("application.properties")){
        props.load(input);
        return DriverManager.getConnection(props.getProperty("db.url"), props.getProperty("db.username"), props.getProperty("db.password"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    @SneakyThrows
    public static String getStatusPayment() {
        var status = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1";
        var conn = getConn();
        return QUERY_RUNNER.query(conn, status, new ScalarHandler<String>());

    }
    @SneakyThrows
    public static String getStatusCredit() {
        var status = "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1";
        var conn = getConn();
        return QUERY_RUNNER.query(conn, status, new ScalarHandler<String>());

    }
        @SneakyThrows
        public static String getStatusOrderCredit() {
            var id = "SELECT credit_id FROM order_entity ORDER BY created DESC LIMIT 1";
            var conn = getConn();
            return QUERY_RUNNER.query(conn, id, new ScalarHandler<String>());
    }

    @SneakyThrows
    public static String getStatusOrderPayment() {
        var id = "SELECT payment_id FROM order_entity ORDER BY created DESC LIMIT 1";
        var conn = getConn();
        return QUERY_RUNNER.query(conn, id, new ScalarHandler<String>());

    }
   @SneakyThrows
   public static void cleanDB() {
       var connection = getConn();
       QUERY_RUNNER.execute(connection, "DELETE FROM credit_request_entity");
       QUERY_RUNNER.execute(connection, "DELETE FROM payment_entity");
       QUERY_RUNNER.execute(connection, "DELETE FROM order_entity");
    }

}