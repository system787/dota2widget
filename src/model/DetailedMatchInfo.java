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

    /**
     *
     * @param mHero int[] with the format { heroNumber, level }
     * @param mKDA int[] with the format { kill, death, assists }
     * @param mCS int[] with the format { lastHits, denies }
     * @param mXGPM int[] with the format { GPM, XPM }
     * @param mRadiantVictory boolean true for radiant victory, false for dire
     * @param mPlayerSlot 8-bit unsigned integer in a special format to denote player's team and player slot position within the team
     *                    See https://wiki.teamfortress.com/wiki/WebAPI/GetMatchDetails#Player_Slot
     */
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
