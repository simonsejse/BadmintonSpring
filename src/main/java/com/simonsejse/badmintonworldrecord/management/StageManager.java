package com.simonsejse.badmintonworldrecord.management;

import com.simonsejse.badmintonworldrecord.controllers.GameController;
import com.simonsejse.badmintonworldrecord.entities.Game;
import com.simonsejse.badmintonworldrecord.utilities.AlertUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StageManager {

    private final ConfigurableApplicationContext springContext;
    private final AlertUtil alertUtil;

    @Autowired
    public StageManager(ConfigurableApplicationContext springContext, AlertUtil alertUtil) {
        this.springContext = springContext;
        this.alertUtil = alertUtil;
    }



    public void openGameWindow(Game newGame, Window window) {
        final Stage stage = new Stage();
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/game.fxml"));
        final GameController gameController = springContext.getBean(GameController.class);
        fxmlLoader.setControllerFactory(controller -> {
            gameController.setCurrentGameId(newGame.getId());
            return gameController;
        });
        try {
            final Parent parent = fxmlLoader.load();
            parent.setOnKeyPressed(gameController::onKeyPressed);
            final Scene scene = new Scene(parent, 514, 277);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.setTitle(String.format("Spil %d", newGame.getId()));
            window.hide();
            stage.show();
        } catch (IOException exception) {
            exception.printStackTrace();
            this.alertUtil.cannotOpenGameWindow();
        }
    }


    public void openMainWindow(Window currentWindow){
        Stage stage = new Stage();
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        try {
            final Parent parent = fxmlLoader.load();
            stage.setResizable(false);
            stage.setTitle("Badminton-Verdensrekord");
            stage.setScene(new Scene(parent, 600, 405));
            currentWindow.hide();
            stage.show();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
