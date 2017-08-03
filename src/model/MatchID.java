package model;

/**
 * Created by vincenthoang on 8/2/17.
 */
public class MatchID {
    private String mMatchID;
    private String mLobbyType;

    public MatchID(String mMatchID, String mLobbyType) {
        this.mMatchID = mMatchID;
        this.mLobbyType = mLobbyType;
    }

    public String getmMatchID() {
        return mMatchID;
    }

    public String getmLobbyType() {
        return mLobbyType;
    }

    @Override
    public String toString() {
        return getmMatchID() + " " + getmLobbyType();
    }
}
