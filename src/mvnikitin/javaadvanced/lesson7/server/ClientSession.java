package mvnikitin.javaadvanced.lesson7.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class ClientSession {

    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private NetChatServer server;

    private String user;

    public String getUser() {
        return user;
    }

    public ClientSession(NetChatServer server, Socket socket) {
        try {
            this.socket = socket;
            this.server = server;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            user = "";

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            String firstMessage = in.readUTF();
                            if (firstMessage.startsWith("/auth")) {
                                String[] tokens = firstMessage.split(" ");
                                String newNick =
                                        AuthService.getNickByLoginAndPass(
                                                tokens[1], tokens[2]);

                                if (newNick == null) {
                                    sendMessage("Неверный логин/пароль!");
                                } else if (server.isOnline(newNick)) {
                                    sendMessage("Вы уже присоединись к чату.");
                                }
                                else {
                                    user = newNick;

                                    sendMessage("/authok");
                                    server.openClientSession(ClientSession.this);

                                    System.out.println("User " + user +
                                            " connected.");
                                    break;
                                }
                            }
                        }

                        while (true) {
                            String messageReceived = in.readUTF();

                            if (messageReceived.equals("/end")) {
                                out.writeUTF("/serverClosed");
                                System.out.println("User " + user +
                                        " disconnected.");
                                break;
                            }

                            if (messageReceived.startsWith("/w")) {
                                String[] privateMessagetokens =
                                        messageReceived.split(" ", 3);
                                String message = user + " to " +
                                        privateMessagetokens[1] + ": " +
                                        privateMessagetokens[2];

                                // ему
                                server.privateMessage(
                                        message, privateMessagetokens[1]);

                                // себе
                                sendMessage(message);

                            } else {
                                server.broadcastMessage(user + ": " +
                                        messageReceived);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
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

                        server.closeClientSession(ClientSession.this);
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
}
