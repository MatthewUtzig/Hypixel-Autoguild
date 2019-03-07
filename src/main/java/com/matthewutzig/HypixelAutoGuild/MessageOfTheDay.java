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
            motds.add("Want to invite a friend? tell them to use /guild join SulphurAdam");
            motds.add("Want to make a party. Everyone in the guild has access to /guild party");
            motds.add("Want special perks? Get the supporter rank. (its free) https://discord.gg/hUAfPmS");
            motds.add("Want special perks? Get the supporter rank. (its free) https://discord.gg/hUAfPmS");
            motds.add("This guild has a youtube. Subscribe at https://www.youtube.com/user/mattzzigster");
            motds.add("This guild has a youtube. Subscribe at https://www.youtube.com/user/mattzzigster");
            motds.add("Follow the guild github at https://github.com/MatthewUtzig");
            motds.add("Follow the guild github at https://github.com/MatthewUtzig");
        }
        Random rand = new Random();
        return motds.get(rand.nextInt(motds.size()));

    }
}
