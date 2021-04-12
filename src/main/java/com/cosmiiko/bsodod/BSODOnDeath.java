package com.cosmiiko.bsodod;

import com.sun.jna.Memory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@Mod("bsodod")
public class BSODOnDeath
{
    private static final Logger LOGGER = LogManager.getLogger();
    private boolean hasDied = false;
    private boolean timerStarted = false;

    public BSODOnDeath() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_SPEC);
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
        {
            hasDied = false;

            // Start the timer of the blue screen
            timerStarted = true;
        }


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

                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + Config.openUrl);
            }
            catch (IOException e)
            {
                LOGGER.error("Error during Rick Roll attempt: ", e);
            }
        }
    }

    private int tickCounter = 0;
    private boolean firstTickPassed = false;

    @SubscribeEvent
    public void onClientTickEvent(TickEvent.ClientTickEvent event)
    {
        if (!firstTickPassed)
        {
            Minecraft.getInstance().displayGuiScreen(new WarningScreen());
            firstTickPassed = true;
        }

        if (timerStarted)
        {
            if (tickCounter % 40 == 0)
            {
                LOGGER.info("BSOD in " + (Config.delayInSecs - tickCounter/40) + " secs");
            }

            if (tickCounter == Config.delayInSecs*40)
            {
                // Enable the shutdown privilege
                NtDll.INSTANCE.RtlAdjustPrivilege(19, true, false, new Memory(1));

                if (Config.defused)
                {
                    LOGGER.info("You would have gotten a blue screen here but the mod is defused.");
                }
                else
                {
                    // Raise an error and shutdown because of it
                    NtDll.INSTANCE.NtRaiseHardError(0xDEADDEAD, 0, 0, 0, 6, new Memory(32));
                }

                timerStarted = false;
                tickCounter = 0;
                return;
            }

            tickCounter++;
        }
    }
}
