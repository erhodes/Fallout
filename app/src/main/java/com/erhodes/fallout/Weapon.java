package com.erhodes.fallout;

import java.util.ArrayList;

/**
 * Created by Eric on 24/01/2016.
 */
public class Weapon extends Item {
    String mAmmoType;  // what kind of ammo this uses, as an id string
    int mDamage;


    Weapon(String name, String description, String id, int weight, int damage, String ammoType, int maxAmmo) {
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
        check.mPassResults.add(result);
        reloadAction.skillCheck = check;
        actions.add(reloadAction);

        Action attackAction = new Action("Fire " + name, "Fire a single shot with your " + name, 2);
        SkillCheck attackCheck = new OpposedStaticSkillCheck(Skills.GUNS, Attributes.DEFENCE, new TargetGroup("Attack Target", 1, 1));
        attackCheck.addTargetGroup(thisGroup);
        EffectResult damageResult = new EffectResult(Attributes.HEALTH, -mDamage);
        damageResult.addAffectedTargetGroup(0);
        attackCheck.mPassResults.add(damageResult);
        Cost ammoCost = new Cost(Attributes.AMMUNITION_CURRENT, 1, 1);
        attackCheck.mCosts.add(ammoCost);
        attackAction.skillCheck = attackCheck;
        actions.add(attackAction);
    }

    @Override
    public boolean isValidAttribute(String attrKey) {
        return Attributes.getWeaponAttributes().contains(attrKey);
    }

    public String getAmmoType() {return mAmmoType;}

    public void addAttack(Action attackAction) {
        actions.add(attackAction);
    }
}
