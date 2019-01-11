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
            supporters.add("advocat_");
            supporters.add("stringsoffantasy");
            supporters.add("distillatez");
            supporters.add("nerdycombos");
            supporters.add("garygary1275");
            supporters.add("richienb");
            supporters.add("cannedmeats");
            supporters.add("shacor");
            supporters.add("evetimi");
            supporters.add("spheredsphered10");
            supporters.add("xdavidet");
            supporters.add("swarnava");
        }
        return supporters.contains(name.toLowerCase());

    }
}
