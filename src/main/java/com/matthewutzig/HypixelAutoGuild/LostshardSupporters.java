package com.matthewutzig.HypixelAutoGuild;

import java.util.ArrayList;
import java.util.Random;

public class LostshardSupporters {

    protected static ArrayList<String> supporters = new ArrayList<String>();
    protected static boolean initialized = false;

    //return if supporter exists
    public static boolean isSupporter(String name) {
        if(!initialized) {
            initialized = true;
            supporters.add("ilovegifts");
            supporters.add("jedleo");
            supporters.add("_chlxe");
            supporters.add("astoeth");
        }
        return supporters.contains(name.toLowerCase());

    }
}
