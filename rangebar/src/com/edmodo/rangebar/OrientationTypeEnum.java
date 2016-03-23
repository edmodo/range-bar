package com.edmodo.rangebar;

public enum OrientationTypeEnum {

    HORIZONTAL(0),
    VERTICAL(1);

    public int getCode() {
        return code;
    }

    private int code;

    OrientationTypeEnum(int mCode){
        this.code = mCode;
    }


    public static OrientationTypeEnum getEnum(int value) {
        for (OrientationTypeEnum v : values()){
            if(v.getCode() == value){
                return v;
            }
        }
        throw new IllegalArgumentException();
    }
}
