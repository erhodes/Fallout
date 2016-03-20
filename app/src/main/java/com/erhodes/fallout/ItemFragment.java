package com.erhodes.fallout;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.erhodes.fallout.model.Attributes;
import com.erhodes.fallout.model.Item;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemFragment extends BaseFragment {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getCharacterService();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        mWeightView = (TextView)view.findViewById(R.id.weightView);
        updateWeightView();

        mAdapter = new ItemAdapter(getActivity(), R.layout.list_item_summary, mCharacter.getInventory());
        mListView = (ListView)view.findViewById(R.id.inventoryListView);
        mListView.setAdapter(mAdapter);

        registerForContextMenu(mListView);
        return view;
    }

    private void updateWeightView() {
        mWeightView.setText("Weight: " + mCharacter.mCarriedWeight + "/" + mCharacter.getAttributeValue(Attributes.WEIGHT_LIMIT));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.item_inventory_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Item i = mAdapter.getItem(info.position);
        switch (item.getItemId()) {
            case R.id.action_equip:
                Log.d("Eric","clicked on item " + i.mDisplayName + " with " + i.effects.size() + " effects");
                mCharacter.equipItem(i);
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.action_drop:
                mCharacter.removeItemFromInventory(i);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    }
