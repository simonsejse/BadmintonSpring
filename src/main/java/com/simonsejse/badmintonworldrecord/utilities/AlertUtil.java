package com.simonsejse.badmintonworldrecord.utilities;

import com.simonsejse.badmintonworldrecord.entities.Game;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.springframework.stereotype.Component;

@Component
public class AlertUtil {

    public void newGameInit(Game newGame){
        Alert a = new Alert(Alert.AlertType.INFORMATION, "oprettet", ButtonType.OK);
        a.setTitle("Oprettet!");
        a.setHeaderText("Nyt spil er oprettet!");
        a.setContentText(String.format("Du har nu oprettet et nyt spil med ID %d d. %s", newGame.getId(), newGame.getWhenStarted().toString()));
        a.show();
    }

    public void loadGameById(Game gameById) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, "loading", ButtonType.OK);
        a.setTitle("Loader spil!");
        a.setHeaderText("Fandt spil!");
        a.setContentText(String.format("Spil med ID %d blev fundet!\nSpiller 1 %s\nSpiller 2 %s", gameById.getId(), gameById.getPlayerOne().getName(), gameById.getPlayerTwo().getName()));
        a.show();
    }

    public void gameDoesNotExist(String gameId) {
        Alert a = new Alert(Alert.AlertType.WARNING, "oprettet", ButtonType.OK);
        a.setTitle("Fejl!");
        a.setHeaderText(String.format("Spil-id %s findes ikke!", gameId));
        a.setContentText(String.format("Du har forsøgt at loade et spil med id %s som ikke findes i databasen! Prøv igen!", gameId));
        a.show();
    }

    public void notANumber(String gameId) {
        Alert a = new Alert(Alert.AlertType.WARNING, "oprettet", ButtonType.OK);
        a.setTitle("Fejl!");
        a.setHeaderText(String.format("%s er ikke et tal!", gameId));
        a.setContentText(String.format("Du har forsøgt at indsætte %s som et tal! Dette virker ikke, prøv igen!", gameId));
        a.show();
    }

    public void cannotOpenGameWindow() {
        Alert a = new Alert(Alert.AlertType.WARNING, "load-game-window", ButtonType.OK);
        a.setTitle("Fejl!");
        a.setHeaderText("Kan ikke loade spillet!");
        a.setContentText("Du har forsøgt at loade et spil! Der er dog gået noget galt i processen, prøv igen!");
        a.show();
    }

    public void noUndoHistoryFound() {
        Alert a = new Alert(Alert.AlertType.WARNING, "no-undo-history", ButtonType.OK);
        a.setTitle("Fejl!");
        a.setHeaderText("Du kan ikke fortryde!");
        a.setContentText("Du har forsøgt at fortryde noget, men der er ingen 'undo' historik fundet in-memory!");
        a.show();
    }
}
