package com.simonsejse.badmintonworldrecord.dtos;

import com.simonsejse.badmintonworldrecord.entities.GameSet;
import com.sun.istack.Nullable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameSetUpdateUiDTO {
    private int score1, score2;

    @Nullable
    private Integer setWon1, setWon2;

    @Nullable
    private Integer totalSets;

    public GameSetUpdateUiDTO(GameSet gameSet){
        this.score1 = gameSet.getPlayerOneScore();
        this.score2 = gameSet.getPlayerTwoScore();
        this.setWon1 = gameSet.getGame().getPlayerOne().getSetsWon();
        this.setWon2 = gameSet.getGame().getPlayerTwo().getSetsWon();
    }
    //In case the player doesn't win we don't need to update set
    public GameSetUpdateUiDTO(int score1, int score2){
        this.score1 = score1;
        this.score2 = score2;
    }

    public void setTotalSets(int size) {
        this.totalSets = size;
    }

    public Integer getTotalSets() {
        return this.totalSets;
    }
}
