package com.erhodes.fallout;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Eric on 24/10/2015.
 */
public class ItemAdapter extends ArrayAdapter<Item> {
    int mResourceId;
    Activity mActivity;
    public ItemAdapter(Activity activity, int resourceId, ArrayList<Item> itemList) {
        super(activity, resourceId, itemList);
        mResourceId = resourceId;
        mActivity = activity;
    }

    protected class ViewHolder {
        TextView nameView, descriptionView, quantityView;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = mActivity.getLayoutInflater();
            convertView = inflater.inflate(mResourceId, null);
            holder = new ViewHolder();
            holder.nameView = (TextView)convertView.findViewById(R.id.nameView);
            holder.descriptionView = (TextView)convertView.findViewById(R.id.descView);
            holder.quantityView = (TextView)convertView.findViewById(R.id.itemQuanityView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Item item = getItem(position);
        holder.nameView.setText(item.mDisplayName);
        holder.descriptionView.setText(item.mDescription);
        holder.quantityView.setText("1");
        return convertView;
    }
}
