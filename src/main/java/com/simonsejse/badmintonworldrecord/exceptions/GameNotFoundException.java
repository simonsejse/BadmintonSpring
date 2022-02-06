package com.simonsejse.badmintonworldrecord.exceptions;

public class GameNotFoundException extends Exception{
    public GameNotFoundException(String msg) {
        super(msg);
    }

    public GameNotFoundException(long gameId) {
        super(String.valueOf(gameId));
    }
}
