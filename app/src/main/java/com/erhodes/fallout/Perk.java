package com.erhodes.fallout;

import java.util.ArrayList;

/**
 * Created by Eric on 17/10/2015.
 */
public class Perk {
    public String name, description, id;
    ArrayList<Effect> effects;

    Perk(String d) {
        description = d;
        id = "test";
    }
}
