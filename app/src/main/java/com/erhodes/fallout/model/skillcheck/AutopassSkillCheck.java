package com.erhodes.fallout.model.skillcheck;

import com.erhodes.fallout.model.*;

import java.lang.*;

/**
 * A SkillCheck that can't be failed.
 */
//TODO: what if this was actually a superclass of skillcheck?
public class AutopassSkillCheck extends SkillCheck {

    public AutopassSkillCheck() {
        super("");
    }

    @Override
    protected int roll(com.erhodes.fallout.model.Character performer) {
        resolvePass(performer);
        return Action.RESULT_PASSED;
    }
}
