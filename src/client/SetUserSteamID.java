package client;/**
 * Created by vincenthoang on 8/2/17.
 */

import api.APIRequest;
import controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.SteamID;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SetUserSteamID {
    Controller mController = Controller.getInstance();
    APIRequest mClient = new APIRequest();

    @FXML
    private Button okButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField steamIdTF;

    @FXML
    private Label descriptionLabel;

    @FXML
    public Object setUserSteamID() {
        String steamId = steamIdTF.getText();

        if (steamId == null) {
            descriptionLabel.setText("Steam ID field must not be empty.");
        }

        try {
            long steamId64 = Long.parseLong(steamId);
            String displayName = mClient.getDisplayName(steamId);
            mController.addSteamID(new SteamID(steamId, displayName));
            return this;
        } catch (NumberFormatException e) {
            Logger.getLogger(getClass().getName()).log(Level.INFO, "SteamID is of type vanity. SteamID64 will be retrieved using vanity.");
        }

        String steamId64 = mClient.get64FromVanity(steamId);
        if (steamId64 != null) {
            String displayName = mClient.getDisplayName(steamId64);
            mController.addSteamID(new SteamID(steamId64, displayName));
            return this;
        }

    return null;
    }

    @FXML
    public Object cancel() {
        return this;
    }
}
