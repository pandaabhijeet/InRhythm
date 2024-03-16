package com.inrhythm.Initializer.exceptions;

public class UserProfileException extends RuntimeException{

    public UserProfileException(String e){
        super("Exception occurred while fetching user profile details: " + e);
    }
}
