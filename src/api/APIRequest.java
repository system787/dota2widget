package api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.MatchID;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vincenthoang on 8/2/17.
 */
public class APIRequest {
    private static final String API_KEY = APIKey.getAPIKey();
    private static final String API_DOMAIN = "systemdota";
    private static final int REQUEST_TIMEOUT = 15000;

    public APIRequest() {}

    public String getDisplayName(String steamId64) {
        String url = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=" + API_KEY + "&steamids=" + steamId64;
        try {
            String json = getJSON(url, REQUEST_TIMEOUT);
            JSONObject response = new JSONObject(json).getJSONObject("response");
            if (response == null) {
                return null;
            }
            String jsonString = response.get("players").toString();
            String jsonSubstring = jsonString.substring(jsonString.indexOf("\"personaname\":"));
            return jsonSubstring.substring(jsonSubstring.indexOf("personaname:\"") + 16, jsonSubstring.indexOf(",") - 1);

        } catch (JSONException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "JSON exception in retrieving user's display name");
            e.printStackTrace();
        }
        return null;
    }

    public String get64FromVanity(String vanity) {
        String url = "http://api.steampowered.com/ISteamUser/ResolveVanityURL/v0001/?key=" + API_KEY + "&vanityurl=" + vanity.trim();
        try {
            String json = getJSON(url, REQUEST_TIMEOUT);
            JSONObject response = new JSONObject(json).getJSONObject("response");
            if (response == null) {
                return null;
            }
            return response.getString("steamid");
        } catch (JSONException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "JSON exception in retrieving user's 64bit SteamId", e);
        }
        return null;
    }

    public String convert64to32(String steamId64) {
        return String.valueOf(Long.parseLong(steamId64) - 76561197960265728L);
    }

    public ArrayList<MatchID> getLatest25Matches(String id64) {
        String id32 = convert64to32(id64);
        String url = "https://api.steampowered.com/IDOTA2Match_570/GetMatchHistory/V001/?account_id=" + id32 +  "&matches_requested=25&key=" + API_KEY;

        try {
            String json = getJSON(url, REQUEST_TIMEOUT);
            JSONObject response = new JSONObject(json).getJSONObject("result");
            if (response == null) {
                return null;
            }

            JSONArray jsonArray = response.getJSONArray("matches");
            ArrayList<MatchID> matchIDArrayList = new ArrayList<>();

            for (int i = 0, n = jsonArray.length(); i < n; i++) {
                String matchString = jsonArray.getJSONObject(i).toString();

                String matchID = matchString.substring(
                        matchString.indexOf("\"match_id\":") + 11, matchString.indexOf("\"match_id\":") + 21);

                String lobbyType = matchString.substring(
                        matchString.indexOf("\"lobby_type\":") + 13, matchString.indexOf("\"lobby_type\":") + 14);

                MatchID match = new MatchID(matchID, lobbyType);
                matchIDArrayList.add(match);
            }
            return matchIDArrayList;
        } catch (JSONException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "JSON exception in retreiving the user's last 25 matches", e);
        }
        return null;
    }

    public String getJSON(String url, int timeout) throws JSONException {
        HttpURLConnection c = null;
        try {
            URL u = new URL(url);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(timeout);
            c.setReadTimeout(timeout);
            c.connect();
            int status = c.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    return sb.toString();
            }
        } catch (MalformedURLException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "MalformedURLException caught", e);
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "IOException caught", e);
        } finally {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception e) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Unexpected Exception thrown", e);
                }
            }
        }
        return null;
    }
}
