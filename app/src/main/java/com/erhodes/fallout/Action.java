package com.erhodes.fallout;

import android.util.Log;

import java.util.ArrayList;

/**
 * Actions are performed by spending Action Points (AP). When a character performs an Action, all of it's effects are applied to that character.
 * All the effects of an action should be temporary.
 */
public class Action {
    String name, description;
    int cost;
    ArrayList<Effect> effects;

    public Action() {}

    public Action(String n, String d, int c) {
        name = n;
        description = d;
        cost = c;
        effects = new ArrayList<>();
    }

    public void performAction(Character c) {
        for (Effect e : effects) {
            c.applyEffect(e);
        }
    }
}
