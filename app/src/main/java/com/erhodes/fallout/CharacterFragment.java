package com.erhodes.fallout;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CharacterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CharacterFragment extends BaseFragment {
    private ListView mListView;
    private TextView mNameView;
    private AttributeAdapter mAdapter;

    public static CharacterFragment newInstance() {
        return new CharacterFragment();
    }

    public CharacterFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_character, container, false);

        mNameView = (TextView)view.findViewById(R.id.charNameView);
        mNameView.setText(mCharacter.name);
        mListView = (ListView)view.findViewById(R.id.attributeListView);
        mAdapter = new AttributeAdapter(getActivity(), R.layout.list_attribute_summary, Attributes.getAllAttributes());
        mListView.setAdapter(mAdapter);
        return view;
    }

    public class AttributeAdapter extends ArrayAdapter<String> {
        int mResourceId;
        Context mContext;
        public AttributeAdapter(Context context, int resourceId, ArrayList<String> attributeList) {
            super(context, resourceId, attributeList);
            mResourceId = resourceId;
            mContext = context;
        }

        protected class ViewHolder {
            TextView nameView;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(mResourceId, null);
                holder = new ViewHolder();
                holder.nameView = (TextView)convertView.findViewById(R.id.textView);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final String string = getItem(position);
            holder.nameView.setText(string + " : " + mCharacter.getAttribute(string));
            return convertView;
        }
    }
}
