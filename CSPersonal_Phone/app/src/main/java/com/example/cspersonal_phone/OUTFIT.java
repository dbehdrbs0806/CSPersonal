package com.example.cspersonal_phone;

// OUTFIT 클래스: 옷차림(OUTFIT)을 나타내는 클래스
public class OUTFIT {

    // 상의 유형
    public static final int TYPE_TOP = 0;
    // 하의 유형
    public static final int TYPE_BOTTOM = 1;

    // 옷차림 ID
    private int OUTFIT_ID;
    // 옷차림 유형 (상의 또는 하의)
    private int OUTFIT_TYPE;
    // 옷의 ID
    private int CLOTHES;
    // 색상 정보
    private String COLOR;
    // 계절 정보
    private int SEASON;
    // 옷차림 코드
    private String OUTFIT_CODE;

    // OUTFIT_ID의 Getter와 Setter
    public int getOUTFIT_ID() { return OUTFIT_ID; }
    public void setOUTFIT_ID(int OUTFIT_ID) { this.OUTFIT_ID = OUTFIT_ID; }

    // OUTFIT_TYPE의 Getter와 Setter
    public int getOUTFIT_TYPE() { return OUTFIT_TYPE; }
    public void setOUTFIT_TYPE(int OUTFIT_TYPE) { this.OUTFIT_TYPE = OUTFIT_TYPE; }

    // CLOTHES의 Getter와 Setter
    public int getCLOTHES() { return CLOTHES; }
    public void setCLOTHES(int CLOTHES) { this.CLOTHES = CLOTHES; }

    // COLOR의 Getter와 Setter
    public String getCOLOR() { return COLOR; }
    public void setCOLOR(String COLOR) { this.COLOR = COLOR; }

    // SEASON의 Getter와 Setter
    public int getSEASON() { return SEASON; }
    public void setSEASON(int SEASON) { this.SEASON = SEASON; }

    // OUTFIT_CODE의 Getter와 Setter
    public String getOUTFIT_CODE() { return OUTFIT_CODE; }
    public void setOUTFIT_CODE(String OUTFIT_CODE) { this.OUTFIT_CODE = OUTFIT_CODE; }
}
