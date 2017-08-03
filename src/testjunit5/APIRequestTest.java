package testjunit5;

import api.APIRequest;
import model.MatchID;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by vincenthoang on 8/2/17.
 */
class APIRequestTest {
    private static final String steamID = "76561197960380339";

    @Test
    void get64FromVanity() {
        APIRequest client = new APIRequest();
        System.out.println(client.get64FromVanity("system787"));
        System.out.println(client.get64FromVanity("aengker"));
    }

    @Test
    void getLatest25Matches() {
        APIRequest client = new APIRequest();
        ArrayList<MatchID> matchIDArrayList = client.getLatest25Matches(steamID);
        for (int i = 0, n = matchIDArrayList.size(); i < n; i++) {
            System.out.println(matchIDArrayList.get(i));
        }
    }

    @Test
    void getDisplayName() {
        APIRequest client = new APIRequest();
        System.out.println(client.getDisplayName(steamID));
    }
}