package com.cosmiiko.bsodod;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.OptionsRowList;
import net.minecraft.util.text.*;

import java.util.*;

public class WarningScreen extends Screen {
    public WarningScreen() {
        super(new StringTextComponent("Blue Screen of Death, On Death").setStyle(Style.EMPTY.setUnderlined(true)));
    }

    private int WARNINGSIZE = 20;
    private int LINESIZE = 16;

    private ArrayList<String> textLines;
    private OptionsRowList optionsRowList;
    private int textVOffset = 0;

    @Override
    public void init()
    {
        textLines = new ArrayList<>();
        textLines.add("This mod causes your computer to blue screen when you die.");
        textLines.add("Therefore, please save any and all unsaved work on your computer.");
        textLines.add("If you are about to play singleplayer, please backup your world first.");
        textLines.add("By clicking 'I understand', you acknowledge the risks involved in");
        textLines.add("playing with such a mod.");

        String latestVersion = UpdateChecker.GetLatestVersion();
        String currentVersion = UpdateChecker.GetCurrentVersion();

        if (latestVersion != null && currentVersion != null)
        {
            if (!latestVersion.equals(currentVersion))
            {
                textLines.add(TextFormatting.RED + "Your mod version is outdated (v" + currentVersion + ", latest is v" + latestVersion + ").");
                textLines.add(TextFormatting.RED + "This version may contain bugs or accidental blue screens.");
            }
            else
            {
                textLines.add(TextFormatting.GREEN + "Your mod version is up-to-date.");
            }
        }
        else
        {
            textLines.add(TextFormatting.GOLD + "Could not check for updates. Proceed with caution.");
        }

        textVOffset = this.height/2 - (textLines.size()*LINESIZE + WARNINGSIZE)/2;

        optionsRowList = new OptionsRowList(Minecraft.getInstance(), this.width, this.height, 30, this.height - 30, 1);
        this.children.add(optionsRowList);

        this.addButton(
                new Button(
                        (this.width - 150)/2, this.height - 25,
                        150, 20,
                        new StringTextComponent("I understand"),
                        button -> this.closeScreen()
                )
        );
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        // Render dirt background
        this.renderBackground(matrixStack);

        // Render black-ish overlay
        this.optionsRowList.render(matrixStack, mouseX, mouseY, partialTicks);

        // Render title & warning text
        this.font.drawTextWithShadow(matrixStack, this.title, centeredX(this.title.getString()), 11, 0xFFFFFF);

        String warningText = "WARNING, PLEASE READ ATTENTIVELY";
        this.font.drawStringWithShadow(matrixStack, warningText, centeredX(warningText), textVOffset, 0xFF0000);

        // Render all text lines
        for (int i = 0; i < textLines.size(); i++)
        {
            String text = textLines.get(i);
            this.font.drawStringWithShadow(matrixStack, text, centeredX(text), textVOffset + WARNINGSIZE + i*LINESIZE, 0xFFFFFF);
        }

        // Finalize the render
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    private int centeredX(String text)
    {
        int screenWidth = Minecraft.getInstance().getMainWindow().getScaledWidth();
        return screenWidth/2 - Minecraft.getInstance().fontRenderer.getStringWidth(text)/2;
    }
}
