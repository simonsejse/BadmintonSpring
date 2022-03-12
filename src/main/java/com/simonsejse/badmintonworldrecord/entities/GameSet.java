package com.simonsejse.badmintonworldrecord.entities;

import com.simonsejse.badmintonworldrecord.constants.PlayerType;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class GameSet {
    @Id
    @Column(nullable = false)
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "gameset_seq_gen"
    )
    @SequenceGenerator(
            name = "gameset_seq_gen",
            allocationSize = 1
    )
    private Long gsid;

    @JoinColumn(name="which_set", unique = true)
    private int whichSet;

    @JoinColumn(name="game_id")
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private Game game;

    @JoinColumn(name="player_one_score")
    private int playerOneScore;

    @JoinColumn(name="player_two_score")
    private int playerTwoScore;

    @JoinColumn(name="is_set_finished")
    private boolean isSetFinished;

    @JoinColumn(name="winning_player")
    @ManyToOne(fetch = FetchType.LAZY)
    private Player winningPlayer;

    public GameSet(int whichSet){
        this.whichSet = whichSet;
        this.playerOneScore = 0;
        this.playerTwoScore = 0;
    }
    protected GameSet(){}

    public void setGame(Game game) {
        this.game = game;
    }

    public void incrementScore(PlayerType type){
        switch(type){
            case PLAYER1:
                ++this.playerOneScore;
                break;
            case PLAYER2:
                ++this.playerTwoScore;
                break;
        }
    }

    public void decrementScore(PlayerType type) {
        switch(type){
            case PLAYER1: --this.playerOneScore;break;
            case PLAYER2: --this.playerTwoScore;break;
        }
    }

    public boolean checkIfPlayerWon(PlayerType type) {
        return playerHasWon(type);
    }

    public void setWinnerAndIncrementPlayerWin(PlayerType playerType){
        this.isSetFinished = true;
        switch(playerType){
            case PLAYER1:
                this.getGame().getPlayerOne().incrementSetsWon();
                this.winningPlayer = this.getGame().getPlayerOne();
                break;
            case PLAYER2:
                this.getGame().getPlayerTwo().incrementSetsWon();
                this.winningPlayer = this.getGame().getPlayerTwo();
                break;
        }
    }


    private boolean playerHasWon(PlayerType type){
        switch(type){
            case PLAYER1: return (playerOneScore >= 21 && (playerOneScore - playerTwoScore >= 2));
            case PLAYER2: return (playerTwoScore >= 21 && (playerTwoScore - playerOneScore >= 2));
            default: return false;
        }
    }


    public boolean checkIfPlayerBelowZero(PlayerType type) {
        switch(type){
            case PLAYER1: return playerOneScore < 0;
            case PLAYER2: return playerTwoScore < 0;
            default: return false;
        }
    }

    public int getOtherScore(PlayerType type) {
        return type == PlayerType.PLAYER1 ? getPlayerTwoScore() : getPlayerOneScore();
    }
}
