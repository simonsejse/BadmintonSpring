package com.simonsejse.badmintonworldrecord.repository;

import com.simonsejse.badmintonworldrecord.constants.PlayerType;
import com.simonsejse.badmintonworldrecord.dtos.GameSetUpdateUiDTO;
import com.simonsejse.badmintonworldrecord.entities.Game;
import com.simonsejse.badmintonworldrecord.entities.GameSet;
import org.hibernate.annotations.OnDelete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface GameSetRepository extends JpaRepository<GameSet, Long> {
    @Query("SELECT s FROM GameSet s LEFT JOIN FETCH s.game g LEFT JOIN s.game.playerOne p1 LEFT JOIN s.game.playerOne p2 WHERE g.id = :gameId AND s.whichSet = size(g.sets) - 1")
    Optional<GameSet> getLastGameSetByGameId(long gameId);

    @Transactional
    @Modifying
    @Query("delete from GameSet gs where gs.whichSet = :whichSet and gs.game.id = :game_id")
    void deleteGameSetByGameId(long game_id, int whichSet);

    @Transactional
    @Modifying
    @Query("UPDATE GameSet gs set gs.isSetFinished = false, gs.winningPlayer = null WHERE gs.whichSet = :whichSet AND exists (SELECT g from Game g WHERE g.id = :gameId)")
    void setGameSetToNotFinishedByGameId(long gameId, int whichSet);
}

