package com.example.b10715.final_pj;

public class Config {
    //URL to our login.php file
    public static final String URL = "http://223.194.154.223/";

    //Keys for email and password as defined in our $_POST['key'] in login.php
    public static final String KEY_EMAIL = "user_email";
    public static final String KEY_PASSWORD = "password";

    //If server response is equal to this that means login is successful
    public static final String LOGIN_SUCCESS = "success";
    public static final String LOGIN_FAILED = "failure";

    //Keys for Sharedpreferences
    //This would be the name of our shared preferences
    public static final String SHARED_PREF_NAME = "myloginapp";
    public static String user_img=null;
    public static String pet_img=null;

    //This would be used to store the email of current logged in user
    public static final String EMAIL_SHARED_PREF = "user_email";

    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";

}