package com.erhodes.fallout.presenter;

import com.erhodes.fallout.model.Item;

/**
 * Created by Eric on 16/04/2016.
 */
public class InventoryContract {
    public interface UserActionListener {
        void equipItem(Item item);
        void dropItem(Item item);
        String getWeightString();
    }
    public interface View {
        void update();
    }
}
