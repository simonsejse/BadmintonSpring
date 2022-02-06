package com.simonsejse.badmintonworldrecord.entities;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="player")
public class Player {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "player_seq_gen"
    )
    @SequenceGenerator(
            name = "player_seq_gen",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "sets_won")
    private int setsWon;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL},
            mappedBy = "playerOne",
            orphanRemoval = true
    )
    private List<Game> games;

    protected Player(){}

    public Player(String name){
        this.name = name;
        this.games = new ArrayList<>();
    }

    public void incrementSetsWon() {
        this.setsWon++;
    }

    public void decrementSetsWon() {
        --this.setsWon;
    }
}
