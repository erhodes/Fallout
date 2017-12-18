package com.erhodes.fallout.view.skills;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.erhodes.fallout.BaseFragment;
import com.erhodes.fallout.R;
import com.erhodes.fallout.model.Character;
import com.erhodes.fallout.model.Skill;

import java.util.ArrayList;

/**
 * Created by Eric on 14/03/2016.
 */
public class SkillsFragment extends Fragment {
    private SkillsViewModel mViewModel;
    private TextView mExpDisplayView;
    private SkillAdapter mAdapter;
    private ArrayList<Skill> mSkills;
    private Character mCharacter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(SkillsViewModel.class);
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
        View view = inflater.inflate(R.layout.fragment_skills, null);
        ListView skillsList = (ListView)view.findViewById(R.id.listView);
        mSkills = new ArrayList<>();
        mAdapter = new SkillAdapter(getActivity(), mSkills);
        skillsList.setAdapter(mAdapter);

        mExpDisplayView = (TextView)view.findViewById(R.id.expView);

        return view;
    }

    public void update() {
        String text = getString(R.string.remaining_exp) + " " + mCharacter.mCurrentExperience;
        mExpDisplayView.setText(text);
        mSkills.clear();
        mSkills.addAll(mCharacter.getSkills());
        mAdapter.notifyDataSetChanged();
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
            TextView nameView, valueView, detailsView, addRankView;
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
                viewHolder.addRankView = (TextView) convertView.findViewById(R.id.addRankView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder)convertView.getTag();
            }
            final Skill skill = getItem(position);
            viewHolder.nameView.setText(skill.getName());
            viewHolder.valueView.setText(String.format("%d", skill.getFinalValue()));
            String detailsText = mActivity.getString(R.string.ranks) + ": " + skill.getRanks() + " + " + skill.getBaseAttribute().getName() + ": " + Math.round(skill.getBaseAttribute().getFinalValue()*0.5f);
            viewHolder.detailsView.setText(detailsText);

            final View secondaryView = viewHolder.secondaryView;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleDetailsView(secondaryView);
                }
            });
            viewHolder.addRankView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewModel.addRank(skill.getKey());
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
