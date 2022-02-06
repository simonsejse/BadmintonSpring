package com.simonsejse.badmintonworldrecord.services;

import com.simonsejse.badmintonworldrecord.constants.PlayerType;
import com.simonsejse.badmintonworldrecord.controllers.GameController;
import com.simonsejse.badmintonworldrecord.dtos.GameSetUpdateUiDTO;
import com.simonsejse.badmintonworldrecord.entities.Game;
import com.simonsejse.badmintonworldrecord.entities.GameSet;
import com.simonsejse.badmintonworldrecord.entities.Player;
import com.simonsejse.badmintonworldrecord.exceptions.GameNotFoundException;
import com.simonsejse.badmintonworldrecord.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class GameService {

    private final GameRepository gameRepository;

    @Autowired
    public GameService(final GameRepository gameRepository){
        this.gameRepository = gameRepository;
    }

    public Game createNewGame(Player player1, Player player2){
        Game game = new Game(player1, player2);
        return this.gameRepository.save(game);
    }

    public Game getGameById(long id) throws GameNotFoundException{
        return gameRepository.getGameById(id)
                .orElseThrow(
                        () -> new GameNotFoundException(String.format("Spil-id %d findes ikke!", id))
                );
    }

    @Transactional
    public Game initGame(long currentGameId) throws GameNotFoundException {
        final Game game = this.gameRepository.getGameById(currentGameId)
                .orElseThrow(
                        () -> new GameNotFoundException(String.format("Spil-id %d findes ikke!", currentGameId))
                );
        final List<GameSet> sets = game.getSets();
        if (sets.isEmpty()) {
            final GameSet firstSet = new GameSet(game);
            sets.add(firstSet);
        }

        return game;
    }

    @Transactional
    public void incrementPlayerScoreByGameId(long gameId, PlayerType playerType, GameController gameController) throws GameNotFoundException {
        final Game game = getGameById(gameId);
        GameSet currentSet = game.getSets().get(game.getSets().size() - 1);

        currentSet.incrementScore(playerType);
        final boolean hasPlayerWon = currentSet.checkIfPlayerWon(playerType);

        GameSetUpdateUiDTO gameSetUpdateUiDTO = null;
        if (hasPlayerWon){
            //Setting winner
            currentSet.setWinnerAndIncrementPlayerWin(playerType);
            //Creating a new set so values go back to zero
            final GameSet newSet = new GameSet(game);
            game.initSet(newSet);

            gameSetUpdateUiDTO = new GameSetUpdateUiDTO(newSet);
            gameSetUpdateUiDTO.setTotalSets(game.getSets().size() - 1);
        }else gameSetUpdateUiDTO = new GameSetUpdateUiDTO(currentSet);

        gameController.updateScoreUI(gameSetUpdateUiDTO);
    }


    @Transactional
    public void revertPlayerScoreByGameId(long gameId, PlayerType type, GameController gameController) throws GameNotFoundException {
        final Game game = getGameById(gameId);
        final GameSet currentSet = game.getSets().get(game.getSets().size() - 1);

        currentSet.decrementScore(type);

    }

}
