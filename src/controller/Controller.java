package controller;

import api.APIRequest;
import database.DBModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.MatchID;
import model.SteamID;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vincenthoang on 8/2/17.
 */
public final class Controller {
    private static Controller mController;

    private static final String DB_NAME = "dota2widget.db";

    private static final String STEAM_ID_TABLE = "steamid";
    private static final String STEAM_ID_FIELD_NAMES[] = {"id", "steamid", "displayname"};
    private static final String STEAM_ID_FIELD_TYPES[] = {"INTEGER PRIMARY KEY", "TEXT", "TEXT"};
    private DBModel mSteamID_DB;
    private ArrayList<SteamID> steamIDArrayList = new ArrayList<>();

    private static final String MATCH_ID_TABLE = "matchid";
    private static final String MATCH_ID_FIELD_NAMES[] = {"id", "matchid", "lobbytype"};
    private static final String MATCH_ID_FIELD_TYPES[] = {"INTEGER PRIMARY KEY", "TEXT", "TEXT"};
    private DBModel mMatchID_DB;
    private static ObservableList<MatchID> matchIDObservableList;

    private static final String ID_MATCH_TABLE = "id_match";
    private static final String ID_MATCH_FIELD_NAMES[] = {"steamid", "matchid"};
    private static final String ID_MATCH_FIELD_TYPES[] = {"text", "text"};
    private DBModel mIDMatch_DB;
    private static ArrayList<String[]> matchIDtable = new ArrayList<>();

    private Controller() {}

    public static Controller getInstance() {
        if (mController == null) {
            mController = new Controller();

            mController.matchIDObservableList = FXCollections.observableArrayList();

            try {
                // Steam IDs and display name
                mController.mSteamID_DB = new DBModel(DB_NAME, STEAM_ID_TABLE, STEAM_ID_FIELD_NAMES, STEAM_ID_FIELD_TYPES);
                ArrayList<ArrayList<String>> steamIDList = mController.mSteamID_DB.getEntry("all");
                for (ArrayList<String> values : steamIDList) {
                    String steamID = values.get(1);
                    String displayName = values.get(2);
                    mController.steamIDArrayList.add(new SteamID(steamID, displayName));
                }

                // Match IDs
                mController.mMatchID_DB = new DBModel(DB_NAME, MATCH_ID_TABLE, MATCH_ID_FIELD_NAMES, MATCH_ID_FIELD_TYPES);
                ArrayList<ArrayList<String>> matchIDList = mController.mMatchID_DB.getEntry("all");
                for (ArrayList<String> values : matchIDList) {
                    String matchID = values.get(0);
                    String lobbyType = values.get(1);
                    mController.matchIDObservableList.add(new MatchID(matchID, lobbyType));
                }

                // Relationship
                mController.mIDMatch_DB = new DBModel(DB_NAME, ID_MATCH_TABLE, ID_MATCH_FIELD_NAMES, ID_MATCH_FIELD_TYPES);
                ArrayList<ArrayList<String>> relationshipID = mController.mIDMatch_DB.getEntry("all");
                for (ArrayList<String> values : relationshipID) {
                    String[] id_pair = {values.get(0), values.get(1)};
                    matchIDtable.add(id_pair);
                }

            } catch (SQLException e) {
                Logger.getLogger(mController.getClass().getName()).log(Level.SEVERE, "SQLException when fetching tables", e);
                e.printStackTrace();
            }

        }

        return mController;
    }

    public static int addSteamID(SteamID steamId) {
        String[] values = new String[]{steamId.getmID(), steamId.getmDisplayName()};
        try {
            mController.mSteamID_DB.createEntry(Arrays.copyOfRange(STEAM_ID_FIELD_NAMES, 1, STEAM_ID_FIELD_NAMES.length), values);
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int retrieveLatest25Matches(String id64) {
        APIRequest client = new APIRequest();
        ArrayList<MatchID> temp = client.getLatest25Matches(id64);

        for (MatchID match : matchIDObservableList) {
            if (!matchIDObservableList.contains(match)) {
                matchIDObservableList.add(match);
            }
        }

        return 1;
    }

}
