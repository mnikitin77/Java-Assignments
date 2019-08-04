package mvnikitin.javaadvanced.lesson6.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {
    @FXML
    TextArea textArea;

    @FXML
    TextArea messageTextArea;

    @FXML
    Button btn1;

    Socket socket;
    DataInputStream in;
    DataOutputStream out;

    final String IP_ADRESS = "localhost";
    final int PORT = 10050;

    //final String USER = "Вася";
    final String USER = "Петя";

    public void sendMsg() {
        String textToSend = messageTextArea.getText();

        if (!textToSend.isEmpty()) {
            try {
                out.writeUTF(textToSend);
                messageTextArea.clear();
                messageTextArea.requestFocus();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @FXML
    public void buttonPressed(KeyEvent e)
    {
        if(e.isShiftDown() && e.getCode().toString().equals("ENTER"))
            sendMsg();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            socket = new Socket(IP_ADRESS, PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        out.writeUTF("/user:" + USER);

                        while (true) {
                            String messageReceived = in.readUTF();
                            textArea.appendText(messageReceived + "\n");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
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
}
