package com.erhodes.fallout.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Eric on 18/04/2016.
 */
public class GameLog {

    public interface GameLogListener {
        void newEvent();
    }

    private ArrayList<String> mEvents;
    private ArrayList<GameLogListener> mListeners;

    private static GameLog sInstance;

    public static GameLog getInstance() {
        if (sInstance == null) {
            sInstance = new GameLog();
        }
        return sInstance;
    }

    private GameLog() {
        mEvents = new ArrayList<>();
        mListeners = new ArrayList<>();
    }

    public void registerListener(GameLogListener listener) {
        //TODO: make this into a weak reference
        mListeners.add(listener);
    }

    public void unregisterListener(GameLogListener listener) {

    }

    public void addEvent(String event) {
        mEvents.add(event);
        for (GameLogListener listener : mListeners) {
            listener.newEvent();
        }
    }

    public void addRollEvent(String performerName, int roll, String skillKey, int skillValue, int difficulty) {
        String result = String.format(Locale.CANADA, "%s rolled a %d(%d + %s %d) against DC %d.",performerName, roll, roll-skillValue, skillKey, skillValue, difficulty);
        if (roll >= difficulty) {
            result += " Success!";
        } else {
            result += " Failure!";
        }
        addEvent(result);
    }

    public void addOpposedRollEvent(String performerName, int performerRoll, String oppositionName, int opposedRoll, String skillKey) {
        if (performerRoll > opposedRoll) {
            addEvent(generateOpposedRollEventLog(performerName, performerRoll, oppositionName, opposedRoll));
        } else {
            addEvent(generateOpposedRollEventLog(oppositionName, opposedRoll, performerName, performerRoll));
        }
    }

    private String generateOpposedRollEventLog(String winnerName, int winningRoll, String loserName, int losingRoll) {
        String result = String.format(Locale.CANADA, "%s rolls %d; %s rolls %d. %s wins!", winnerName, winningRoll, loserName, losingRoll, winnerName);
        return result;
    }

    public void addEffectEvent(String name, String skillKey, int magnitude, int duration) {
        String result = name;
        if (magnitude >= 0) {
            result += String.format(Locale.CANADA," gained %d", magnitude);
        } else {
            result += String.format(Locale.CANADA," lost %d", -magnitude);
        }
        result += " " + skillKey;
        if (duration > 0) {
            result += String.format(Locale.CANADA, " for %d rounds.", duration);
        } else {
            result += ".";
        }
        addEvent(result);
    }

    public void addReloadEvent(String weaponName, int reloadedAmount) {
        String result = "Reloaded " + reloadedAmount + " round";
        if (reloadedAmount > 1)
            result +="s";
        result += " into " + weaponName;
        addEvent(result);
    }

    public List<String> getLogs() {
        return mEvents;
    }
}
