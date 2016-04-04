package com.erhodes.fallout.presenter;

import com.erhodes.fallout.model.Perk;

/**
 * Created by Eric on 03/04/2016.
 */
public class PerkContract {
    public interface UserActionListener{
        void acquirePerk(Perk perk);
    }
    public interface View {
        void update();
    }
}
