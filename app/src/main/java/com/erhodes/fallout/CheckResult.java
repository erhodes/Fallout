package com.erhodes.fallout;

import java.util.ArrayList;

/**
 * This class represents the result of passing (or failing) a skill check as part of an Action.
 */
public abstract class CheckResult {
    public abstract void applyResult(Character performer, Character target);
    public abstract boolean requiresTarget();
}
