package com.erhodes.fallout;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Eric on 29/02/2016.
 */
public class ReloadResult extends CheckResult {

    public ReloadResult() {
        mAffectedTargetGroups.put(0,0);
    }
    @Override
    public void applyResult(Character performer, ArrayList<TargetGroup> mTargetGroups) {
        Weapon weapon = (Weapon)mTargetGroups.get(0).mTargets.get(0);
        Log.d("Eric", "reloading " + weapon.mDisplayName);
        String ammoType = weapon.getAmmoType();
        Log.d("Eric","current ammo is " + weapon.getAttribute(Attributes.AMMUNITION_CURRENT) + " and max is " + weapon.getAttribute(Attributes.AMMUNITION_MAX));
        int missingAmmo = -(weapon.getAttribute(Attributes.AMMUNITION_CURRENT) - weapon.getAttribute(Attributes.AMMUNITION_MAX));
        int spareAmmo = performer.hasItem(ammoType);
        int reloadAmount = spareAmmo < missingAmmo ? spareAmmo : missingAmmo;
        performer.removeItems(ammoType, reloadAmount);
        Log.d("Eric","character has " + spareAmmo + " ammo, and weapon is missing " + missingAmmo + "; final reload amount is " + reloadAmount);
        weapon.modifyAttribute(Attributes.AMMUNITION_CURRENT, reloadAmount);
    }
}
