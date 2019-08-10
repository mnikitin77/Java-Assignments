package mvnikitin.javaadvanced.lesson7.server;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthService {

    public static String getNickByLoginAndPass(String login, String pass) throws SQLException {

        String query = String.format("SELECT nick FROM users where username = '%s' and password = '%s'", login, pass);
        ResultSet rs = DataService.getData(query);

        if (rs.next()) {
            return rs.getString(1);
        }

        return null;
    }
}
