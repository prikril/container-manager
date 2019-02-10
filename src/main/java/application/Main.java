package application;

import application.logging.LoggingFacade;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import org.greenrobot.eventbus.EventBus;
import ui.RootLayout;

public class Main extends Application {

    private LoggingFacade logger = LoggingFacade.getInstance();

    @Override
    public void start(Stage primaryStage) {
        //EventBus.getDefault().register(this);

        BorderPane root = new BorderPane();

        try {
            Scene scene = new Scene(root, 1000, 680);
            scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Container Manager");
            primaryStage.setResizable(true);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        root.setCenter(new RootLayout());

        GlobalConfiguration.getInstance().readFromFile("default.config");
    }

    public static void main(String[] args) {
        launch(args);
    }


}
