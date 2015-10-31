package com.erhodes.fallout;

/**
 * Created by Eric on 24/10/2015.
 */
public class Item {
    String displayName, description, id;
    int weight;
    Effect effect;

    public Item(String name, String desc) {
        displayName = name;
        description = desc;
    }
}
