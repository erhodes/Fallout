package com.erhodes.fallout;

/**
 * Created by Eric on 24/01/2016.
 */
public class Weapon extends Item {

    Weapon(String name, String desc, String i, int w) {
        super(name, desc, i, TYPE_WEAPON, w);
    }
}
