package com.erhodes.fallout;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.erhodes.fallout.model.Item;


/**
 * A simple {@link Fragment} subclass.
 */
public class EquipmentFragment extends BaseFragment {
    TextView mArmorDesc, mArmorName, mWeaponDesc, mWeaponName;
    LinearLayout mArmorView, mWeaponView;
    Item mTargetItem;

    public EquipmentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getCharacterService();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_equipment, container, false);

        mArmorDesc = (TextView)view.findViewById(R.id.armorDescView);
        mArmorName = (TextView)view.findViewById(R.id.armorNameView);
        mWeaponName = (TextView)view.findViewById(R.id.weaponNameView);
        mWeaponDesc = (TextView)view.findViewById(R.id.weaponDescView);

        mArmorView = (LinearLayout)view.findViewById(R.id.armorView);
        mArmorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Eric", "well that works");
            }
        });

        mWeaponView = (LinearLayout)view.findViewById(R.id.weaponView);
        registerForContextMenu(mArmorView);
        registerForContextMenu(mWeaponView);

        updateItems();
        return view;
    }

    private void updateItems() {
        mArmorDesc.setText(mCharacter.getArmor().mDescription);
        mArmorName.setText(mCharacter.getArmor().mDisplayName);

        Item weapon = mCharacter.getWeapon();
        mWeaponName.setText(weapon.mDisplayName);
        mWeaponDesc.setText(weapon.mDescription);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        if (v.equals(mArmorView)) {
            mTargetItem = mCharacter.getArmor();
            inflater.inflate(R.menu.equipment_menu_armor, menu);
        } else if (v.equals(mWeaponView)){
            mTargetItem = mCharacter.getWeapon();
            inflater.inflate(R.menu.equipment_menu_weapon, menu);
        }
    }
    //TODO: is there a better way to figure out which view triggred this?
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.d("Eric","clicked on item " + mTargetItem.mDisplayName + " with " + mTargetItem.effects.size() + " effects");
        switch (item.getItemId()) {
            case R.id.action_unequip:
                mCharacter.unequipItem(mTargetItem);
                updateItems();
                return true;
            case R.id.action_reload:
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


}
