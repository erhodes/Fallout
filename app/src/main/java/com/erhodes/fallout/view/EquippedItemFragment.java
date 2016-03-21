package com.erhodes.fallout.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.erhodes.fallout.BaseFragment;
import com.erhodes.fallout.R;
import com.erhodes.fallout.model.Attribute;
import com.erhodes.fallout.model.Attributes;
import com.erhodes.fallout.model.CapacityAttribute;
import com.erhodes.fallout.model.Item;
import com.erhodes.fallout.model.Weapon;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Eric on 20/03/2016.
 */
public class EquippedItemFragment extends BaseFragment {

    public static EquippedItemFragment newInstance() {
        return new EquippedItemFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getCharacterService();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attributes, null);

        ExpandableListView expandableListView = (ExpandableListView)view.findViewById(R.id.primaryAttributeList);
        ArrayList<Weapon> weapons = new ArrayList<>();
        weapons.add(mCharacter.getWeapon());

        ArrayList<Item> armor = new ArrayList<>();
        armor.add(mCharacter.getArmor());

        ItemAdapter adapter = new ItemAdapter(weapons, armor, mCharacter.getQuickItems());

        expandableListView.setAdapter(adapter);
        expandableListView.expandGroup(0);
        expandableListView.expandGroup(1);
        expandableListView.expandGroup(2);
        return view;
    }

    public class ItemAdapter extends BaseExpandableListAdapter {
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
                case 0:
                    return mWeapons.size();
                case 1:
                    return mArmors.size();
                case 2:
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
            TextView nameView, valueView;
        }
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_summary, null);
                viewHolder = new ViewHolder();
                viewHolder.nameView = (TextView)convertView.findViewById(R.id.nameView);
                viewHolder.valueView = (TextView)convertView.findViewById(R.id.valueView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder)convertView.getTag();
            }
            Item item = getChild(groupPosition, childPosition);
            viewHolder.nameView.setText(item.getName());

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
