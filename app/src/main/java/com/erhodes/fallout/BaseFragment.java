package com.erhodes.fallout;


import android.support.v4.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment {
    protected com.erhodes.fallout.model.Character mCharacter;
    protected CharacterRepository mCharacterRepository;

    public BaseFragment() {
    }

    protected void getCharacterService() {
        mCharacterRepository = MyApplication.getComponent().getCharacterRepo();
        mCharacter = mCharacterRepository.getActiveCharacter().getValue();
    }
}
