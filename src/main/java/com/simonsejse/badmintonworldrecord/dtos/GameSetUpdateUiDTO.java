package com.simonsejse.badmintonworldrecord.dtos;

import com.simonsejse.badmintonworldrecord.entities.GameSet;
import com.sun.istack.Nullable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameSetUpdateUiDTO {
    private int score1, score2;
    private int setWon1, setWon2;
    @Nullable
    private Integer totalSets;
    public GameSetUpdateUiDTO(GameSet gameSet){
        this.score1 = gameSet.getPlayerOneScore();
        this.score2 = gameSet.getPlayerTwoScore();
        this.setWon1 = gameSet.getGame().getPlayerOne().getSetsWon();
        this.setWon2 = gameSet.getGame().getPlayerTwo().getSetsWon();
    }

    public void setTotalSets(int size) {
        this.totalSets = size;
    }

    public Integer getTotalSets() {
        return this.totalSets;
    }
}
