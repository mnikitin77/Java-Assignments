package mvnikitin.javaadvanced.lesson8.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Controller implements Initializable {
    @FXML
    TextArea textArea;

    @FXML
    TextArea messageTextArea;

    @FXML
    Button btn1;

    @FXML
    HBox bottomPanel;

    @FXML
    HBox upperPanel;

    @FXML
    HBox regPanel;

    @FXML
    TextField loginfield;

    @FXML
    PasswordField passwordfiled;

    @FXML
    TextField regnick;

    @FXML
    TextField reglogin;

    @FXML
    PasswordField regpwd1;

    @FXML
    PasswordField regpwd2;

    @FXML
    Button regbtn;

    @FXML
    Button backbtn;


    private boolean isAuthorized;

    final String IP_ADRESS = "localhost";
    final int PORT = 10050;

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public void setAuthorized(boolean isAuthorized) {
        this.isAuthorized = isAuthorized;
        if (!isAuthorized) {
            upperPanel.setVisible(true);
            upperPanel.setManaged(true);
            bottomPanel.setVisible(false);
            bottomPanel.setManaged(false);
        } else {
            upperPanel.setVisible(false);
            upperPanel.setManaged(false);
            bottomPanel.setVisible(true);
            bottomPanel.setManaged(true);
        }
    }

    public void sendMessage() {
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
            sendMessage();
    }

    public void connect() {
        try {
            socket = new Socket(IP_ADRESS, PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        while (true) {
                            String str = in.readUTF();
                            if (str.startsWith("/authok")) {
                                setAuthorized(true);
                                break;
                            } else {
                                textArea.appendText(str + "\n");
                            }
                        }

                        while (true) {
                            String messageReceived = in.readUTF();
                            if(messageReceived.equals("/serverClosed")) {
                                setAuthorized(false);
                                break;
                            }
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

    @FXML
    public void authorize() {
        if (socket == null || socket.isClosed()) {
            connect();
        }

        try {
            out.writeUTF("/auth " + loginfield.getText() + " " + passwordfiled.getText());
            loginfield.clear();
            passwordfiled.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void disconnect() {
        if(out != null) {
            try {
                out.writeUTF("/end");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void regster() {
        if (socket == null || socket.isClosed()) {
            connect();
        }

        if (checkRegForm()) {
            try {

                out.writeUTF("/reg " + regnick.getText() + " " +
                        reglogin.getText() + " " + regpwd2.getText());
                hideRegPanel();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hideRegPanel();
    }

    @FXML
    public void showRegPanel() {
        upperPanel.setVisible(false);
        upperPanel.setManaged(false);
        regPanel.setVisible(true);
        regPanel.setManaged(true);
    }

    @FXML
    public void hideRegPanel() {
        regPanel.setVisible(false);
        regPanel.setManaged(false);
        upperPanel.setVisible(true);
        upperPanel.setManaged(true);

        regnick.clear();
        reglogin.clear();
        regpwd1.clear();
        regpwd2.clear();

        loginfield.requestFocus();
    }

    private boolean checkRegForm () {
        boolean res = false;

        if (regnick.getText().isEmpty())
            textArea.appendText("Please fill in the nickname.\n");
        else if (reglogin.getText().isEmpty())
            textArea.appendText("Please fill in the username.\n");
        else if (regpwd1.getText().isEmpty())
            textArea.appendText("Please fill in the password.\n");
        else if (regpwd2.getText().isEmpty())
            textArea.appendText("Please repeat the password.\n");
        else if (!regpwd1.getText().equals(regpwd2.getText()))
            textArea.appendText("Please fill in the same password two times.\n");
        else if (!checkPasswordStrengh(regpwd2.getText())) {
            textArea.appendText("The password is weak. " +
                    "A password must contain at least one capital [A-Z], " +
                    "one digit [0-9], one small letter [a-z], " +
                    "one special symbol [@#$%^&+=]. " +
                    "It also must have at least 8 symbols but not more that 20.\n");
        } else res = true;

        return res;
    }

    private boolean checkPasswordStrengh(String s){
        Pattern p = Pattern.compile("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}");
        Matcher m = p.matcher(s);
        if(m.matches()){
            return true;
        }
        return false;
    }
}
