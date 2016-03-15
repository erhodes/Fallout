package com.erhodes.fallout.model;

import java.util.ArrayList;

/**
 * Created by Eric on 17/10/2015.
 */
public class Perk {
    public String name, description, id;
    public ArrayList<Effect> effects;

    Perk(String d) {
        description = d;
        id = "test";
    }
}
