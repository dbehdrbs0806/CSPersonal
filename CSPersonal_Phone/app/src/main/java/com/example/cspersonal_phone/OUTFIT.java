package com.example.cspersonal_phone;

public class OUTFIT {

    public static final int TYPE_TOP = 0;
    public static final int TYPE_BOTTOM = 1;

    private int OUTFIT_ID;
    private int OUTFIT_TYPE;
    private String OUTFIT_CODE;

    public int getOUTFIT_ID() { return OUTFIT_ID; }

    public int getOUTFIT_TYPE() {
        return OUTFIT_TYPE;
    }

    public String getOUTFIT_CODE() {
        return OUTFIT_CODE;
    }

    public void setOUTFIT_CODE(String OUTFIT_CODE) {
        this.OUTFIT_CODE = OUTFIT_CODE;
    }

    public void setOUTFIT_TYPE(int OUTFIT_TYPE) {
        this.OUTFIT_TYPE = OUTFIT_TYPE;
    }

    public void setOUTFIT_ID(int OUTFIT_ID) {
        this.OUTFIT_ID = OUTFIT_ID;
    }

}
