package com.erhodes.fallout.model;

import com.erhodes.fallout.model.skillcheck.AutopassSkillCheck;
import com.erhodes.fallout.model.skillcheck.EffectResult;
import com.erhodes.fallout.model.skillcheck.OpposedStaticSkillCheck;
import com.erhodes.fallout.model.skillcheck.ReloadResult;
import com.erhodes.fallout.model.skillcheck.SkillCheck;

/**
 * Created by Eric on 20/03/2016.
 */
public class AmmoWeapon extends Weapon {
    String mAmmoType;  // what kind of ammo this uses, as an id string

    public AmmoWeapon(String name, String description, String id, int weight, int damage, String ammoType, int maxAmmo) {
        super(name, description, id, weight, damage);
        mAmmoType = ammoType;

        // attributes
        mAttributes.put(Attributes.AMMUNITION_MAX, new Attribute("Max Ammo", Attributes.AMMUNITION_MAX, maxAmmo));
        mAttributes.put(Attributes.AMMUNITION_CURRENT, new CapacityAttribute(Attributes.AMMUNITION_CURRENT, mAttributes.get(Attributes.AMMUNITION_MAX)));

        Action reloadAction = new Action("Reload","Reload " + name, 2);
        SkillCheck check = new AutopassSkillCheck();
        ReloadResult result = new ReloadResult();
        TargetGroup thisGroup = new TargetGroup(this);
        check.addTargetGroup(thisGroup);
        check.addPassResult(result);
        reloadAction.skillCheck = check;
        actions.add(reloadAction);
    }

    public String getAmmoType() {return mAmmoType;}

    public Action buildStandardAttackAction(int apCost) {
        Action attackAction = super.buildStandardAttackAction(apCost);
        Cost ammoCost = new Cost(Attributes.AMMUNITION_CURRENT, 1, 1);
        attackAction.skillCheck.mCosts.add(ammoCost);
        return attackAction;
    }
}
