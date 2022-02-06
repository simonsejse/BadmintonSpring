package com.simonsejse.badmintonworldrecord.entities;

import jdk.jfr.Timestamp;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
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
            fetch = FetchType.LAZY,
            mappedBy = "game",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<GameSet> sets;

    @Column(name = "game_is_done")
    private boolean done;

    protected Game(){}

    public Game(final Player playerOne, final Player playerTwo){
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.whenStarted = LocalDateTime.now();
        this.done = false;
    }

    public void initSet(GameSet newSet) {
        this.sets.add(newSet);
    }
}
