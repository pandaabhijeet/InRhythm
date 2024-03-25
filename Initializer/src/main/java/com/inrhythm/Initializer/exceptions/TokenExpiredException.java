package com.inrhythm.Initializer.exceptions;

public class TokenExpiredException extends RuntimeException{

    public TokenExpiredException(){
        super("Token has expired or is null. A new token must be fetched.");
    }
}
