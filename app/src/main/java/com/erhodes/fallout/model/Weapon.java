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
    int mDamage;

    public Weapon(String name, String description, String id, int weight, int damage) {
        super(name, description, id, TYPE_WEAPON, weight);
        mDamage = damage;
    }

    public Weapon(Weapon oldWeapon) {
        super(oldWeapon);
        mDamage = oldWeapon.mDamage;
    }
    @Override
    public boolean isValidAttribute(String attrKey) {
        return Attributes.getWeaponAttributes().contains(attrKey);
    }

    public int getDamage() { return mDamage; }

    public void addAttack(Action attackAction) {
        actions.add(attackAction);
    }

    public Action buildStandardAttackAction(int apCost) {
        Action attackAction = new Action("Fire " + mDisplayName, "Fire a single shot with your " + mDisplayName, 2);
        SkillCheck attackCheck = new OpposedStaticSkillCheck(Skills.GUNS, Attributes.DEFENCE, new TargetGroup("Attack Target", 1, 1));
        TargetGroup thisGroup = new TargetGroup(this);
        attackCheck.addTargetGroup(TargetGroup.TARGET_GRANTING_ITEM, thisGroup);
        EffectResult damageResult = new EffectResult(Attributes.HEALTH, -mDamage);
        damageResult.addAffectedTargetGroup(TargetGroup.TARGET_PRIMARY);
        attackCheck.addPassResult(damageResult);
        attackAction.skillCheck = attackCheck;
        return attackAction;
    }
}
