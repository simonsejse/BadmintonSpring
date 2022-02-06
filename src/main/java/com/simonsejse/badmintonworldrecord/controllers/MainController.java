package com.simonsejse.badmintonworldrecord.controllers;


import com.simonsejse.badmintonworldrecord.management.StageManager;
import com.simonsejse.badmintonworldrecord.entities.Game;
import com.simonsejse.badmintonworldrecord.entities.Player;
import com.simonsejse.badmintonworldrecord.exceptions.GameNotFoundException;
import com.simonsejse.badmintonworldrecord.services.GameService;
import com.simonsejse.badmintonworldrecord.utilities.AlertUtil;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainController {

    private final AlertUtil alertUtil;
    private final GameService gameService;
    private final StageManager stageManager;


    @Autowired
    public MainController(final AlertUtil alertUtil, final GameService gameService, StageManager stageManager){
        this.alertUtil = alertUtil;
        this.gameService = gameService;
        this.stageManager = stageManager;
    }

    @FXML private TextField playerOneInput;
    @FXML private TextField playerTwoInput;
    @FXML private TextField loadByGameIdInput;


    public void startGame(){
        final String gameId = loadByGameIdInput.getText();
        final boolean gameIdEmpty = gameId.isEmpty();

        if (gameIdEmpty){
            final String playerName1 = playerOneInput.getText();
            final String playerName2 = playerTwoInput.getText();

            final Player player1 = new Player(playerName1);
            final Player player2 = new Player(playerName2);

            final Game newGame = this.gameService.createNewGame(player1, player2);
            stageManager.openGameWindow(newGame, playerOneInput.getScene().getWindow());
            this.alertUtil.newGameInit(newGame);
        }else {
            try {
                final Game gameById = this.gameService.getGameById(Long.parseLong(gameId));
                stageManager.openGameWindow(gameById, playerOneInput.getScene().getWindow());
                this.alertUtil.loadGameById(gameById);
            } catch (GameNotFoundException | NumberFormatException e) {
                if (e instanceof GameNotFoundException) this.alertUtil.gameDoesNotExist(gameId);
                if (e instanceof NumberFormatException) this.alertUtil.notANumber(gameId);
            }
        }
    }


}
