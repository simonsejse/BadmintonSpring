package com.simonsejse.badmintonworldrecord.repository;


import com.simonsejse.badmintonworldrecord.dtos.GameSetUpdateUiDTO;
import com.simonsejse.badmintonworldrecord.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    @Query("SELECT g FROM Game g LEFT JOIN FETCH g.playerOne LEFT JOIN FETCH g.playerTwo LEFT JOIN FETCH g.sets WHERE g.id = :gameId")
    Optional<Game> getGameById(Long gameId);

    @Query("SELECT new com.simonsejse.badmintonworldrecord.dtos.GameSetUpdateUiDTO(gs) from GameSet gs JOIN gs.game g JOIN g.playerOne p1 JOIN g.playerTwo p2 WHERE g.id = :gameId AND gs.whichSet = size(g.sets) - 1")
    GameSetUpdateUiDTO getGameSetUpdateUiDTOByGameId(long gameId);



}
