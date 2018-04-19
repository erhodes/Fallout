package com.erhodes.fallout.view.attributes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.erhodes.fallout.R;
import com.erhodes.fallout.model.Character;
import com.erhodes.fallout.model.Attribute;
import com.erhodes.fallout.model.Attributes;
import com.erhodes.fallout.model.CapacityAttribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Eric on 14/03/2016.
 */
public class AttributesFragment extends Fragment {
    private static final String TAG = AttributesFragment.class.getSimpleName();
    private ArrayList<Attribute> mPrimaryAttributes, mSecondaryAttributes, mCapacityAttributes;
    private Character character;
    AttributesViewModel mViewModel;
    AttributeAdapter mAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(AttributesViewModel.class);
        mViewModel.getCharacter().observe(this, new Observer<Character>() {
            @Override
            public void onChanged(@Nullable Character character) {
                if (character != null) {
                    update(character);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expandable_list_view, null);

        mPrimaryAttributes = new ArrayList<>();
        mSecondaryAttributes = new ArrayList<>();
        mCapacityAttributes = new ArrayList<>();

        ExpandableListView expandableListView = (ExpandableListView)view.findViewById(R.id.expandableList);
        mAdapter = new AttributeAdapter();
        mAdapter.addGroup(getResources().getString(R.string.primary_attributes), mPrimaryAttributes);
        mAdapter.addGroup(getResources().getString(R.string.secondary_attributes), mSecondaryAttributes);
        mAdapter.addGroup(getResources().getString(R.string.capacity_attributes), mCapacityAttributes);

        expandableListView.setAdapter(mAdapter);
        expandableListView.expandGroup(0);
        expandableListView.expandGroup(1);
        expandableListView.expandGroup(2);

        return view;
    }

    public void update(Character character) {
        mPrimaryAttributes.clear();
        mPrimaryAttributes.addAll(character.getPrimaryAttributes());

        mSecondaryAttributes.clear();
        mSecondaryAttributes.add(character.getAttribute(Attributes.DEFENCE));
        mSecondaryAttributes.add(character.getAttribute(Attributes.TOUGHNESS));
        mSecondaryAttributes.add(character.getAttribute(Attributes.ACTION_POINTS));
        mSecondaryAttributes.add(character.getAttribute(Attributes.WEIGHT_LIMIT));

        mCapacityAttributes.clear();
        mCapacityAttributes.add(character.getAttribute(Attributes.HEALTH));
        mCapacityAttributes.add(character.getAttribute(Attributes.MORALE));

        mAdapter.notifyDataSetChanged();
    }

    public class AttributeAdapter extends BaseExpandableListAdapter {
        ArrayList<String> mGroups;
        HashMap<String, List<Attribute>> mChildren;

        public AttributeAdapter() {
            mGroups = new ArrayList<>();
            mChildren = new HashMap<>();
        }

        public void addGroup(String groupName, ArrayList<Attribute> children) {
            mGroups.add(groupName);
            mChildren.put(groupName, children);
        }

        @Override
        public int getGroupCount() {
            return mGroups.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return mChildren.get(mGroups.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return mGroups.get(groupPosition);
        }

        @Override
        public Attribute getChild(int groupPosition, int childPosition) {
            return mChildren.get(mGroups.get(groupPosition)).get(childPosition);
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
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_attribute_summary, null);
                viewHolder = new ViewHolder();
                viewHolder.nameView = (TextView)convertView.findViewById(R.id.nameView);
                viewHolder.valueView = (TextView)convertView.findViewById(R.id.valueView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder)convertView.getTag();
            }
            Attribute attribute = getChild(groupPosition, childPosition);
            viewHolder.nameView.setText(attribute.getName());
            String valueString = String.format("%d",attribute.getFinalValue());
            if (groupPosition == 2) {
                valueString += "/" + ((CapacityAttribute) attribute).getMaxValue();
            }
            viewHolder.valueView.setText(valueString);

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
