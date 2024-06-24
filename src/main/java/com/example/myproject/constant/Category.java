package com.example.myproject.constant;

public enum Category {

    SEOUL("서울"), GYEONGGI("경기"), GANGWON("강원"), CHUNGNAM("충남"), CHUNGBUK("충북"),
    GYEONGNAM("경남"), GYEONGBUK("경북"), JEONNAM("전남"), JEONBUK("전북"), JEJU("제주"),
    BUSAN("부산"), DAEGU("대구"), DAEJEON("대전"), INCHEON("인천"), GWANGJU("광주"), ULSAN("울산"), SEJONG("세종");

    private String KrName;

    Category(String krName){
        this.KrName = krName;
    }

    public String getKrName(){
        return KrName;
    }

}
