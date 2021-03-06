package com.erhodes.fallout.view.perk;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.erhodes.fallout.R;
import com.erhodes.fallout.model.Character;
import com.erhodes.fallout.model.Perk;
import com.erhodes.fallout.model.PerkManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Eric on 19/03/2016.
 */
public class PerkFragment extends Fragment {
    private static final String TAG = PerkFragment.class.getCanonicalName();
    PerkAdapter mAdapter;
    ExpandableListView mListView;
    PerkViewModel mViewModel;
    Character mCharacter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(PerkViewModel.class);
        mViewModel.getCharacter().observe(this, new Observer<Character>() {
            @Override
            public void onChanged(@Nullable Character character) {
                mCharacter = character;
                update();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expandable_list_view, null);
        mListView = (ExpandableListView)view.findViewById(R.id.expandableList);
        return view;
    }

    public void update() {
        mAdapter = new PerkAdapter();
        mAdapter.addGroup(getString(R.string.perks), mCharacter.getPerks());
        mAdapter.addGroup(getString(R.string.remaining_perks), PerkManager.getInstance().getUnacquiredPerks(mCharacter));
        mListView.setAdapter(mAdapter);
        mListView.expandGroup(0);
        if (mCharacter.mAvailablePerks > 0)
            mListView.expandGroup(1);
    }

    public class PerkAdapter extends BaseExpandableListAdapter {
        private int ACQUIRED_PERKS = 0;
        private int UNACQUIRED_PERKS = 1;
        ArrayList<String> mGroups;
        HashMap<String, List<Perk>> mChildren;

        public PerkAdapter() {
            mGroups = new ArrayList<>();
            mChildren = new HashMap<>();
        }

        public void addGroup(String groupName, List<Perk> children) {
            mGroups.add(groupName);
            mChildren.put(groupName, children);
        }

        public void updateGroup(String groupName, List<Perk> children) {
            mChildren.put(groupName, children);
        }

        @Override
        public int getGroupCount() {
            return mGroups.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return getChildren(groupPosition).size();
        }

        @Override
        public String getGroup(int groupPosition) {
            return mGroups.get(groupPosition);
        }

        public List<Perk> getChildren(int groupPosition) {
            return mChildren.get(getGroup(groupPosition));
        }
        @Override
        public Perk getChild(int groupPosition, int childPosition) {
            return getChildren(groupPosition).get(childPosition);
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
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_attribute_group, null);
            }
            TextView groupName = (TextView)convertView.findViewById(R.id.groupNameView);
            String otherPerks = mGroups.get(groupPosition);
            if (groupPosition > 0 && mCharacter.mAvailablePerks > 0) {
                otherPerks += "(" + mCharacter.mAvailablePerks + " remaining)";
            }
            groupName.setText(otherPerks);
            return convertView;
        }

        private class ViewHolder {
            TextView nameView, descView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.list_perk_summary, null);
                holder = new ViewHolder();
                holder.nameView = (TextView)convertView.findViewById(R.id.nameView);
                holder.descView = (TextView)convertView.findViewById(R.id.descView);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final Perk perk = getChild(groupPosition, childPosition);
            holder.nameView.setText(perk.name);
            holder.descView.setText(perk.description);
            if (groupPosition == UNACQUIRED_PERKS && mCharacter.mAvailablePerks > 0) {
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewModel.acquirePerk(perk);
                    }
                });
            }
            return convertView;
        }
    }
}
