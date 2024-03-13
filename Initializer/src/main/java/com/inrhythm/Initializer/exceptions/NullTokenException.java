package com.inrhythm.Initializer.exceptions;

public class NullTokenException extends NullPointerException{

    public NullTokenException(String source){
        super("Null or invalid value received while trying to fetch " + source + " access token !");
    }
}
