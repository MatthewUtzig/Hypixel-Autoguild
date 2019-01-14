package com.matthewutzig.HypixelAutoGuild;

import java.util.ArrayList;

public class BannedPlayers {
    protected static ArrayList<String> banned = new ArrayList<String>();
    protected static boolean initialized = false;

    public static boolean isBanned(String name) {
        if(!initialized) {
            initialized = true;
            banned.add("dicxt");
            banned.add("ilovegifts");
            banned.add("akiara23");
            banned.add("exotico");
            banned.add("flashcan1");
            banned.add("lonelythunderfox");
        }
        return banned.contains(name.toLowerCase());

    }
}
