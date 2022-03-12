package com.simonsejse.badmintonworldrecord.services;

import com.simonsejse.badmintonworldrecord.constants.PlayerType;
import com.simonsejse.badmintonworldrecord.controllers.GameController;
import com.simonsejse.badmintonworldrecord.dtos.BallsUsedDTO;
import com.simonsejse.badmintonworldrecord.dtos.GameSetUpdateUiDTO;
import com.simonsejse.badmintonworldrecord.entities.Game;
import com.simonsejse.badmintonworldrecord.entities.GameSet;
import com.simonsejse.badmintonworldrecord.entities.Player;
import com.simonsejse.badmintonworldrecord.exceptions.GameNotFoundException;
import com.simonsejse.badmintonworldrecord.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Iterator;
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
        if (game.getSets().isEmpty()) {
            final GameSet firstSet = new GameSet(0);
            game.addSet(firstSet);
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
            final GameSet newSet = new GameSet(game.getSets().size());
            game.addSet(newSet);

            gameSetUpdateUiDTO = new GameSetUpdateUiDTO(newSet);
            gameSetUpdateUiDTO.setTotalSets(game.getSets().size() - 1);
        }else gameSetUpdateUiDTO = new GameSetUpdateUiDTO(currentSet.getPlayerOneScore(), currentSet.getPlayerTwoScore());

        gameController.updateScoreUI(gameSetUpdateUiDTO);
    }


    @Transactional
    public void revertPlayerScoreByGameId(long gameId, PlayerType type, GameController gameController) throws GameNotFoundException {
        final Game game = getGameById(gameId);
        final int currentSetIndex = game.getSets().size() - 1;
        final GameSet currentSet = game.getSets().get(currentSetIndex);

        //Decrement score
        //Check if player score of type is below zero if true:
            //Then delete current set since it no longer needs to exist
            //Then send back data from last set
        //else:
            //Then just send back current data
        currentSet.decrementScore(type);

        GameSetUpdateUiDTO gameSetUpdateUiDTO = null;

        if (currentSet.checkIfPlayerBelowZero(type)){
            game.removeSet(currentSet);

            final GameSet previousSet = game.getSets().get(currentSetIndex - 1);
            previousSet.decrementScore(type);

            if (type == PlayerType.PLAYER1) {
                game.getPlayerOne().decrementSetsWon();
            }else{
                game.getPlayerTwo().decrementSetsWon();
            }
            gameSetUpdateUiDTO = new GameSetUpdateUiDTO(previousSet);
            gameSetUpdateUiDTO.setTotalSets(game.getSets().size() - 1);
        }else {
            gameSetUpdateUiDTO = new GameSetUpdateUiDTO(currentSet.getPlayerOneScore(), currentSet.getPlayerTwoScore());
        }


        gameController.updateScoreUI(gameSetUpdateUiDTO);
    }

    public void increaseBallsUsedByGameId(long currentGameId, GameController gameController) {
        gameRepository.increaseBallsUsedByGameId(currentGameId);
        final BallsUsedDTO ballsUsedDTOByGameId = gameRepository.getBallsUsedDTOByGameId(currentGameId);
        //Update ball text
        gameController.updateBallsUsedUI(ballsUsedDTOByGameId);
    }
}
