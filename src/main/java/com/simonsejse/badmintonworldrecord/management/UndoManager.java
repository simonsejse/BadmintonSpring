package com.simonsejse.badmintonworldrecord.management;

import com.simonsejse.badmintonworldrecord.constants.PlayerType;
import com.simonsejse.badmintonworldrecord.exceptions.NoUndoHistoryFoundException;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class UndoManager {

    //Add point to player 1
    //Add Element to History
    //So I l8 if i press F choose the latest

    private Map<Long, LinkedList<PlayerType>> undoHistoryByGameId;

    public UndoManager(){
        this.undoHistoryByGameId = new HashMap<>();
    }

    public void addNextSelection(long gameId, PlayerType type){
        undoHistoryByGameId.compute(gameId, (id, currentUndoHistory) -> {
            if (currentUndoHistory == null) currentUndoHistory = new LinkedList<>();
            currentUndoHistory.addFirst(type);
            return currentUndoHistory;
        });
    }

    public PlayerType revertSelection(long currentGameId) throws NoUndoHistoryFoundException {
        final LinkedList<PlayerType> undoHistory = undoHistoryByGameId.get(currentGameId);
        if (undoHistory == null || undoHistory.isEmpty()) throw new NoUndoHistoryFoundException("Der er ingen historik for dette spil!");
        return undoHistory.pollFirst();

    }
}
