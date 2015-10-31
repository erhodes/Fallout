package com.erhodes.fallout;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemFragment extends Fragment {
    private Character mCharacter;
    private CharacterInterface mInterface;
    ListView mListView;
    TextView mWeightView;
    ItemAdapter mAdapter;

    public static ItemFragment newInstance() {
        ItemFragment fragment = new ItemFragment();
        return fragment;
    }

    public ItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        mWeightView = (TextView)view.findViewById(R.id.weightView);
        updateWeightView();

        mAdapter = new ItemAdapter(getActivity(), R.layout.list_item_summary, mCharacter.getInventory());
        mListView = (ListView)view.findViewById(R.id.inventoryListView);
        mListView.setAdapter(mAdapter);

        return view;
    }

    private void updateWeightView() {
        mWeightView.setText("Weight: " + mCharacter.mCarriedWeight + "/" + mCharacter.getAttribute(Attributes.WEIGHT_LIMIT));
    }


}
