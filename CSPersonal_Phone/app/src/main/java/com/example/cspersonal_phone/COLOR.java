package com.example.cspersonal_phone;

public class COLOR {

    public static final int SEASON_SPRING = 0;
    public static final int SEASON_SUMMER = 1;
    public static final int SEASON_AUTUMN = 2;
    public static final int SEASON_WINTER = 3;
    private int COLOR_ID;
    private int COLOR_SEASON;
    private String COLOR_CODE;

    public int getCOLOR_ID() {
        return COLOR_ID;
    }

    public int getCOLOR_SEASON() {
        return COLOR_SEASON;
    }

    public String getCOLOR_CODE() {
        return COLOR_CODE;
    }

    public void setCOLOR_CODE(String COLOR_CODE) {
        this.COLOR_CODE = COLOR_CODE;
    }

    public void setCOLOR_SEASON(int COLOR_SEASON) {
        this.COLOR_SEASON = COLOR_SEASON;
    }

    public void setCOLOR_ID(int COLOR_ID) {
        this.COLOR_ID = COLOR_ID;
    }
}
