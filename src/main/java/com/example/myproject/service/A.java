package com.example.myproject.service;

import java.util.UUID;

public class A {

    public static void main(String[] args) {
        String str = "1235452-qwsw4232-11asfffff";
        System.out.println(str.indexOf("-"));
        System.out.println(str.substring(0,str.indexOf("-")));
    }
}
