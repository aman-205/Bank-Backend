package com.aman.BankBackend.utils;

import java.time.Year;

public class Account {

    public  static final String Account_exist_code="001";
    public  static  final  String Account_exist_message="This user Already exist";

    public  static final String Account_creation_code="002";
    public  static final String Account_creation_message="User created";

    public  static final String Account_notExist_code="003";
    public  static final String Account_notExist_message="No user found";

    public  static final String Account_Found_code="004";
    public  static final String Account_Found_message="User exists";

    public  static final String Account_credit_code="005";
    public  static final String Account_credit_message="Amount Credited";

    public  static final String Account_insufficient_code="006";
    public  static final String Account_insufficient_message="Account Balance is insufficient";

    public  static final String Account_debit_code="007";
    public  static final String Account_debit_message="Account Debited";

    public  static final String Account_noReceiver_code="008";
    public  static final String Account_noReceiver_message="Receiver not exist";

    public  static final String Account_transferred_code="009";
    public  static final String Account_transferred_message ="Amount transferred successfully";



    public static String generate(){


        Year currentYear= Year.now();

        int min=100000;
        int max=999999;

        int random= (int) Math.floor((Math.random()*(max-min+1)));

        return String.valueOf(currentYear)+String.valueOf(random);
    }
}
