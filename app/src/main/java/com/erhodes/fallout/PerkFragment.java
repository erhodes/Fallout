package com.erhodes.fallout;


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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerkFragment extends Fragment implements AbsListView.OnItemClickListener{
    ArrayList<Perk> mPerkList;
    ListView mPerkView;
    PerkAdapter mAdapter;

    public static PerkFragment newInstance() {
        PerkFragment fragment = new PerkFragment();
        return fragment;
    }

    public PerkFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Load in the perks. TODO: Load them in somewhere better
        mPerkList = new ArrayList<Perk>();
        try {
            Reader reader = new BufferedReader(new InputStreamReader(getActivity().getAssets().open("perks.xml")));
            Gson gson = new GsonBuilder().create();
            Perk[] perkArray = gson.fromJson(reader, Perk[].class);
            Log.d("Eric","total perks read: " + perkArray.length);
            for (Perk p : perkArray) {
                mPerkList.add(p);
            }
        } catch (IOException ex) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perk, container, false);

        mPerkView = (ListView)view.findViewById(R.id.perkList);
        mAdapter = new PerkAdapter(getActivity(), R.layout.list_perk_summary, mPerkList);
        mPerkView.setAdapter(mAdapter);
        mPerkView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Perk perk = (Perk)parent.getItemAtPosition(position);
        Log.d("Eric","perk " + perk.name + " has the following effects: ");
        if (perk.effects != null) {
            for (Effect e : perk.effects) {
                Log.d("Eric",e.toString());
            }
        }
    }

    public class PerkAdapter extends ArrayAdapter<Perk> {int mResourceId;
        Context mContext;
        public PerkAdapter(Context context, int resourceId, ArrayList<Perk> creatureList) {
            super(context, resourceId, creatureList);
            mResourceId = resourceId;
            mContext = context;
        }

        protected class ViewHolder {
            TextView nameView, descView;
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
