package com.erhodes.fallout;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class EquipmentFragment extends BaseFragment {


    public EquipmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_equipment, container, false);

        TextView armorDesc = (TextView)view.findViewById(R.id.armorDescView);
        armorDesc.setText(mCharacter.getArmor().description);

        TextView armorName = (TextView)view.findViewById(R.id.armorNameView);
        armorName.setText(mCharacter.getArmor().displayName);


        LinearLayout test = (LinearLayout)view.findViewById(R.id.armorView);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Eric", "well that works");
            }
        });

        return view;
    }


}
