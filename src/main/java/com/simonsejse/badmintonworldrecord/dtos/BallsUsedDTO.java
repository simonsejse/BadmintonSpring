package com.simonsejse.badmintonworldrecord.dtos;

public class BallsUsedDTO {
    private int ballsUsed;
    public BallsUsedDTO(int ballsUsed){
        this.ballsUsed = ballsUsed;
    }

    public void setBallsUsed(int ballsUsed) {
        this.ballsUsed = ballsUsed;
    }

    public int getBallsUsed() {
        return ballsUsed;
    }
}
