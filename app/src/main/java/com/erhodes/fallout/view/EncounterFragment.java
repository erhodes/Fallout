package com.erhodes.fallout.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.erhodes.fallout.BaseFragment;
import com.erhodes.fallout.R;

import com.erhodes.fallout.model.Action;
import com.erhodes.fallout.model.Attributes;
import com.erhodes.fallout.model.Character;
import com.erhodes.fallout.model.GameLog;
import com.erhodes.fallout.model.TargetGroup;
import com.erhodes.fallout.presenter.EncounterContract;
import com.erhodes.fallout.presenter.EncounterPresenter;

import java.util.List;

/**
 * Created by Eric on 03/04/2016.
 */
public class EncounterFragment extends BaseFragment implements AdapterView.OnItemSelectedListener, EncounterContract.View, GameLog.GameLogListener {
    private EncounterContract.UserActionListener mActionListener;
    CharacterAdapter mCharacterAdapter;
    ActionAdapter mActionAdapter;
    ArrayAdapter mLogAdapter;
    ListView mListView, mLogView;
    TextView mApCountView;
    Spinner mActionSpinner;
    GameLog mLog;
    LinearLayout mTargetButtons;
    Button mPerformButton;
    Button[] mButtons = new Button[3];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getCharacterService();
        mActionListener = new EncounterPresenter(this, mCharacterService);
        mLog = GameLog.getInstance();
        mLog.registerListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLog.unregisterListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_encounter, null);
        mCharacterAdapter = new CharacterAdapter(mCharacterService.getNonActiveCharacters());
        mListView = (ListView)view.findViewById(R.id.listView);
        mListView.setAdapter(mCharacterAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mActionListener.toggleTarget(mCharacterAdapter.getItem(position));
            }
        });

        mLogView = (ListView)view.findViewById(R.id.logView);
        mLogAdapter = new LogAdapter(getActivity(), mLog.getLogs());
        mLogView.setAdapter(mLogAdapter);
        mLogView.setSelection(mLogAdapter.getCount()-1);

        mTargetButtons = (LinearLayout)view.findViewById(R.id.targetButtonLayout);

        mActionAdapter = new ActionAdapter(getActivity(), mCharacter.getActions());
        mActionSpinner = (Spinner)view.findViewById(R.id.actionSpinner);
        mActionSpinner.setAdapter(mActionAdapter);
        mActionSpinner.setOnItemSelectedListener(this);

        mApCountView = (TextView)view.findViewById(R.id.apCountView);

        // should probably redo this with a recycler view
        mButtons[0] = (Button)view.findViewById(R.id.button);
        mButtons[1] = (Button)view.findViewById(R.id.button2);
        mButtons[2] = (Button)view.findViewById(R.id.button3);

        for (int i = 0; i < mButtons.length; i++) {
            mButtons[i].setOnClickListener(mTargetButtonListener);
            mButtons[i].setTag(i);
        }

        mPerformButton = (Button)view.findViewById(R.id.performButton);
        mPerformButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionListener.performAction();
            }
        });

        Button turnButton = (Button)view.findViewById(R.id.turnButton);
        turnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionListener.endTurn();
            }
        });
        update();
        return view;
    }

    public void update() {
        mCharacterAdapter.notifyDataSetChanged();
        String text = getString(R.string.remaining_ap) + mCharacter.mActionPoints;
        mApCountView.setText(text);
        mPerformButton.setEnabled(mActionListener.sufficientApRemaining());

        for (int i = 0; i < mActionListener.getTargetGroupCount(); i++) {
            TargetGroup targetGroup = mActionListener.getTargetGroup(i);
            text = targetGroup.getName();
            if (targetGroup.getMinTargets() == targetGroup.getMaxTargets()) {
                text += " (" + targetGroup.getCurrentTargetCount() + " of " + targetGroup.getMaxTargets() + ")";
            } else {
                text += " (" + targetGroup.getMinTargets() + " - " + targetGroup.getMaxTargets() + ")";
            }
            mButtons[i].setText(text);
        }
    }

    public void newEvent() {
        mLogAdapter.notifyDataSetChanged();
        if (mLogView != null)
            mLogView.setSelection(mLogAdapter.getCount()-1);
    }

    // candidate for removal
    public View.OnClickListener mTargetButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int selectedGroup = (int)v.getTag();
            mActionListener.setActiveTargetGroup(selectedGroup);
        }
    };

    public void setTargetButtons(List<String> buttons) {
        //maybe this should use some kind of recycler view?
        for (int i = 0; i < buttons.size(); i++) {
            mButtons[i].setVisibility(View.VISIBLE);
        }
        for (int i = buttons.size(); i < mButtons.length; i++) {
            mButtons[i].setVisibility(View.INVISIBLE);
        }
    }

    public void setActiveTargetGroup(int group) {
        if (group >= mButtons.length)
            return;
        for (int i = 0; i < mButtons.length; i ++) {
            mButtons[i].setBackgroundColor(Color.GRAY);
        }
        mButtons[group].setBackgroundColor(Color.RED);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mActionListener.onActionSelected((Action)mActionAdapter.getItem(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    class LogAdapter extends ArrayAdapter {
        List<String> mLogs;

        public LogAdapter(Context context, List<String> logs) {
            super(context, R.layout.list_action, logs);
        }
    }

    class ActionAdapter extends ArrayAdapter {
        List<Action> mActions;
        Context mContext;

        public ActionAdapter(Context context, List<Action> actions) {
            super(context,R.layout.list_action, actions);
            mActions = actions;
            mContext = context;
        }

        private View getActionView(int position, View convertView) {
            if (convertView == null) {
                convertView = ((LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_action, null);
            }
            Action action = (Action)getItem(position);
            String text = action.name + " (" + action.cost + ")";
            ((TextView)convertView).setText(text);
            return convertView;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getActionView(position, convertView);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getActionView(position, convertView);
        }
    }

    class CharacterAdapter extends BaseAdapter {
        List<Character> mCharacters;

        public CharacterAdapter(List<Character> characters) {
            mCharacters = characters;
        }

        @Override
        public int getCount() {
            return mCharacters.size();
        }

        @Override
        public Character getItem(int position) {
            return mCharacters.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        protected class ViewHolder {
            TextView nameView, healthView;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.list_character_summary, null);
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
            if (mActionListener.isCharacterTargetted(character)) {
                convertView.setBackgroundColor(mActionListener.getActiveTargetGroupColor());
            } else {
                convertView.setBackgroundColor(Color.WHITE);
            }
            return convertView;
        }
    }
}
