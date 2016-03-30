package com.erhodes.fallout.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.erhodes.fallout.BaseFragment;
import com.erhodes.fallout.R;
import com.erhodes.fallout.model.Skill;

import java.util.ArrayList;

/**
 * Created by Eric on 14/03/2016.
 */
public class SkillsFragment extends BaseFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getCharacterService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_skills, null);
        ListView skillsList = (ListView)view.findViewById(R.id.listView);
        SkillAdapter adapter = new SkillAdapter(getActivity(), mCharacter.getSkills());
        skillsList.setAdapter(adapter);

        return view;
    }

    public class SkillAdapter extends BaseAdapter {
        ArrayList<Skill> mSkills;
        Activity mActivity;

        public SkillAdapter(Activity activity, ArrayList<Skill> skills) {
            mActivity = activity;
            mSkills = skills;
        }

        @Override
        public int getCount() {
            return mSkills.size();
        }

        @Override
        public Skill getItem(int position) {
            return mSkills.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {
            TextView nameView, valueView, detailsView;
            LinearLayout secondaryView;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = new ViewHolder();
            if (convertView == null) {
                convertView = mActivity.getLayoutInflater().inflate(R.layout.list_skill, null);
                viewHolder.nameView = (TextView) convertView.findViewById(R.id.nameView);
                viewHolder.valueView = (TextView) convertView.findViewById(R.id.valueView);
                viewHolder.detailsView = (TextView) convertView.findViewById(R.id.detailsView);
                viewHolder.secondaryView = (LinearLayout) convertView.findViewById(R.id.secondaryView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder)convertView.getTag();
            }
            Skill skill = getItem(position);
            viewHolder.nameView.setText(skill.getName());
            viewHolder.valueView.setText(String.format("%d", skill.getFinalValue()));
            String detailsText = mActivity.getString(R.string.ranks) + ": " + skill.getRanks() + " + " + skill.getBaseAttribute().getName() + ": " + skill.getBaseAttribute().getFinalValue()/2;
            viewHolder.detailsView.setText(detailsText);

            final View secondaryView = viewHolder.secondaryView;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleDetailsView(secondaryView);
                }
            });

            return convertView;
        }

        private void toggleDetailsView(View v) {
            if (v.isShown()) {
                v.setVisibility(View.GONE);
            } else {
                v.setVisibility(View.VISIBLE);
            }
        }
    }
}
