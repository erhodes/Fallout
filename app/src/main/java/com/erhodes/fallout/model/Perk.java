package com.erhodes.fallout.model;

import java.util.ArrayList;

/**
 * Created by Eric on 17/10/2015.
 */
public class Perk {
    public String name, description, id;
    public ArrayList<Effect> effects;

    public Perk(String desc) {
        description = desc;
        id = "test";
        effects = new ArrayList<>();
    }
    public Perk(String id, String desc) {
        description = desc;
        this.id = id;
        effects = new ArrayList<>();
    }
}
