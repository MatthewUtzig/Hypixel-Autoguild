package com.matthewutzig.HypixelAutoGuild;

import java.util.ArrayList;
import java.util.Random;

public class HypixelPlayCommands {
    protected static ArrayList<String> popularGames = new ArrayList<String>();
    protected static ArrayList<String> unpopularGames = new ArrayList<String>();
    protected static boolean initialized = false;

    //return a random play command. This allows the bot to connect to lobbies of every gamemode
    public static String getPlayCommand() {
        if(!initialized) {
            initialized = true;
            popularGames.add("mw_standard");
            unpopularGames.add("blitz_solo_normal");
            popularGames.add("solo_insane");
            popularGames.add("tnt_tntrun");
            popularGames.add("bedwars_eight_one");
            unpopularGames.add("arcade_soccer");
            unpopularGames.add("mcgo_normal");
            popularGames.add("build_battle_solo_normal");
            unpopularGames.add("crazy_walls_solo");
            popularGames.add("uhc_solo");
            unpopularGames.add("vampirez");
            popularGames.add("duels_bridge_four");
            unpopularGames.add("skyclash_team_war");
            unpopularGames.add("speed_solo_insane");
            unpopularGames.add("super_smash_solo_normal");
            popularGames.add("murder_classic");
            popularGames.add("prototype_towerwars_solo");
            unpopularGames.add("lobby");
        }
        Random rand = new Random();
        String game;
        /*
            6/9 chance of choosing a popular game
            2/9 chance of choosing an unpopular game
            1/9 chance of choosing the main lobby
        */
        int gameType = rand.nextInt(9);
        if(gameType <= 5) {
            game = popularGames.get(rand.nextInt(popularGames.size()));
        } else if (gameType <= 7) {
            game = unpopularGames.get(rand.nextInt(unpopularGames.size()));
        } else {
            game = "lobby";
        }

        //generate the command
        if(game.equals("lobby")) {
            return "lobby";
        } else {
            return  "play " + game;
        }
    }

}
