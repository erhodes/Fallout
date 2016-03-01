package com.erhodes.fallout;

import java.util.ArrayList;

/**
 * Created by Eric on 24/01/2016.
 */
public class Weapon extends Item {
    String mAmmoType;  // what kind of ammo this uses, as an id string
    int mDamage;

    Weapon(String name, String desc, String i, int w, int damage, String ammoType, int maxAmmo) {
        super(name, desc, i, TYPE_WEAPON, w);
        mDamage = damage;
        mAmmoType = ammoType;

        // attributes
        mAttributes.put(Attributes.AMMUNITION_MAX, new Attribute("Max Ammo", Attributes.AMMUNITION_MAX, maxAmmo));
        mAttributes.put(Attributes.AMMUNITION_CURRENT, new CapacityAttribute(Attributes.AMMUNITION_CURRENT, mAttributes.get(Attributes.AMMUNITION_MAX)));

        Action reloadAction = new Action();
        SkillCheck check = new AutopassSkillCheck();
        ReloadResult result = new ReloadResult();
        TargetGroup targetGroup = new TargetGroup(this);
        check.addTargetGroup(targetGroup);
        check.mPassResults.add(result);
        reloadAction.skillCheck = check;
        actions.add(reloadAction);
    }

    public String getAmmoType() {return mAmmoType;}
}
