package com.erhodes.fallout;

/**
 * Created by Eric on 24/01/2016.
 */
public class Weapon extends Item {
    String mAmmoType;  // what kind of ammo this uses, as an id string
    Weapon(String name, String desc, String i, int w, String ammoType) {
        super(name, desc, i, TYPE_WEAPON, w);

        mAmmoType = ammoType;

        //Action reload = new ItemAction();
        //actions.add(reload);
    }

    public String getAmmoType() {return mAmmoType;}
}
