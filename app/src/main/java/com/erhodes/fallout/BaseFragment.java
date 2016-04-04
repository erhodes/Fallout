package com.erhodes.fallout;


import android.support.v4.app.Fragment;

import com.erhodes.fallout.view.FragmentLifecycle;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment implements FragmentLifecycle {
    protected com.erhodes.fallout.model.Character mCharacter;
    protected CharacterService mCharacterService;
    protected CharacterInterface mInterface;

    public BaseFragment() {
    }

    @Override
    public void onResumeFragment(){
    }

    @Override
    public void onPauseFragment() {}

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
