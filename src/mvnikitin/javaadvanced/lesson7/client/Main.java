package mvnikitin.javaadvanced.lesson7.client;


import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("NetChat.fxml"));
        Parent root = loader.load();
        //Parent root = FXMLLoader.load(getClass().getResource("NetChat.fxml"));
        primaryStage.setTitle("-~=* Super Chat *=~-");
        primaryStage.setScene(new Scene(root, 640, 720));

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                ((Controller)loader.getController()).disconnect();
            }
        });

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
