package com.erhodes.fallout.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.erhodes.fallout.BaseFragment;
import com.erhodes.fallout.R;
import com.erhodes.fallout.model.Attribute;
import com.erhodes.fallout.model.Attributes;
import com.erhodes.fallout.model.CapacityAttribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Eric on 14/03/2016.
 */
public class AttributesFragment extends BaseFragment {
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
        AttributeAdapter adapter = new AttributeAdapter();
        adapter.addGroup(getResources().getString(R.string.primary_attributes), mCharacter.getPrimaryAttributes());
        ArrayList<Attribute> secondaryAttributes = new ArrayList<>();
        secondaryAttributes.add(mCharacter.getAttribute(Attributes.DEFENCE));
        secondaryAttributes.add(mCharacter.getAttribute(Attributes.TOUGHNESS));
        adapter.addGroup(getResources().getString(R.string.secondary_attributes), secondaryAttributes);

        ArrayList<Attribute> capacityAttributes = new ArrayList<>();
        capacityAttributes.add(mCharacter.getAttribute(Attributes.HEALTH));
        adapter.addGroup(getResources().getString(R.string.capacity_attributes), capacityAttributes);

        expandableListView.setAdapter(adapter);
        expandableListView.expandGroup(0);
        expandableListView.expandGroup(1);
        expandableListView.expandGroup(2);
        return view;
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
