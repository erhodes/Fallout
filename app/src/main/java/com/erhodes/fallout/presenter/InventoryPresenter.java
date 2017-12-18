package com.erhodes.fallout.presenter;

import android.content.Context;
import android.util.Log;

import com.erhodes.fallout.CharacterRepository;
import com.erhodes.fallout.model.Character;
import com.erhodes.fallout.R;
import com.erhodes.fallout.model.Attributes;
import com.erhodes.fallout.model.Item;

/**
 * Created by Eric on 16/04/2016.
 */
public class InventoryPresenter implements InventoryContract.UserActionListener {
    Context mContext;
    Character mCharacter;
    InventoryContract.View mView;

    public InventoryPresenter(Context context, CharacterRepository characterRepository, InventoryContract.View view) {
        mView = view;
        mContext = context;
        mCharacter = characterRepository.getActiveCharacter().getValue();
    }

    @Override
    public void equipItem(Item item) {
        Log.d("Eric","equiping item " + item.mDisplayName + " with " + item.effects.size() + " effects");
        mCharacter.equipItem(item);
        mView.update();
    }

    @Override
    public void dropItem(Item item) {
        mCharacter.removeItemFromInventory(item);
        mView.update();
    }

    @Override
    public String getWeightString() {
        return String.format(mContext.getString(R.string.current_weight), mCharacter.mCarriedWeight, mCharacter.getAttributeValue(Attributes.WEIGHT_LIMIT));
    }
}
