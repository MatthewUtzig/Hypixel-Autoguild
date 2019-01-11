package com.matthewutzig.HypixelAutoGuild;

import java.util.ArrayList;
import java.util.Random;

public class MessageOfTheDay {

    protected static ArrayList<String> motds = new ArrayList<String>();
    protected static boolean initialized = false;

    //return a random motd
    public static String getMOTD() {
        if(!initialized) {
            initialized = true;
            motds.add("This guild has a discord! Join with https://discord.gg/hUAfPmS");
            motds.add("This guild has special rules. Read them at https://discord.gg/hUAfPmS");
            motds.add("Want to invite a friend? tell them to use /guild join MatthewUtzig.");
            motds.add("Want to make a party. Everyone in the guild has access to /guild party");
            motds.add("Tired of seeing people leave/join. Disable it in /g menu -> settings -> personal");
            motds.add("Want special perks? Get the supporter rank. (its free) Instructions in Discord"); //double prob
            motds.add("Want special perks? Get the supporter rank. (its free) Instructions in Discord");
            motds.add("yt");
            motds.add("github");
            motds.add("yt");
            motds.add("github");
        }
        Random rand = new Random();
        return motds.get(rand.nextInt(motds.size()));

    }
}
