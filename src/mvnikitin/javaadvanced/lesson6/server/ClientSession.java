package mvnikitin.javaadvanced.lesson6.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientSession implements Comparable<ClientSession> {

    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private NetChatServer server;

    private String user;

    public ClientSession(NetChatServer server, Socket socket) {
        try {
            this.socket = socket;
            this.server = server;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            user = "";
            ClientSession me = this;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            String messageReceived = in.readUTF();

                            if (messageReceived.startsWith("/user:") &&
                                    user.equals("")) {
                                user = messageReceived.substring(
                                        "/user:".length());
                                System.out.println("User " + user +
                                        " connected.");
                            } else if (messageReceived.equals("/end")) {
                                out.writeUTF("/serverClosed");
                                System.out.println("User " + user +
                                        " disconnected.");
                                server.releaseClientSession(me);
                                break;
                            } else {
                                System.out.println(user + " " + messageReceived);
                                server.broadcastMessage(user + ": " +
                                        messageReceived);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int compareTo(ClientSession other) {
        return hashCode() - other.hashCode();
    }
}
