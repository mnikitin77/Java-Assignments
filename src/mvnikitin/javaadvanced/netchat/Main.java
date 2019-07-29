package mvnikitin.netchat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("NetChat.fxml"));
        primaryStage.setTitle("-~=* Super Chat *=~-");
        primaryStage.setScene(new Scene(root, 640, 720));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
