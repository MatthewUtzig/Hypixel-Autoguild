package com.matthewutzig.HypixelAutoGuild;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;

public class AutoGuildTickHandler {
    protected static ArrayList<String> messages = new ArrayList<String>();
    protected static int tickRemaining = 0;
    protected static boolean newLobby = false;

    public static boolean scriptEnabled = false;

    @SubscribeEvent
    public void tickEvent(TickEvent.ClientTickEvent event) {
        this.runCommand();
    }

    public static void runCommand() {
        //make sure script is enabled
        if(scriptEnabled) {
            //check if three seconds have elapsed
            tickRemaining--;
            if (tickRemaining <= 0) {
                //set counter back to 60 ticks
                tickRemaining = 30;
                if (messages.size() > 0) {
                    //remove last message from arrayList
                    String lastMessage = messages.get(messages.size() - 1);
                    messages.remove(lastMessage);
                    //send message
                    System.out.println("Executing command " + "/" + lastMessage);
                    Minecraft.getMinecraft().thePlayer.sendChatMessage("/" + lastMessage);
                } else {
                    if (newLobby) {
                        //go to new lobby
                        newLobby = false;
                        System.out.println("moving to new lobby");
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/lobby");
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
                            System.out.println("Error: script running when not connect to server");
                        }

                    }
                }
            }
        }
    }
}
