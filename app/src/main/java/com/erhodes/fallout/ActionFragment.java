package com.erhodes.fallout;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.erhodes.fallout.model.*;
import com.erhodes.fallout.model.Character;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ActionFragment extends BaseFragment implements AbsListView.OnItemClickListener{
    ListView mListView;
    TextView mAPView, mHealthView;
    ActionAdapter mAdapter;

    public ActionFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getCharacterService();
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_action, container, false);

        mAPView = (TextView)view.findViewById(R.id.apCountView);
        mHealthView = (TextView)view.findViewById(R.id.healtView);
        mAdapter = new ActionAdapter(getActivity(), R.layout.list_action_summary, mCharacter.getActions());
        mListView = (ListView)view.findViewById(R.id.listView);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        Button newRound = (Button)view.findViewById(R.id.button);
        newRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCharacter.newTurn();
                update();
            }
        });

        update();
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Action action = (Action)parent.getItemAtPosition(position);

        final com.erhodes.fallout.model.Character target;

        ArrayList<TargetGroup> targetGroups = action.getDynamicTargetGroups();
        if (targetGroups.size() > 0) {
            final TargetGroup mainTargetGroup = targetGroups.get(0);
            final CharacterAdapter characterAdapter = new CharacterAdapter(getActivity(), R.layout.list_character_summary, mCharacterService.getNonActiveCharacters());
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Select " + mainTargetGroup.getName())
                    .setAdapter(characterAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("Eric","choosing a new target");
                            mainTargetGroup.addTarget((Character) characterAdapter.getItem(which));
                            Log.d("Eric","performing an action!");
                            actionPerformed(action.performAction(mCharacter));
                        }
                    });
            builder.create().show();
        } else {
            actionPerformed(action.performAction(mCharacter));

        }
    }

    private void actionPerformed(int result) {
        switch (result) {
            case Action.RESULT_INSUFFICIENT_AP:
                Toast.makeText(getActivity(), "Not enough AP to perform that action", Toast.LENGTH_SHORT).show();
                break;
            case Action.RESULT_MISSING_TARGETS:
                Toast.makeText(getActivity(), "Wrong number of targets", Toast.LENGTH_SHORT).show();
                break;
            case Action.RESULT_PASSED:
                Toast.makeText(getActivity(), "Passed the check", Toast.LENGTH_SHORT).show();
                break;
            case Action.RESULT_FAILED_CHECK:
                Toast.makeText(getActivity(), "Failed the check", Toast.LENGTH_SHORT).show();
                break;
            case Action.RESULT_UNABLE_TO_PAY_COSTS:
                Toast.makeText(getActivity(), "Unable to pay all costs", Toast.LENGTH_SHORT).show();
                break;
        }
        update();
        mAdapter.notifyDataSetChanged();
    }

    private void update() {
        mAPView.setText("Action Points: " + mCharacter.mActionPoints + "/" + mCharacter.getAttributeValue(Attributes.ACTION_POINTS));
        mHealthView.setText("Health: " + mCharacter.getAttributeValue(Attributes.HEALTH) + "/" + mCharacter.getAttributeValue(Attributes.MAX_HEALTH));
    }

    public class ActionAdapter extends ArrayAdapter<Action> {
        int mResourceId;

        public ActionAdapter(Activity activity, int resourceId, ArrayList<Action> actionList) {
            super(activity, resourceId, actionList);
            mResourceId = resourceId;
        }

        protected class ViewHolder {
            TextView nameView, costView, descView;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(mResourceId, null);
                holder = new ViewHolder();
                holder.nameView = (TextView)convertView.findViewById(R.id.nameView);
                holder.descView = (TextView)convertView.findViewById(R.id.descView);
                holder.costView = (TextView)convertView.findViewById(R.id.costView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final Action action = getItem(position);
            holder.nameView.setText(action.name);
            holder.descView.setText(action.description);
            holder.costView.setText(action.cost + " AP");
            return convertView;
        }
    }

    public class CharacterAdapter extends ArrayAdapter<Character> {
        int mResourceId;
        public CharacterAdapter(Activity activity, int resourceId, ArrayList<Character> characterList) {
            super(activity, resourceId, characterList);
            mResourceId = resourceId;
        }
        protected class ViewHolder {
            TextView nameView, healthView;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(mResourceId, null);
                holder = new ViewHolder();
                holder.nameView = (TextView) convertView.findViewById(R.id.nameView);
                holder.healthView = (TextView) convertView.findViewById(R.id.healthView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final Character character = getItem(position);
            holder.nameView.setText(character.name);
            holder.healthView.setText(character.getAttributeValue(Attributes.HEALTH) + "/" + character.getAttributeValue(Attributes.MAX_HEALTH));
            return convertView;
        }
    }

}
