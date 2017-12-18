package com.erhodes.fallout.model;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by Eric on 19/03/2016.
 */
public class PerkManager {
    public static final String PERK_STRENGTH = "gain_str";
    public static final String PERK_ENDURANCE = "gain_end";

    private HashMap<String, Perk> mPerks;

    private static PerkManager sInstance;

    public static PerkManager getInstance() {
        if (sInstance == null) {
            sInstance = new PerkManager();
        }
        return sInstance;
    }

    private PerkManager() {
        mPerks = new HashMap<>();
    }

    //TODO: this is a horrible hack! Maybe this should be an actual service or something?
    public void loadPerks(Context context) {
        Perk strPerk = new Perk(PERK_STRENGTH,"Gain Strength", "Grants +1 Strength");
        strPerk.effects.add(new Effect(Attributes.STRENGTH, 1));
        addPerk(strPerk);

        Perk endPerk = new Perk(PERK_ENDURANCE,"Gain Endurance", "Grants +1 Endurance");
        endPerk.effects.add(new Effect(Attributes.ENDURANCE, 1));
        addPerk(endPerk);

        //TODO not only is this a hack, but the gson serializer seems to have broken.
//        try {
//            Reader reader = new BufferedReader(new InputStreamReader(context.getAssets().open("perks.json")));
//            Gson gson = new GsonBuilder().create();
//            Perk[] perkArray = gson.fromJson(reader, Perk[].class);
//            for (Perk p : perkArray) {
//                addPerk(p);
//            }
//        } catch (IOException ex) {
//            // well shit
//        }
    }

    private void addPerk(Perk perk) {
        mPerks.put(perk.id, perk);
    }

    public Perk getPerk(String id) {
        return mPerks.get(id);
    }

    public List<Perk> getUnacquiredPerks(Character character) {
        ArrayList<Perk> result = new ArrayList<>();
        for (Perk perk : mPerks.values()) {
            if (!character.hasPerk(perk)) {
                result.add(perk);
            }
        }
        return result;
    }

    public List<Perk> getPerks(Set<String> iDs) {
        ArrayList<Perk> result = new ArrayList<>();
        for (String perkId : iDs) {
            result.add(getPerk(perkId));
        }
        return result;
    }

}
