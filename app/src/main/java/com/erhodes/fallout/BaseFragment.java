package com.erhodes.fallout;


import android.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment {
    protected com.erhodes.fallout.model.Character mCharacter;
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
