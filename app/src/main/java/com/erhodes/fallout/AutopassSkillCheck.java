package com.erhodes.fallout;

import java.util.ArrayList;

/**
 * A SkillCheck that can't be failed.
 */
//TODO: what if this was actually a superclass of skillcheck?
public class AutopassSkillCheck extends SkillCheck {

    public AutopassSkillCheck() {
        super("");
    }

    @Override
    protected int roll(Character performer) {
        resolvePass(performer);
        return Action.RESULT_PASSED;
    }
}
