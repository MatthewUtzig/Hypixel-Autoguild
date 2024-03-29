package com.matthewutzig.HypixelAutoGuild;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import scala.Char;
import scala.actors.migration.pattern;

import java.util.*;
import java.util.regex.Pattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutoGuildTickHandler {
    protected static final int PRUNE_LIMIT = 123; //the maximum number of players in the guild before pruning starts.
    protected static final int PRUNE_COUNT = 5; //the number of members to prune.
    protected static final boolean CHAT_DEBUG = false; //output all unformatted chat
    protected static final boolean ENABLE_MOTD = true; //Whether to enable guild messages

    protected static ArrayList<String> messages = new ArrayList<String>();
    protected static int tickRemaining = 0;
    protected static int kickCount = 0;
    protected static int spamCheckTicks = 0;

    protected static boolean newLobby = true; //stores whether to warp to new lobby
    protected static boolean guildMembers = false; //stores whether the next chat messages will be the list of guild members
    protected static boolean abovePruneLimit = false; //stores whether to begin the pruning process
    protected static boolean skipPruneCheck = false;
    protected static boolean guildCheck = true; //used for determining when to switch lobbies
    protected static boolean waitingForLobby = false;
    protected static boolean lookForSupporters = false;

    protected static HashMap<String, Integer> messageCounter = new HashMap<String, Integer>();

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

                //Count chat messages for spam
                if(message.contains("Guild >")) {
                    //convert message to playername
                    if(message.contains("]")) { //ranks
                        message = message.substring(message.indexOf("]") + 2);

                    } else { //non-ranks
                        message=message.substring(message.indexOf("Guild >") + 10);
                    }
                    if(message.contains("[")) {//guild rank title
                        message = message.substring(0 , message.indexOf("[") - 3);
                    } else { //no guild rank
                        message = message.substring(0 , message.indexOf("\u00a7f"));
                    }
                    //increment or initialize counter
                    if(messageCounter.containsKey(message)) {
                        messageCounter.put(message, messageCounter.get(message) + 1);
                        System.out.println("Spam prevention: " + message + " has sent " + messageCounter.get(message) + " messages in the last minute");
                        //mute players with over 10 messages
                        if(messageCounter.get(message) >= 10) {
                            messages.add("gchat muted " + message + " for spam. Appeal at https://discord.gg/hUAfPmS");
                            messages.add("guild mute " + message + " 1h");
                            messageCounter.remove(message);
                        }

                    } else {
                        messageCounter.put(message, 1);
                        System.out.println("Spam prevention: " + message + " has sent 1 message in the last minute");
                    }
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
                    if(BannedPlayers.isBanned(message)) {
                        messages.add("guild kick " + message + " you are banned from the guild. Appeal at https://discord.gg/hUAfPmS");
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
                                    messages.add("guild kick " + playerName + " you were kicked for logging out. Rejoin with /guild join SulphurAdam");
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
                //check if one minute has elapsed (for spam check)
                spamCheckTicks--;
                if (spamCheckTicks <= 0) {
                    spamCheckTicks = 1200;
                    messageCounter.clear();
                }

                //check if a half second have elapsed. (Send one message every half second)
                tickRemaining--;
                if (tickRemaining <= 0) {
                    //set counter back to 6 ticks. This is the shortest time where no "you are sending commands to fast" is received.
                    tickRemaining = 6;
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
                                abovePruneLimit = false;
                                lookForSupporters = false;
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

    /*
        Queue all online players to be invited.
        Also has a random chance of queuing an info message.
     */
    protected static void appendPlayersToList() {
        try {
            java.util.List<EntityPlayer> players = Minecraft.getMinecraft().theWorld.playerEntities;
            for (int i = 0; i < players.size(); i++) {
                //add players to invite list if they are not a Hypixel bot
                if(!isHypixelBot(players.get(i).getName())) {
                    messages.add("guild invite " + players.get(i).getName());
                }
                //.1% change of adding motd
                Random rand = new Random();
                if(rand.nextInt(250) == 10) {
                    //MESSAGES DISABLED FOR PUBLIC RELEASE. REMOVE THESE COMMENTS TO ENABLE.
                    //Change messages in the MessageOfTheDay class.
                    //if(ENABLE_MOTD) {
                    // messages.add("gchat " + MessageOfTheDay.getMOTD());
                    //}
                }
            }
        } catch (Exception e) {
            System.out.println("Error: script running when not connected to server");
        }
    }

    /*
        Returns whether the specified player is a Hypixel bot. These are either watchdog bots or lobby npcs.

        >99% chance of correctly identifying a player
        98.4% chance of correctly identifying a bot.
     */
    protected static boolean isHypixelBot(String playerName) {
        //bots always have 10 character names
        if(playerName.length() != 10) {
            return false;
        }
        //most players have numbers at end of names
        if(getNumbers(playerName.substring(0,6)) == 0) {
            return false;
        }
        //names will 1 or less letters OR 1 or less numbers are most likely players
        int numbers = getNumbers(playerName);
        if(numbers >= 9 || numbers <= 1) {
            return false;
        }
        return true;
    }

    /*
        Returns the number of numbers in a string.
    */
    protected static int getNumbers(String string) {
        int numbers = 0;
        for(int i = 0; i < string.length(); i++) {
            if (Character.isDigit(string.charAt(i))) {
                numbers++;
            }
        }
        return numbers;
    }
}
