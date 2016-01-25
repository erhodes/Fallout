package com.erhodes.fallout;

/**
 * Created by Eric on 24/01/2016.
 */
public class OpposedSkillCheck extends SkillCheck {
    private String mOpposedSkillKey;
    OpposedSkillCheck(String skill, String opposedSkill) {
        super(skill);
    }

    @Override
    public boolean requiresTarget() {
        return true;
    }

    //TODO: this results in a system where ties are handled inconsistently. Maybe this and unopposed skill checks should both inherit from a superclass?
    @Override
    public int makeCheck(Character performer, Character target) {
        int skillValue = performer.getAttribute(mSkillKey);
        int opponentSkillValue = target.getAttribute(mOpposedSkillKey);
        int rollResult = 0;
            if (rollOff(skillValue, opponentSkillValue)) {
                return resolvePass(performer, target);
            } else {
                return resolveFail(performer, target);
            }
    }

    private boolean rollOff(int performerSkillValue, int targetSkillValue) {
        int a = roll(performerSkillValue);
        int b = roll(targetSkillValue);
        if (a == b) {
            return rollOff(performerSkillValue, targetSkillValue);
        } else if (a > b) {
            return true;
        } else {
            return false;
        }
    }
}
