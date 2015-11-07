package com.erhodes.fallout;

import java.util.ArrayList;

/**
 * Created by Eric on 01/11/2015.
 */
public class Action {
    String name, description;
    int cost;
    ArrayList<Effect> effects;

    public Action(String n, String d, int c) {
        name = n;
        description = d;
        cost = c;
        effects = new ArrayList<>();
    }
}
