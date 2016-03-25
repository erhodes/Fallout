package com.erhodes.fallout.view;


import android.content.Context;
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
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.erhodes.fallout.BaseFragment;
import com.erhodes.fallout.ItemAdapter;
import com.erhodes.fallout.R;
import com.erhodes.fallout.model.Attributes;
import com.erhodes.fallout.model.Item;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InventoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InventoryFragment extends BaseFragment {
    ListView mListView;
    TextView mWeightView;
    ItemAdapter mAdapter;

    public static InventoryFragment newInstance() {
        InventoryFragment fragment = new InventoryFragment();
        return fragment;
    }

    public InventoryFragment() {
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

        mAdapter = new ItemAdapter(mCharacter.getInventory());
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

    public class ItemAdapter extends BaseAdapter {
        List<Item> mItems;

        public ItemAdapter(List<Item> items) {
            mItems = items;
        }
        protected class ViewHolder {
            TextView nameView, descriptionView, quantityView;
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Item getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.list_item_summary, null);
                holder = new ViewHolder();
                holder.nameView = (TextView) convertView.findViewById(R.id.nameView);
                holder.descriptionView = (TextView) convertView.findViewById(R.id.descView);
                holder.quantityView = (TextView) convertView.findViewById(R.id.itemQuanityView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final Item item = getItem(position);
            holder.nameView.setText(item.mDisplayName);
            holder.descriptionView.setText(item.mDescription);
            holder.quantityView.setText(String.format("%d",item.getQuantity()));
            return convertView;
        }
    }
}
