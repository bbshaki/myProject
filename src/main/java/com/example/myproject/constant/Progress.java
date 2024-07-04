package com.example.myproject.constant;

public enum Progress {

    EXPECT("예정"), PROCEED("진행중"), FINISH("종료");

    private String KrName;

    Progress(String krName){
        this.KrName = krName;
    }

    public String getKrName(){
        return KrName;
    }


}
