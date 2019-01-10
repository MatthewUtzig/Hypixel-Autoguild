package com.matthewutzig.HypixelAutoGuild;

import java.util.ArrayList;
import java.util.Random;

public class MessageOfTheDay {

    protected static ArrayList<String> motds = new ArrayList<String>();
    protected static boolean initialized = false;

    //return a random motd
    public static String getMOTD() {
        if(!initialized) {S
            initialized = true;
            motds.add("This guild has a discord! Join with https://discord.gg/hUAfPmS");
            motds.add("This guild has special rules. Read them at https://discord.gg/hUAfPmS");
            motds.add("Want to invite a friend? tell them to use /guild join MatthewUtzig.");
            motds.add("Tired of being invited? Disable guild invites in 'my profile'");
            motds.add("Want to make a party. Everyone in the guild has access to /guild party");
        }
        Random rand = new Random();
        return motds.get(rand.nextInt(motds.size()));

    }
}
