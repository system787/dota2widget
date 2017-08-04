package client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vincenthoang on 8/3/17.
 */
public class ViewNavigator {
    public static final String SPLASH = "Splash.fxml";
    public static final String HOME = "Home.fxml";

    public static final String SETUSERSTEAMID = "SetUserSteamID.fxml";

    public static Stage mainStage;

    public static void setStage(Stage stage) {
        mainStage = stage;
    }

    public static void loadScene(String title, String sceneFXML) {
        try {
            mainStage.setTitle(title);
            Scene scene = new Scene(FXMLLoader.load(ViewNavigator.class.getResource(sceneFXML)));
            mainStage.setScene(scene);
            mainStage.show();
        } catch (IOException e) {
            Logger.getLogger("ViewNavigator").log(Level.SEVERE, "Loading new scene failed: " + title + ", " + sceneFXML, e);
            e.printStackTrace();
        }
    }
}
