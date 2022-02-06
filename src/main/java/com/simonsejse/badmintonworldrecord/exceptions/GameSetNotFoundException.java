package com.simonsejse.badmintonworldrecord.exceptions;

public class GameSetNotFoundException extends Exception {
    public GameSetNotFoundException(String message) {
        super(message);
    }
    public GameSetNotFoundException(long gameId){
        super(String.valueOf(gameId));
    }
}
