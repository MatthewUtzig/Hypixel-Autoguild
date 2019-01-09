package com.matthewutzig.HypixelAutoGuild;

import com.matthewutzig.HypixelAutoGuild.proxy.CommonProxy;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = HypixelAutoGuild.MODID, version = HypixelAutoGuild.VERSION)
public class HypixelAutoGuild
{
    public static final String MODID = "hypixelautoguild";
    public static final String VERSION = "1.0";
    public static final String NAME = "Hypixel Auto Guild";


    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        //register event handler
        MinecraftForge.EVENT_BUS.register(new AutoGuildTickHandler());
        //register command handler
        ClientCommandHandler.instance.registerCommand(new StartCommand());

        proxy.postInit(event);
    }

    @SidedProxy(clientSide = "com.matthewutzig.HypixelAutoGuild.proxy.ClientProxy", serverSide = "com.matthewutzig.HypixelAutoGuild.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static HypixelAutoGuild instance;
}
