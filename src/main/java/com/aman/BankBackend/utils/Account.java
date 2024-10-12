package com.aman.BankBackend.utils;

import java.time.Year;

public class Account {

    public  static final String Account_exist_code="001";
    public  static  final  String Account_exist_message="This user Already exist";

    public  static final String Account_creation_code="002";
    public  static final String Account_creation_message="User created";

    public static String generate(){
        Year currentYear= Year.now();

        int min=100000;
        int max=999999;

        int random= (int) Math.floor((Math.random()*(max-min+1)));

        return String.valueOf(currentYear)+String.valueOf(random);
    }
}
