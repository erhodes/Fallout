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
        try {
            Reader reader = new BufferedReader(new InputStreamReader(context.getAssets().open("perks.json")));
            Gson gson = new GsonBuilder().create();
            Perk[] perkArray = gson.fromJson(reader, Perk[].class);
            for (Perk p : perkArray) {
                addPerk(p);
            }
        } catch (IOException ex) {
            // well shit
        }
    }

    private void addPerk(Perk perk) {
        mPerks.put(perk.id, perk);
    }

    public Perk getPerk(String id) {
        return mPerks.get(id);
    }

    public List<Perk> getPerks(Set<String> iDs) {
        ArrayList<Perk> result = new ArrayList<>();
        for (String perkId : iDs) {
            result.add(getPerk(perkId));
        }
        return result;
    }

}
