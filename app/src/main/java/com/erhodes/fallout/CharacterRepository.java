package com.erhodes.fallout;

import android.arch.lifecycle.LiveData;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.erhodes.fallout.database.CharacterDao;
import com.erhodes.fallout.database.MyDatabase;
import com.erhodes.fallout.model.*;
import com.erhodes.fallout.model.Character;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Eric on 19/12/2015.
 */
@Singleton
public class CharacterRepository {
    private static final String TAG = "CharacterRepository";
    private static final String PREF_ACTIVE_CHARACTER = "pref_active_character";

    private static final int MESSAGE_SAVE = 0;

    private ArrayList<Character> mCharacters;
    private LiveData<List<Character>> mCharactersLiveData;
    private LiveData<Character> mActiveCharacterData;
    private CharacterDao mCharacterDao;
    private SharedPreferences mSharedPreferences;

    private DatabaseHandler mHandler;


    @Inject
    public CharacterRepository(MyDatabase database, SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
        mCharacters = new ArrayList<>();
        mCharacterDao = database.characterDao();
        mCharactersLiveData = mCharacterDao.loadAll();

        HandlerThread handlerThread = new HandlerThread("databaseThread", Process.THREAD_PRIORITY_BACKGROUND);
        handlerThread.start();
        mHandler = new DatabaseHandler(handlerThread.getLooper(), this);

        long activeCharId = mSharedPreferences.getLong(PREF_ACTIVE_CHARACTER, -1);
        if (activeCharId != 1) {
            Log.d("Eric", "loading character " + activeCharId);
            mActiveCharacterData = mCharacterDao.load(activeCharId);
        }
    }

    public void resetDatabase() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                // remove the old stuff
                for (Character character : mCharactersLiveData.getValue()) {
                    mCharacterDao.delete(character);
                }

                Character mainChar = new Character("Johanson");
                Item poison = ItemManager.getItem(ItemManager.ITEM_POISON);
                mainChar.acquireItem(poison);
                mainChar.equipItem(poison);
                Item poisonGun = ItemManager.getItem(ItemManager.ITEM_POISON_GUN);
                mainChar.acquireItem(poisonGun);
                Item revolver = ItemManager.getItem(ItemManager.ITEM_REVOLVER);
                mainChar.acquireItem(revolver);
                mainChar.equipItem(revolver);
                Item revolverAmmo = ItemManager.getItem(ItemManager.ITEM_38_ROUND);
                mainChar.acquireItem(revolverAmmo);
                mainChar.acquireItem(revolverAmmo);
                //mainChar.acquireItem(ItemManager.getItem(ItemManager.ITEM_LEATHER_ARMOR));
                mainChar.applyPerk(PerkManager.getInstance().getPerk(PerkManager.PERK_STRENGTH));
                mainChar.currentExperience = 6;
                mainChar.availablePerks++;
                for (int i = 0; i < 5; i++) {
                    mainChar.addRank(Skills.GUNS);
                }

                mCharacters.add(new Character("Raider 1"));
                mCharacters.add(new Character("Raider 2"));

                Character ally = new Character("Ally 1");
//                ally.applyEffect(new Effect(Attributes.HEALTH, -15));
                mCharacters.add(ally);

                // hack to get something into the database
                long x = mCharacterDao.save(mainChar);
                setActiveCharacter(x);
                return null;
            }
        }.execute();
    }

    /**
     * Set a new active character. The character must already exist in the database
     * @param newId
     */
    public void setActiveCharacter(long newId) {
        Log.d("Eric","active char is now " + newId);
        mSharedPreferences.edit().putLong(PREF_ACTIVE_CHARACTER, newId).apply();
        mActiveCharacterData = mCharacterDao.load(newId);
    }

    public LiveData<Character> getActiveCharacter() {
        return mActiveCharacterData;
    }

    public void addPerkToCharacter(Character character, Perk perk) {
        if (character.acquirePerk(perk)) {
            saveCharacter(character);
        }
    }

    public void addSkillRankToCharacter(@Nonnull Character character, String skillKey) {
        //todo consider how to handle synchronizing saves, so different versions of the same character don't get added at the same time
        if (character.addRank(skillKey)) {
            saveCharacter(character);
        }
    }

    public ArrayList<Character> getNonActiveCharacters() {
        return mCharacters;
    }

    public ArrayList<Character> getAllCharacters() {
        ArrayList<Character> result = new ArrayList<>(mCharacters);
        result.add(mActiveCharacterData.getValue());
        return result;
    }

    public LiveData<List<Character>> getCharactersLiveData() {
        return mCharactersLiveData;
    }

    public void saveCharacter(final Character character) {
        Message message = new Message();
        message.what = MESSAGE_SAVE;
        message.obj = character;
        mHandler.sendMessage(message);
    }

    private void saveCharacterInternal(Character character) {
        mCharacterDao.save(character);
    }

    private static final class DatabaseHandler extends Handler {
        private final CharacterRepository repository;

        DatabaseHandler(Looper looper, CharacterRepository repository) {
            super(looper);
            this.repository = repository;
        }

        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case MESSAGE_SAVE:
                    Character character = (Character) message.obj;
                    repository.saveCharacterInternal(character);
                    break;
                default:
            }
        }
    }
}
