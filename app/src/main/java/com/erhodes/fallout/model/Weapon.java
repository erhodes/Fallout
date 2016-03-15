package com.erhodes.fallout.model;

import com.erhodes.fallout.model.skillcheck.AutopassSkillCheck;
import com.erhodes.fallout.model.skillcheck.EffectResult;
import com.erhodes.fallout.model.skillcheck.OpposedStaticSkillCheck;
import com.erhodes.fallout.model.skillcheck.ReloadResult;
import com.erhodes.fallout.model.skillcheck.SkillCheck;

/**
 * Created by Eric on 24/01/2016.
 */
public class Weapon extends Item {
    String mAmmoType;  // what kind of ammo this uses, as an id string
    int mDamage;


    public Weapon(String name, String description, String id, int weight, int damage, String ammoType, int maxAmmo) {
        super(name, description, id, TYPE_WEAPON, weight);
        mDamage = damage;
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

    @Override
    public boolean isValidAttribute(String attrKey) {
        return Attributes.getWeaponAttributes().contains(attrKey);
    }

    public String getAmmoType() {return mAmmoType;}

    public void addAttack(Action attackAction) {
        actions.add(attackAction);
    }

    public Action buildStandardAttackAction(int apCost) {
        Action attackAction = new Action("Fire " + mDisplayName, "Fire a single shot with your " + mDisplayName, 2);
        SkillCheck attackCheck = new OpposedStaticSkillCheck(Skills.GUNS, Attributes.DEFENCE, new TargetGroup("Attack Target", 1, 1));
        TargetGroup thisGroup = new TargetGroup(this);
        attackCheck.addTargetGroup(thisGroup);
        EffectResult damageResult = new EffectResult(Attributes.HEALTH, -mDamage);
        damageResult.addAffectedTargetGroup(0);
        attackCheck.addPassResult(damageResult);
        Cost ammoCost = new Cost(Attributes.AMMUNITION_CURRENT, 1, 1);
        attackCheck.mCosts.add(ammoCost);
        attackAction.skillCheck = attackCheck;
        return attackAction;
    }
}
