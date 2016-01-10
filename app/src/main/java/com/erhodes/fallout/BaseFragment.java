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
    protected CharacterService mCharacterService;
    protected CharacterInterface mInterface;

    public BaseFragment() {
    }

    protected void getCharacterService() {
        try {
            mInterface = (CharacterInterface)getActivity();
            mCharacterService = mInterface.getCharacterService();
            mCharacter = mCharacterService.getActiveCharacter();
        } catch (ClassCastException ex) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement CharacterInterface");
        }
    }
}
