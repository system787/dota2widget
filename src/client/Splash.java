package client;

/**
 * Created by vincenthoang on 8/3/17.
 */

import controller.Controller;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Splash extends Application {
    Controller mController;

    public static final String SPLASH_IMAGE = "/assets/client/splash.png";

    @FXML
    private Pane splashPane;

    @FXML
    private ImageView splashImage;

    @FXML
    private ProgressBar loadProgress;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        mController = Controller.getInstance();
    }
    @Override
    public void start(Stage primaryStage) {

    }


}
