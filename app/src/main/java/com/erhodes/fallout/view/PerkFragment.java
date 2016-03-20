package com.erhodes.fallout.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.erhodes.fallout.BaseFragment;
import com.erhodes.fallout.R;
import com.erhodes.fallout.model.Perk;
import com.erhodes.fallout.model.Skill;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 19/03/2016.
 */
public class PerkFragment extends BaseFragment {
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
        Log.d("Eric", "loaded char perks fragment, character has " + mCharacter.getPerks().size() + " perks");
        PerkAdapter adapter = new PerkAdapter(mCharacter.getPerks());
        skillsList.setAdapter(adapter);

        return view;
    }

    public class PerkAdapter extends BaseAdapter {
        List<Perk> mSkills;

        public PerkAdapter(List<Perk> skills) {
            mSkills = skills;
        }

        @Override
        public int getCount() {
            return mSkills.size();
        }

        @Override
        public Perk getItem(int position) {
            return mSkills.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {
            TextView nameView, descView;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
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

            final Perk perk = getItem(position);
            holder.nameView.setText(perk.name);
            holder.descView.setText(perk.description);
            return convertView;
        }
    }
}
