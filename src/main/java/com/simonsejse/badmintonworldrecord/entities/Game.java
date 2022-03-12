package com.simonsejse.badmintonworldrecord.entities;

import com.simonsejse.badmintonworldrecord.constants.PlayerType;
import jdk.jfr.Timestamp;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="game")
public class Game {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "game_seq_gen"
    )
    @SequenceGenerator(
            name = "game_seq_gen",
            allocationSize = 1
    )
    private long id;

    @JoinColumn(name = "balls_used")
    private int ballsUsed;

    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade=CascadeType.ALL
    )
    @JoinColumn(
            name = "player_one_id"
    )
    private Player playerOne;

    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade=CascadeType.ALL
    )
    @JoinColumn(
            name = "player_two_id"
    )
    private Player playerTwo;

    @Timestamp()
    @JoinColumn(name = "when_started")
    private LocalDateTime whenStarted;

    //Fandt 22:00
    @OneToMany(
            mappedBy = "game",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<GameSet> sets = new ArrayList<>();

    public Game addSet(GameSet gameSet){
        sets.add(gameSet);
        gameSet.setGame(this);
        return this;
    }

    public Game removeSet(GameSet gameSet){
        sets.remove(gameSet);
        gameSet.setGame(null);
        return this;
    }

    @Column(name = "game_is_done")
    private boolean done;

    protected Game(){}

    public Game(final Player playerOne, final Player playerTwo){
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.whenStarted = LocalDateTime.now();
        this.done = false;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void incrementBallsUsed(){
        ballsUsed = ballsUsed + 1;
    }
    public int getBallsUsed() {
        return ballsUsed;
    }

    public void setBallsUsed(int ballsUsed) {
        this.ballsUsed = ballsUsed;
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(Player playerOne) {
        this.playerOne = playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(Player playerTwo) {
        this.playerTwo = playerTwo;
    }

    public LocalDateTime getWhenStarted() {
        return whenStarted;
    }

    public void setWhenStarted(LocalDateTime whenStarted) {
        this.whenStarted = whenStarted;
    }

    public List<GameSet> getSets() {
        return sets;
    }

    public void setSets(List<GameSet> sets) {
        this.sets = sets;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
