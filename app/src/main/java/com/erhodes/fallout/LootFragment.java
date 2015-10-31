package com.erhodes.fallout;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class LootFragment extends Fragment {
    ListView mListView;
    ItemAdapter mAdapter;
    Character mCharacter;
    CharacterInterface mInterface;

    public static LootFragment newInstance() {
        return new LootFragment();
    }

    public LootFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mInterface = (CharacterInterface)activity;
            mCharacter = mInterface.getCharacter();
        } catch (ClassCastException ex) {
            throw new ClassCastException(activity.toString()
                    + " must implement CharacterInterface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loot, container, false);

        mAdapter = new ItemAdapter(getActivity(), R.layout.list_item_summary, ItemManager.getAllItems());
        mListView = (ListView)view.findViewById(R.id.lootListView);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCharacter.acquireItem((Item)mListView.getItemAtPosition(position));
            }
        });

        return view;
    }


}
