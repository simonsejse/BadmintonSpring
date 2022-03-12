package com.simonsejse.badmintonworldrecord.repository;


import com.simonsejse.badmintonworldrecord.dtos.BallsUsedDTO;
import com.simonsejse.badmintonworldrecord.dtos.GameSetUpdateUiDTO;
import com.simonsejse.badmintonworldrecord.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;


@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    @Query("SELECT g FROM Game g LEFT JOIN FETCH g.playerOne LEFT JOIN FETCH g.playerTwo LEFT JOIN FETCH g.sets WHERE g.id = :gameId")
    Optional<Game> getGameById(Long gameId);

    @Transactional
    @Modifying
    @Query("UPDATE Game g SET g.ballsUsed = (g.ballsUsed + 1) WHERE g.id = :gameId")
    void increaseBallsUsedByGameId(long gameId);

    @Query("SELECT new com.simonsejse.badmintonworldrecord.dtos.BallsUsedDTO(g.ballsUsed) from Game g WHERE g.id =:gameId")
    BallsUsedDTO getBallsUsedDTOByGameId(long gameId);
}
