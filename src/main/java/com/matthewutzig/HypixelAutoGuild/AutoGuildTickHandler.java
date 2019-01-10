package com.matthewutzig.HypixelAutoGuild;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;

public class AutoGuildTickHandler {
    protected static final int PRUNE_LIMIT = 120; //the maximum number of players in the guild before pruning starts.
    protected static final int PRUNE_COUNT = 10; //the number of members to prune.
    protected static final boolean CHAT_DEBUG = true; //output all unformatted chat

    protected static ArrayList<String> messages = new ArrayList<String>();
    protected static int tickRemaining = 0;


    protected static boolean newLobby = true; //stores whether to warp to new lobby
    protected static boolean guildMembers = false; //stores whether the next chat messages will be the list of guild members
    protected static boolean abovePruneLimit = false; //stores whether to begin the pruning process
    protected static boolean skipPruneCheck = false;
    protected static boolean guildCheck = true; //used for determining when to switch lobbies

    public static boolean scriptEnabled = false;

    @SubscribeEvent
    public void tickEvent(TickEvent.ClientTickEvent event) {
        this.runCommand();
    }

    //handle guild messages
    @SubscribeEvent
    public void messageEvent(ClientChatReceivedEvent event) {
        //make sure script is enabled
        if(scriptEnabled) {
            String message = event.message.getFormattedText();
            if (CHAT_DEBUG) {
                System.out.println(message);
            }


            if(!skipPruneCheck) {
                if (abovePruneLimit) {
                    //check if chat message contains guild members
                    if (guildMembers) {
                        guildMembers = false;
                        abovePruneLimit = false;
                        skipPruneCheck = true;
                        //replace all reset formatters and spaces and split online members into array.
                        message = message.replace(" ", "");
                        message = message.replace("\u00a7r", "");
                        String[] splitMessage = message.split("\u25cf");
                        //strip color code for name
                        for (int i = 0; i < splitMessage.length; i++) {
                            splitMessage[i] = splitMessage[i].substring(2);
                        }
                        //strip rank tag if it exists
                        for (int i = 0; i < splitMessage.length; i++) {
                            if (splitMessage[i].contains("]")) {
                                splitMessage[i] = splitMessage[i].substring(splitMessage[i].indexOf("]") + 2);
                            }
                        }
                        int kickCount = 0; //make sure not to kick too many
                        //only color code left is red. Get if it is light red (offline)
                        for (int i = 0; i < splitMessage.length; i++) {
                            if (splitMessage[i].contains("\u00a7c")) {
                                //add kick player to command queue
                                String playerName = splitMessage[i].substring(0, splitMessage[i].indexOf("\u00a7c"));
                                System.out.println("Added " + playerName + " to the kick queue.");
                                messages.add("guild kick " + playerName + " you were kicked for inactivity (not personal). To rejoin, find f_w.");
                                kickCount++;
                                if (kickCount >= PRUNE_COUNT) {
                                    return;
                                }
                            }
                        }
                    } else if (message.contains("-- Member --")) {
                        //next message will contain guild members
                        guildMembers = true;
                    }
                } else {
                    System.out.println(message);
                    //check guild member count
                    if (message.contains("\u00a7eTotal Members: \u00a7r\u00a7a")) {
                        System.out.println("Looking for members");
                        //get number only
                        message = message.substring(message.indexOf("\u00a7a") + 2, message.indexOf("\u00a7a") + 5);
                        //fix numbers with only 2 digits
                        if (message.contains(("\u00a7"))) {
                            message = message.substring(0, 2);
                        }
                        try {
                            System.out.println("Detected " + Integer.parseInt(message) + " members in the guild");
                            if (Integer.parseInt(message) > PRUNE_LIMIT) {
                                //commence pruning
                                abovePruneLimit = true;
                                messages.add("guild list");
                            }
                        } catch (Exception e) {
                            System.out.println("Error getting total guild members. " + message);
                        }
                    }
                }
            }
        }
    }

    public static void runCommand() {
        //make sure script is enabled
        if(scriptEnabled) {
            //check if three seconds have elapsed
            tickRemaining--;
            if (tickRemaining <= 0) {
                //set counter back to 30 ticks
                tickRemaining = 30;
                if (messages.size() > 0) {
                    //remove last message from arrayList
                    String lastMessage = messages.get(messages.size() - 1);
                    messages.remove(lastMessage);

                    //send message
                    System.out.println("Executing command " + "/" + lastMessage);
                    Minecraft.getMinecraft().thePlayer.sendChatMessage("/" + lastMessage);
                } else {
                    //reenable the prune checker if it is enabled
                    skipPruneCheck = false;

                    if (newLobby) {
                        System.out.println("moving to new lobby");
                        if(guildCheck) {
                            guildCheck = false;
                            Minecraft.getMinecraft().thePlayer.sendChatMessage("/guild list");
                        } else {
                            //go to new lobby
                            newLobby = false;
                            guildCheck = true;
                            Minecraft.getMinecraft().thePlayer.sendChatMessage("/lobby");
                        }
                    } else {
                        //get all players and queue invite commands
                        newLobby = true;
                        try {
                            java.util.List<EntityPlayer> players = Minecraft.getMinecraft().theWorld.playerEntities;
                            for (int i = 0; i < players.size(); i++) {
                                if(players.get(i).getName().length() != 10) {
                                    messages.add("guild invite " + players.get(i).getName());
                                }
                            }
                        } catch (Exception e) {
                            System.out.println("Error: script running when not connected to server");
                        }

                    }
                }
            }
        }
    }
}
