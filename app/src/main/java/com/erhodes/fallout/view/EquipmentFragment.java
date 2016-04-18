package com.erhodes.fallout.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.erhodes.fallout.BaseFragment;
import com.erhodes.fallout.R;
import com.erhodes.fallout.model.AmmoWeapon;
import com.erhodes.fallout.model.Attributes;
import com.erhodes.fallout.model.Item;
import com.erhodes.fallout.model.Weapon;
import com.google.gson.internal.bind.MapTypeAdapterFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 20/03/2016.
 */
public class EquipmentFragment extends BaseFragment implements FragmentLifecycle {
    private ArrayList<Weapon> mWeapons;
    private ArrayList<Item> mArmor;
    private ItemAdapter mAdapter;

    public static EquipmentFragment newInstance() {
        return new EquipmentFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getCharacterService();
    }

    @Override
    public void onResumeFragment(){
        update();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expandable_list_view, null);

        ExpandableListView expandableListView = (ExpandableListView)view.findViewById(R.id.expandableList);
        mWeapons = new ArrayList<>();
        mArmor = new ArrayList<>();
        mAdapter = new ItemAdapter(mWeapons, mArmor, mCharacter.getQuickItems());
        update();


        expandableListView.setAdapter(mAdapter);
        expandableListView.expandGroup(0);
        expandableListView.expandGroup(1);
        expandableListView.expandGroup(2);
        return view;
    }

    public void update() {
        mWeapons.clear();
        mWeapons.add(mCharacter.getWeapon());
        mArmor.clear();
        mArmor.add(mCharacter.getArmor());
        mAdapter.notifyDataSetChanged();
    }

    public class ItemAdapter extends BaseExpandableListAdapter {
        private final int GROUP_WEAPON = 0;
        private final  int GROUP_ARMOR = 1;
        private final int GROUP_QUICK = 2;
        ArrayList<String> mGroups;
        List<Weapon> mWeapons;
        List<Item> mArmors;
        List<Item> mQuickItems;

        public ItemAdapter(List<Weapon> weapons, List<Item> armors, List<Item> quickItems) {
            mWeapons = weapons;
            mArmors = armors;
            mQuickItems = quickItems;
            mGroups = new ArrayList<>();
            mGroups.add(getString(R.string.weapons));
            mGroups.add(getString(R.string.armor));
            mGroups.add(getString(R.string.quick_items));
        }

        @Override
        public int getGroupCount() {
            return 3;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case GROUP_WEAPON:
                    return mWeapons.size();
                case GROUP_ARMOR:
                    return mArmors.size();
                case GROUP_QUICK:
                    return mQuickItems.size();
            }
            return 0;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return mGroups.get(groupPosition);
        }

        @Override
        public Item getChild(int groupPosition, int childPosition) {
            switch (groupPosition) {
                case 0:
                    return mWeapons.get(childPosition);
                case 1:
                    return mArmors.get(childPosition);
                default:
                    return mQuickItems.get(childPosition);
            }
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_attribute_group, null);
            }
            TextView groupName = (TextView)convertView.findViewById(R.id.groupNameView);
            groupName.setText(mGroups.get(groupPosition));
            return convertView;
        }

        class ViewHolder {
            TextView nameView, descView;
        }
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_summary, null);
                viewHolder = new ViewHolder();
                viewHolder.nameView = (TextView)convertView.findViewById(R.id.nameView);
                viewHolder.descView = (TextView)convertView.findViewById(R.id.descView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder)convertView.getTag();
            }
            Item item = getChild(groupPosition, childPosition);
            viewHolder.nameView.setText(item.getName());
            String description;
            if (groupPosition == GROUP_WEAPON) {
                Weapon weapon = (Weapon)item;
                description = "Damage: " + weapon.getDamage();
                if (weapon instanceof AmmoWeapon) {
                    description += ".   Ammo: " + weapon.getAttributeValue(Attributes.AMMUNITION_CURRENT) + "/" + weapon.getAttributeValue(Attributes.AMMUNITION_MAX);
                }
            } else {
                description = item.getDescription();
            }
            viewHolder.descView.setText(description);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
