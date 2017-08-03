package model;

/**
 * Created by vincenthoang on 8/2/17.
 */
public class DetailedMatchInfo {
    private int[] mHero;
    private int[] mKDA;
    private int[] mCS;
    private int[] mXGPM;
    private boolean mRadiantVictory;
    private String mPlayerSlot;

    public DetailedMatchInfo(int[] mHero, int[] mKDA, int[] mCS, int[] mXGPM, boolean mRadiantVictory, String mPlayerSlot) {
        this.mHero = mHero;
        this.mKDA = mKDA;
        this.mCS = mCS;
        this.mXGPM = mXGPM;
        this.mRadiantVictory = mRadiantVictory;
        this.mPlayerSlot = mPlayerSlot;
    }

    public int[] getmHero() {
        return mHero;
    }

    public int[] getmKDA() {
        return mKDA;
    }

    public int[] getmCS() {
        return mCS;
    }

    public int[] getmXGPM() {
        return mXGPM;
    }

    public boolean ismRadiantVictory() {
        return mRadiantVictory;
    }

    public String getmPlayerSlot() {
        return mPlayerSlot;
    }
}
