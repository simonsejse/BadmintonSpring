package com.simonsejse.badmintonworldrecord.controllers;

import com.simonsejse.badmintonworldrecord.dtos.GameSetUpdateUiDTO;
import com.simonsejse.badmintonworldrecord.exceptions.NoUndoHistoryFoundException;
import com.simonsejse.badmintonworldrecord.management.StageManager;
import com.simonsejse.badmintonworldrecord.constants.PlayerType;
import com.simonsejse.badmintonworldrecord.entities.Game;
import com.simonsejse.badmintonworldrecord.entities.GameSet;
import com.simonsejse.badmintonworldrecord.exceptions.GameNotFoundException;
import com.simonsejse.badmintonworldrecord.management.UndoManager;
import com.simonsejse.badmintonworldrecord.services.GameService;
import com.simonsejse.badmintonworldrecord.utilities.AlertUtil;
import com.simonsejse.badmintonworldrecord.utilities.DateUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

@Getter
@Setter
@EnableScheduling
@EnableAsync
@Component
public class GameController implements Initializable {

    private final StageManager stageManager;
    private final GameService gameService;
    private final AlertUtil alertUtil;
    private final UndoManager undoManager;


    @FXML AnchorPane ap;
    @FXML Label playerOneLabel;
    @FXML Label playerTwoLabel;
    @FXML Label playerOneSetWonLabel;
    @FXML Label playerTwoSetWonLabel;
    @FXML Label playerOneScoreLabel;
    @FXML Label playerTwoScoreLabel;
    @FXML Label timerLabel;
    @FXML Label totalSets;

    private long currentGameId;
    private LocalDateTime before;
    private boolean hasStarted;

    @Autowired
    public GameController(final StageManager stageManager, final GameService gameService, final AlertUtil alertUtil, final UndoManager undoManager) {
        this.stageManager = stageManager;
        this.gameService = gameService;
        this.alertUtil = alertUtil;
        this.undoManager = undoManager;
    }

    public void onKeyPressed(KeyEvent keyEvent) {
        System.out.println("hi");
        switch(keyEvent.getCode()){
            case J: incrementPlayerScore(PlayerType.PLAYER1); break;
            case M: incrementPlayerScore(PlayerType.PLAYER2);break;
            default: break;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            final Game game = gameService.initGame(currentGameId);
            this.before = game.getWhenStarted();
            this.hasStarted = !game.isDone();
            final int currentSet = game.getSets().size() - 1;
            this.playerOneLabel.setText(game.getPlayerOne().getName());
            this.playerTwoLabel.setText(game.getPlayerTwo().getName());
            final GameSet currentGameSet = game.getSets().get(currentSet);
            this.totalSets.setText(String.valueOf(currentGameSet.getWhichSet()));
            playerOneScoreLabel.setText(String.valueOf(currentGameSet.getPlayerOneScore()));
            playerOneSetWonLabel.setText(String.valueOf(currentGameSet.getGame().getPlayerOne().getSetsWon()));
            playerTwoScoreLabel.setText(String.valueOf(currentGameSet.getPlayerTwoScore()));
            playerTwoSetWonLabel.setText(String.valueOf(currentGameSet.getGame().getPlayerTwo().getSetsWon()));
            timerLabel.setText(DateUtil.calculateDifferenceBetweenDates(LocalDateTime.now(), game.getWhenStarted()));
        } catch (GameNotFoundException e) {
            e.printStackTrace();
            System.out.println("TODO: Explode program!");
        }
    }


    private void incrementPlayerScore(PlayerType type) {
        this.undoManager.addNextSelection(currentGameId, type);
        try {
            this.gameService.incrementPlayerScoreByGameId(currentGameId, type, this);
        } catch (GameNotFoundException e) {
            this.stageManager.openMainWindow(this.ap.getScene().getWindow());
        }

    }

    public void updateScoreUI(GameSetUpdateUiDTO gameSetUpdateUiDTO) {
        playerOneScoreLabel.setText(String.valueOf(gameSetUpdateUiDTO.getScore1()));
        playerTwoScoreLabel.setText(String.valueOf(gameSetUpdateUiDTO.getScore2()));
        playerOneSetWonLabel.setText(String.valueOf(gameSetUpdateUiDTO.getSetWon1()));
        playerTwoSetWonLabel.setText(String.valueOf(gameSetUpdateUiDTO.getSetWon2()));
        if (gameSetUpdateUiDTO.getTotalSets() != null) totalSets.setText(String.valueOf(gameSetUpdateUiDTO.getTotalSets()));
    }


    public void revertLastPointGiven(MouseEvent mouseEvent) {
        try {
            final PlayerType whichPlayerToRevert = undoManager.revertSelection(this.currentGameId);
            this.gameService.revertPlayerScoreByGameId(currentGameId, whichPlayerToRevert, this);
        } catch (NoUndoHistoryFoundException | GameNotFoundException e) {
            if (e instanceof NoUndoHistoryFoundException) this.alertUtil.noUndoHistoryFound();
            if (e instanceof GameNotFoundException) this.stageManager.openMainWindow(ap.getScene().getWindow());
        }

    }


    @Scheduled(fixedRate = 1000)
    public void setTimer(){
        if (hasStarted)
            Platform.runLater(() -> this.timerLabel.setText(DateUtil.calculateDifferenceBetweenDates(LocalDateTime.now(), before)));
    }
    /**
     * Need to be used in the xml file since we cant use parameters!
     */
    public void incrementPlayerOneBtn() {
        incrementPlayerScore(PlayerType.PLAYER1);
    }

    /**
     * Need to be used in the xml file since we cant use parameters!
     */
    public void incrementPlayerTwoBtn() {
        incrementPlayerScore(PlayerType.PLAYER2);
    }
}
