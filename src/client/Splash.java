package client;

/**
 * Splash page used as a pre-loader for existing databases in the application
 *
 * Following a guide found on someone's github: https://gist.github.com/jewelsea/2305098
 */

import controller.Controller;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Splash extends Application {
    private Controller mController;

    private static final String SPLASH_IMAGE = "/assets/client/splash_sm.png";
    private static final String APPLICATION_ICON = "/assets/client/ico_64.png";
    private static final int SPLASH_WIDTH = 500;
    private static final int SPLASH_HEIGHT = 375;

    private Pane splashPane;
    private ImageView splashImage;
    private ProgressBar loadProgress;
    private Stage mainStage;


    public static void main(String[] args) {
        launch(args);
    }

    /**
     * init() executes before start()
     * Place javaFX code and pre-execution stuff here
     */
    @Override
    public void init() throws Exception {
        ImageView splash = new ImageView(new Image(SPLASH_IMAGE));

        loadProgress = new ProgressBar();
        loadProgress.setPrefWidth(SPLASH_WIDTH - 20);
        splashPane = new VBox();
        splashPane.getChildren().addAll(splash, loadProgress);
        splashPane.setStyle("-fx-padding: 5; " +
                "-fx-background-color: dimgrey; " +
                "-fx-border-width:5; " +
                "-fx-border-color: " +
                "linear-gradient(" +
                "to bottom, " +
                "chocolate, " +
                "derive(chocolate, 50%)" +
                ");"
        );
        splashPane.setEffect(new DropShadow());
    }

    /**
     * This space is used to execute any actions that would take longer than normal
     * Adjust the progress bar and fade time as necessary in showSplash() below
     */
    @Override
    public void start(final Stage primaryStage) throws Exception {

        // Setting stage for ViewNavigator to prevent NullPointerException
        ViewNavigator.setStage(primaryStage);

        final Task<Controller> initializeTask = new Task<Controller>() {
            @Override
            protected Controller call() throws InterruptedException {
                mController = Controller.getInstance();

                return mController;
            }
        };

        showSplash(primaryStage,
                initializeTask,
                () -> ViewNavigator.loadScene("Dota 2 Widget", ViewNavigator.HOME));
        new Thread(initializeTask).start();
    }


    public interface InitCompletionHandler {
        void complete();
    }

    /**
     *
     * @param initStage primaryStage passed through from start()
     * @param task The task to be completed before loading the next scene, defined in initializeTask()
     * @param initCompletionHandler Some interface magic. Don't know how it works.
     */
    private void showSplash(
            final Stage initStage,
            Task<?> task,
            InitCompletionHandler initCompletionHandler) {
        loadProgress.progressProperty().bind(task.progressProperty());
        task.stateProperty().addListener((observableValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                loadProgress.progressProperty().unbind();
                loadProgress.setProgress(1);
                initStage.toFront();
                FadeTransition fadeSplash = new FadeTransition(Duration.seconds(1.2), splashPane);
                fadeSplash.setFromValue(1.0);
                fadeSplash.setToValue(0.0);
                fadeSplash.setOnFinished(actionEvent -> initStage.hide());
                fadeSplash.play();

                initCompletionHandler.complete();
            }
        });

        Scene splashScene = new Scene(splashPane, Color.TRANSPARENT);
        final Rectangle2D bounds = Screen.getPrimary().getBounds();
        initStage.setScene(splashScene);
        initStage.setX(bounds.getMinX() + bounds.getWidth() / 2 - SPLASH_WIDTH / 2);
        initStage.setY(bounds.getMinY() + bounds.getHeight() / 2 - SPLASH_HEIGHT / 2);
        initStage.setAlwaysOnTop(true);
        initStage.show();
    }

}
