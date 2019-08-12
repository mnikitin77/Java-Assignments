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

    private BlackList blackList;

    public String getUser() {
        return user;
    }

    public boolean checkInBlackList(String nick) {
        return blackList.isInBlackList(nick);
    }

    public ClientSession(NetChatServer server, Socket socket) {
        try {
            this.socket = socket;
            this.server = server;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            user = "";
            blackList = null;

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
                                case "/reg":
                                    register(firstMessage);
                                    break;
                                    default:
                                        sendMessage("Incorrect command.");
                            }

                            if(isAuthenticated) {
                                blackList = new BlackList(user);
                                break;
                            }
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
                                case "/block":
                                    addToBlackList(messageReceived);
                                    break;
                                case "/unblock":
                                    removeFromBlackList(messageReceived);
                                    break;
                                default:
                                    server.broadcastMessage(user + ": " +
                                            messageReceived, user);
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

        return res;
    }

    private void register(String message) throws SQLException {

        String[] tokens = message.split(" ");

        if (RegService.register(tokens[1], tokens[2], tokens[3])) {
            sendMessage(tokens[1] + " (username " + tokens[2] +
                    ") successfully resistered.\n" +
                    "Welcome to the chat!");
        } else {
            sendMessage("The user with nick '" + tokens[1] + "' and/or username '" + tokens[2] +
                    "' was registered before.\n" +
                    "Please choose other nick and/or username");
        }
    }

    private void sendPrivateMessage(String message) {

        String[] tokens =
                message.split(" ", 3);
        String messageText = user + " to " +
                tokens[1] + ": " +
                tokens[2];

        // ему
        if (server.privateMessage(
                messageText, tokens[1], user)) {
            // себе
            sendMessage(messageText);
        } else {
            sendMessage("User " +
                    tokens[1] +
                    " is not in the chat.");
        }
    }

    private void addToBlackList(String message) {
        String[] tokens =
                message.split(" ", 2);

        if(blackList.isInBlackList(tokens[1])) {
            sendMessage(tokens[1] + " is already in the blacklist." );
            return;
        }

        if (blackList.addToBlackList(tokens[1])) {
            sendMessage(tokens[1] + " successfully added to the blacklist." );
        } else {
            sendMessage("Failed to add " + tokens[1] + " to the blacklist." );
        }
    }

    private void removeFromBlackList(String message) {
        String[] tokens =
                message.split(" ", 2);

        if (blackList.removeFromBlackList(tokens[1])) {
            sendMessage(tokens[1] +
                    " successfully removed from the blacklist.");
        } else {
            sendMessage("Failed to remove " +
                    tokens[1] + " from the blacklist.");
        }
    }
}
