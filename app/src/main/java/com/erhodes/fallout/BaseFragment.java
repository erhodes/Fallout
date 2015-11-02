package com.erhodes.fallout;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment {
    protected Character mCharacter;
    protected CharacterInterface mInterface;

    public BaseFragment() {
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
}
