package mvnikitin.javaadvanced.lesson6.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class NetChatServer {

    private Set<ClientSession> clientSessions;

    public NetChatServer() {
        clientSessions = new ConcurrentSkipListSet<>();
    }

    public static void main(String[] args) {
        new NetChatServer().run();
    }

    public void run() {
        ServerSocket server = null;
        Socket socket = null;

        try {
            server = new ServerSocket(10050);
            System.out.println("Сервер запущен!");

            while (true) {
                socket = server.accept();
                clientSessions.add(new ClientSession(this, socket));
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
        }
    };

    public void broadcastMessage(String message) {
        for (ClientSession c: clientSessions) {
            c.sendMessage(message);
        }
    }

    public void releaseClientSession(ClientSession session) {
        clientSessions.remove(session);
    }
}
