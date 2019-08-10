package mvnikitin.javaadvanced.lesson7.server;

import java.sql.*;

public class AuthService {
    private static Connection connection;
    private static Statement stmt;

    public static void connect() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:src\\mvnikitin\\javaadvanced\\netchatusers");
            stmt = connection.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String getNickByLoginAndPass(String login, String pass) throws SQLException {

        String query = String.format("SELECT nick FROM users where username = '%s' and password = '%s'", login, pass);
        ResultSet rs = stmt.executeQuery(query);

        if (rs.next()) {
            return rs.getString(1);
        }

        return null;
    }

    public static void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
