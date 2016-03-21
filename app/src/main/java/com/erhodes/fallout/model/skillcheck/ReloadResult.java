package com.erhodes.fallout.model.skillcheck;

import android.util.Log;

import com.erhodes.fallout.model.*;

import java.lang.*;
import java.util.ArrayList;

/**
 * Created by Eric on 29/02/2016.
 */
public class ReloadResult extends CheckResult {

    public ReloadResult() {
        mAffectedTargetGroups.put(0,0);
    }
    @Override
    public void applyResult(com.erhodes.fallout.model.Character performer, ArrayList<TargetGroup> mTargetGroups) {
        AmmoWeapon weapon = (AmmoWeapon)mTargetGroups.get(0).mTargets.get(0);
        Log.d("Eric", "reloading " + weapon.mDisplayName);
        String ammoType = weapon.getAmmoType();
        Log.d("Eric","current ammo is " + weapon.getAttributeValue(Attributes.AMMUNITION_CURRENT) + " and max is " + weapon.getAttributeValue(Attributes.AMMUNITION_MAX));
        int missingAmmo = -(weapon.getAttributeValue(Attributes.AMMUNITION_CURRENT) - weapon.getAttributeValue(Attributes.AMMUNITION_MAX));
        int spareAmmo = performer.hasItem(ammoType);
        int reloadAmount = spareAmmo < missingAmmo ? spareAmmo : missingAmmo;
        performer.removeItemsFromInventory(ammoType, reloadAmount);
        Log.d("Eric","character has " + spareAmmo + " ammo, and weapon is missing " + missingAmmo + "; final reload amount is " + reloadAmount);
        weapon.modifyAttribute(Attributes.AMMUNITION_CURRENT, reloadAmount);
    }
}
