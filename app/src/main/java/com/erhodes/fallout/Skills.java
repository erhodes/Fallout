package com.erhodes.fallout;

import java.util.ArrayList;

/**
 * Created by Eric on 12/01/2016.
 */
public class Skills {
    public static final String GUNS = "guns";
    public static final String MEDICINE = "medicine";
    public static final String MELEE = "melee";

    public static ArrayList<String> getAllSkills() {
        ArrayList<String> result = new ArrayList<>();
        result.add(GUNS);
        result.add(MEDICINE);
        result.add(MELEE);
        return result;
    }
}
