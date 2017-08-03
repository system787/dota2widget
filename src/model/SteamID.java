package model;

/**
 * Created by vincenthoang on 8/2/17.
 */
public class SteamID {
    private String mID;
    private String mDisplayName;

    public SteamID(String mID, String mDisplayName) {
        this.mID = mID;
        this.mDisplayName = mDisplayName;
    }

    public String getmID() {
        return mID;
    }

    public void setmID(String mID) {
        this.mID = mID;
    }

    public String getmDisplayName() {
        return mDisplayName;
    }

    public void setmDisplayName(String mDisplayName) {
        this.mDisplayName = mDisplayName;
    }
}
