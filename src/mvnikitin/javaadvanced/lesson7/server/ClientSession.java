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
                        // Цикл обработки начальных команд чата.
                        while (true) {
                            String firstMessage = in.readUTF();
                            String[] commandString =
                                    firstMessage.split(" ", 2);

                            boolean isAuthenticated = false;

                            switch (commandString[0]) {
                                case "/auth":
                                    isAuthenticated =
                                            authenticate(firstMessage);
                                    break;
                                case "/reg"://TODO
                                    break;
                                    default:
                                        sendMessage("Incorrect command.");
                            }

                            if(isAuthenticated)
                                break;
                        }

                        // Цикл обработки сообщений чата.
                        while (true) {
                            String messageReceived = in.readUTF();
                            String[] commandString =
                                    messageReceived.split(" ", 2);

                            boolean isLogedOff = false;

                            switch (commandString[0]) {
                                case "/end":
                                    isLogedOff = true;
                                    out.writeUTF("/serverClosed");
                                    System.out.println("User " + user +
                                            " disconnected.");
                                    break;
                                case "/w":
                                    sendPrivateMessage(messageReceived);
                                    break;
                                default:
                                    server.broadcastMessage(user + ": " +
                                            messageReceived);
                            }

                            if(isLogedOff)
                                break;
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


    private boolean authenticate(String message) throws SQLException {

        boolean res = false;

        if (message.startsWith("/auth")) {
            String[] tokens = message.split(" ");
            String newNick =
                    AuthService.getNickByLoginAndPass(
                            tokens[1], tokens[2]);

            if (newNick == null) {
                sendMessage("Wrong username or password.");
            } else if (server.isOnline(newNick)) {
                sendMessage("You are already in the chat.");
            }
            else {
                user = newNick;
                res = true;

                sendMessage("/authok");
                server.openClientSession(ClientSession.this);

                System.out.println("User " + user +
                        " connected.");
            }
        }

        return res;
    }


    private void sendPrivateMessage(String message) {

            String[] privateMessagetokens =
                    message.split(" ", 3);
            String messageText = user + " to " +
                    privateMessagetokens[1] + ": " +
                    privateMessagetokens[2];

            // ему
            if (server.privateMessage(
                    messageText, privateMessagetokens[1])) {
                // себе
                sendMessage(messageText);
            } else {
                sendMessage("User " +
                        privateMessagetokens[1] +
                        " is not in the chat");
            }
    }

}
