package mvnikitin.javaadvanced.lesson7.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NetChatServer {

    private Map<String, ClientSession> clientSessions;

    public NetChatServer() {
        clientSessions = new ConcurrentHashMap<>();
    }

    public static void main(String[] args) {
        try {
            new NetChatServer().run();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void run() throws SQLException {
        ServerSocket server = null;
        Socket socket = null;

        try {
            DataService.connect("org.sqlite.JDBC",
                    "jdbc:sqlite:src\\mvnikitin\\javaadvanced\\netchatusers");

            server = new ServerSocket(10050);
            System.out.println("Сервер запущен!");

            while (true) {
                socket = server.accept();
                new ClientSession(this, socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            DataService.disconnect();
        }
    };

    public void broadcastMessage(String message, String userFrom) {
        for (ClientSession c: clientSessions.values()) {
            if (!c.checkInBlackList(userFrom)) {
                c.sendMessage(message);
            }
        }
    }

    public boolean privateMessage (
            String message, String userTo, String userFrom) {
        boolean res = false;

        ClientSession c = clientSessions.get(userTo);
        if (c != null) {
            if(!c.checkInBlackList(userFrom)) {
                c.sendMessage(message);
            }
            res = true;
        }

        return res;
    }

    public void openClientSession (ClientSession session) {
            clientSessions.put(session.getUser(), session);
    }

    public void closeClientSession(ClientSession session) {
        clientSessions.remove(session.getUser());
    }

    public boolean isOnline(String userName) {
        return (clientSessions.get(userName) == null ? false : true);
    }
}
