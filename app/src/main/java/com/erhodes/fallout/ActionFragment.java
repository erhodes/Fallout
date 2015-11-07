package com.erhodes.fallout;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ActionFragment extends BaseFragment implements AbsListView.OnItemClickListener{
    ListView mListView;
    TextView mAPView;
    ActionAdapter mAdapter;

    public ActionFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_action, container, false);

        mAPView = (TextView)view.findViewById(R.id.apCountView);
        mAdapter = new ActionAdapter(getActivity(), R.layout.list_action_summary, mCharacter.getActions());
        mListView = (ListView)view.findViewById(R.id.listView);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        updateAP();
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Action action = (Action)parent.getItemAtPosition(position);

        if (!mCharacter.takeAction(action)) {
            Toast.makeText(getActivity(),"Not enough AP to perform that action",Toast.LENGTH_SHORT).show();
        }
        updateAP();;
    }

    private void updateAP() {
        mAPView.setText("Action Points: " + mCharacter.mActionPoints + "/" + mCharacter.getAttribute(Attributes.ACTION_POINTS));
    }

    public class ActionAdapter extends ArrayAdapter<Action> {
        Context mContext;
        int mResourceId;

        public ActionAdapter(Activity activity, int resourceId, ArrayList<Action> actionList) {
            super(activity, resourceId, actionList);
            mContext = activity;
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

}