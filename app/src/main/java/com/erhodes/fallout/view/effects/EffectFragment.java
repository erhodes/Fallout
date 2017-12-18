package com.erhodes.fallout.view.effects;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.erhodes.fallout.R;
import com.erhodes.fallout.model.Character;
import com.erhodes.fallout.model.Effect;

import java.util.ArrayList;

/**
 * Displays information about effects currently affecting a character.
 */
public class EffectFragment extends Fragment {
    private ListView mListView;
    private EffectAdapter mAdapter;
    private EffectsViewModel mViewModel;
    private Character mCharacter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(EffectsViewModel.class);
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
        View view = inflater.inflate(R.layout.fragment_effect,null);
        mListView = (ListView)view.findViewById(R.id.effectListView);
        return view;
    }

    public void update() {
        mAdapter = new EffectAdapter(mCharacter.getActiveEffects());
        mListView.setAdapter(mAdapter);
    }

    private class EffectAdapter extends BaseAdapter {
        ArrayList<Effect> mEffects;

        public EffectAdapter(ArrayList<Effect> effects) {
            mEffects = effects;
        }

        @Override
        public int getCount() {
            return mEffects.size();
        }

        @Override
        public Effect getItem(int position) {
            return mEffects.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        class ViewHolder {
            TextView effectName, effectDescription, effectDuration;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_effect, null);
                viewHolder = new ViewHolder();
                viewHolder.effectName = (TextView)convertView.findViewById(R.id.nameView);
                viewHolder.effectDuration = (TextView)convertView.findViewById(R.id.durationView);
                viewHolder.effectDescription = (TextView)convertView.findViewById(R.id.descriptionView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder)convertView.getTag();
            }
            Effect effect = getItem(position);
            viewHolder.effectDescription.setText(effect.toString());
            String durationText = getString(R.string.remaining_rounds) + " " + effect.getDuration();
            viewHolder.effectDuration.setText(durationText);

            return convertView;
        }
    }
}
