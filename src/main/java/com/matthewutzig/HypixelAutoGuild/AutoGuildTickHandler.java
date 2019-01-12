package com.matthewutzig.HypixelAutoGuild;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Random;

public class AutoGuildTickHandler {
    protected static final int PRUNE_LIMIT = 123; //the maximum number of players in the guild before pruning starts.
    protected static final int PRUNE_COUNT = 5; //the number of members to prune.
    protected static final boolean CHAT_DEBUG = false; //output all unformatted chat
    //protected static final boolean ENABLE_MOTD = false;

    protected static ArrayList<String> messages = new ArrayList<String>();
    protected static int tickRemaining = 0;
    protected static int kickCount = 0;

    protected static boolean newLobby = true; //stores whether to warp to new lobby
    protected static boolean guildMembers = false; //stores whether the next chat messages will be the list of guild members
    protected static boolean abovePruneLimit = false; //stores whether to begin the pruning process
    protected static boolean skipPruneCheck = false;
    protected static boolean guildCheck = true; //used for determining when to switch lobbies
    protected static boolean waitingForLobby = false;
    protected static boolean lookForSupporters = false;

    public static boolean scriptEnabled = false;

    @SubscribeEvent
    public void tickEvent(TickEvent.ClientTickEvent event) {
        this.runCommand();
    }

    //handle guild messages
    @SubscribeEvent
    public void messageEvent(ClientChatReceivedEvent event) {
        try {
            //make sure script is enabled
            if (scriptEnabled) {
                String message = event.message.getFormattedText();
                if (CHAT_DEBUG) {
                    System.out.println(message);
                }

                //promote supporters
                if (message.contains("\u00a7r\u00a7ejoined the guild") || message.contains("\u00a7r\u00a7e joined the guild")) {
                    //get player name
                    if (message.contains("]")) { //ranks
                        message = message.substring(message.indexOf("]") + 2);
                        message = message.substring(0,message.indexOf("\u00a7"));
                    } else { //non ranks
                        message = message.substring(2, message.indexOf(" "));
                        if (message.contains("\u00a7")) {
                            message = message.substring(0, message.indexOf("\u00a7"));
                        }
                    }
                    if(LostshardSupporters.isSupporter(message)) {
                        System.out.println("Promoted " + message + " to supporter.");
                        messages.add("guild demote " + message);
                    } else {
                        System.out.println("Did not promote " + message + " to supporter.");
                    }
                }

                //auto guild accept
                if (message.contains("\u00a7r\u00a7b/guild accept")) {
                    String name = message.substring(message.indexOf("\u00a7r\u00a7b/guild accept") + 18);
                    messages.add("guild accept " + name.substring(0, name.indexOf("!") - 3));
                }

                if (!skipPruneCheck) {
                    if (abovePruneLimit) {
                        //check if chat message contains guild members
                        if (guildMembers) {
                            guildMembers = false;
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
                                    splitMessage[i] = splitMessage[i].substring(splitMessage[i].indexOf("]") + 1);
                                }
                            }
                            if(!lookForSupporters) {
                                kickCount = 0; //make sure not to kick too many
                            }
                            //only color code left is red. Get if it is light red (offline)
                            for (int i = 0; i < splitMessage.length; i++) {
                                if (splitMessage[i].contains("\u00a7c")) {
                                    //add kick player to command queue
                                    String playerName = splitMessage[i].substring(0, splitMessage[i].indexOf("\u00a7c"));
                                    System.out.println("Added " + playerName + " to the kick queue.");
                                    messages.add("guild kick " + playerName + " you were kicked for inactivity. /guild join MatthewUtzig");
                                    kickCount++;
                                    if (kickCount >= PRUNE_COUNT) {
                                        kickCount = 0;
                                        abovePruneLimit = false;
                                        skipPruneCheck = true;
                                        return;
                                    }
                                }
                            }
                            if(!lookForSupporters) {
                                lookForSupporters = true;
                            } else {
                                //all members checked
                                kickCount = 0;
                                abovePruneLimit = false;
                                skipPruneCheck = true;
                                return;
                            }
                        } else if (!lookForSupporters && message.contains("-- Member --")) {
                            //next message will contain guild members
                            guildMembers = true;
                        } else if (lookForSupporters && message.contains("-- Supporter --")) {
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
        } catch (Exception e) {
            System.out.println("Error: script running while not connected to server. " + e);
        }
    }

    public static void runCommand() {
        try {
            //make sure script is enabled
            if(scriptEnabled) {
                //check if three seconds have elapsed
                tickRemaining--;
                if (tickRemaining <= 0) {
                    //set counter back to 30 ticks
                    tickRemaining = 30;
                    //check if read to send /lobby
                    if(waitingForLobby) {
                        waitingForLobby = false;
                        //add all players in game to invite list for later
                        appendPlayersToList();
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/lobby");
                        return;
                    }

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
                                String command = HypixelPlayCommands.getPlayCommand();
                                if(command.equals("lobby")) {
                                    Minecraft.getMinecraft().thePlayer.sendChatMessage("/lobby");
                                } else {
                                    //hault command execution for ~1.5 seconds
                                    tickRemaining = 90;
                                    Minecraft.getMinecraft().thePlayer.sendChatMessage("/" + command);
                                    waitingForLobby = true;
                                }

                            }
                        } else {
                            //get all players and queue invite commands
                            newLobby = true;
                            appendPlayersToList();
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error: script running while not connected to server. " + e);
        }
    }

    protected static void appendPlayersToList() {
        try {
            java.util.List<EntityPlayer> players = Minecraft.getMinecraft().theWorld.playerEntities;
            for (int i = 0; i < players.size(); i++) {
                if(players.get(i).getName().length() != 10) {
                    messages.add("guild invite " + players.get(i).getName());
                }
                //5% change of adding motd
                Random rand = new Random();
                if(rand.nextInt(150) == 10) {
                    //if(ENABLE_MOTD) {
                        messages.add("gchat " + MessageOfTheDay.getMOTD());
                    //}
                }
            }
        } catch (Exception e) {
            System.out.println("Error: script running when not connected to server");
        }
    }
}
