package com.erhodes.fallout.view.inventory;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.erhodes.fallout.CharacterRepository;
import com.erhodes.fallout.R;
import com.erhodes.fallout.model.Character;
import com.erhodes.fallout.model.Item;
import com.erhodes.fallout.presenter.InventoryContract;
import com.erhodes.fallout.presenter.InventoryPresenter;
import com.erhodes.fallout.view.encounter.EncounterViewModel;
import com.erhodes.fallout.view.perk.PerkViewModel;

import java.util.ArrayList;
import java.util.List;


/**
 * A fragment to display items currently in the character's inventory, as opposed to those equipped.
 */
public class InventoryFragment extends Fragment implements InventoryContract.View {
    InventoryContract.UserActionListener mActionListener;
    ListView mListView;
    TextView mWeightView;
    ItemAdapter mAdapter;
    private InventoryViewModel mViewModel;
    private Character mCharacter;

    public static InventoryFragment newInstance() {
        InventoryFragment fragment = new InventoryFragment();
        return fragment;
    }

    public InventoryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(InventoryViewModel.class);
        mViewModel.getCharacter().observe(this, new Observer<Character>() {
            @Override
            public void onChanged(@Nullable Character character) {
                mCharacter = character;
                update();
            }
        });

        mActionListener = new InventoryPresenter(getActivity(), mViewModel.getRepo(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        mWeightView = (TextView)view.findViewById(R.id.weightView);

        mAdapter = new ItemAdapter(new ArrayList<Item>());

        mListView = (ListView)view.findViewById(R.id.inventoryListView);
        mListView.setAdapter(mAdapter);

        registerForContextMenu(mListView);

        return view;
    }

    public void update() {
        mWeightView.setText(mActionListener.getWeightString());
        mAdapter.updateDataSet(mCharacter.getInventory());
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
                mActionListener.equipItem(i);
                return true;
            case R.id.action_drop:
                mActionListener.dropItem(i);
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

        public void updateDataSet(List<Item> items) {
            mItems = items;
            notifyDataSetChanged();
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
