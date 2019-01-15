package com.matthewutzig.HypixelAutoGuild;

import java.util.ArrayList;
import java.util.Random;

public class HypixelPlayCommands {
    protected static ArrayList<String> playCommands = new ArrayList<String>();
    protected static boolean initialized = false;

    //return a random play command. This allows the bot to connect to lobbies of every gamemode
    public static String getPlayCommand() {
        if(!initialized) {
            initialized = true;
            playCommands.add("mw_standard");
              //playCommands.add("blitz_solo_normal");
            playCommands.add("solo_insane");
            playCommands.add("tnt_tntrun");
            playCommands.add("bedwars_eight_one");
             // playCommands.add("arcade_soccer");
              //playCommands.add("mcgo_normal");
            playCommands.add("build_battle_solo_normal");
             // playCommands.add("crazy_walls_solo");
            playCommands.add("uhc_solo");
             // playCommands.add("vampirez");
            playCommands.add("duels_bridge_four");
             // playCommands.add("skyclash_team_war");
             // playCommands.add("speed_solo_insane");
             // playCommands.add("super_smash_solo_normal");
            playCommands.add("murder_classic");
            playCommands.add("prototype_towerwars_solo");
            //playCommands.add("lobby");
        }
        Random rand = new Random();
        String game = playCommands.get(rand.nextInt(playCommands.size()));
        //generate random command
        if(game.equals("lobby")) {
            return "lobby";
        } else {
            return  "play " + game;
        }
    }

}
