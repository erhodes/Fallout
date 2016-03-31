package com.erhodes.fallout.presenter;

/**
 * Created by Eric on 29/03/2016.
 */
public class SkillsContract {
    public interface UserActionsListener {
        public void addRank(String skill);
    }

    public interface View {
        public void update();
    }
}
