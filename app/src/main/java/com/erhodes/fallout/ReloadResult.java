package com.erhodes.fallout;

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
        String ammoType = weapon.getAmmoType();
        int missingAmmo = weapon.getAttribute(Attributes.AMMUNITION_MAX) - weapon.getAttribute(Attributes.AMMUNITION_CURRENT);
        int spareAmmo = performer.hasItem(ammoType);
        int reloadAmount = spareAmmo < missingAmmo ? spareAmmo : missingAmmo;
        performer.removeItems(ammoType, reloadAmount);
        weapon.modifyAttribute(Attributes.AMMUNITION_CURRENT, reloadAmount);
    }
}
