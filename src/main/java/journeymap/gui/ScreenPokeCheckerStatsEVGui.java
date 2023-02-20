package journeymap.gui;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.storage.PokemonStorage;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.client.gui.pokechecker.GuiPokeCheckerTabs;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.EVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import java.awt.Color;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.translation.I18n;
import org.lwjgl.opengl.GL11;

public class ScreenPokeCheckerStatsEVGui extends ScreenPokeCheckerStatsGui {
    private int hexWhite = Color.WHITE.getRGB();
    
    public ScreenPokeCheckerStatsEVGui(PokemonStorage selected, StoragePosition b, GuiScreen box) {
      super(selected, b, box);
    }
    @Override
    public void initGui()
    {
        super.initGui();
        this.buttonList.add(new GuiPokeCheckerTabs(2, 200, this.width / 2 + 36, this.height / 2 + 80, 69, 15, I18n.translateToLocal("gui.screenpokechecker.stats")));
    }

    @Override
    public void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 200)
            this.mc.displayGuiScreen((GuiScreen)new ScreenPokeCheckerStatsGui(this.pokemon.getStorage(), this.isPC, this.box));
        else
            super.actionPerformed(button);
    }

    @Override
    public void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        EVStore EVs = Pixelmon.storageManager.getParty(this.pokemon.getOwnerPlayerUUID()).get(this.position).getEVs();
        int HPEV = EVs.get(StatsType.HP);
        int attackEV = EVs.get(StatsType.Attack);
        int defenseEV = EVs.get(StatsType.Defence);
        int spAttEV = EVs.get(StatsType.SpecialAttack);
        int spDefEV = EVs.get(StatsType.SpecialDefence);
        int speedEV = EVs.get(StatsType.Speed);
        int hexColor = this.hexWhite;
        GL11.glNormal3f(0.0F, -1.0F, 0.0F);

        this.drawString(this.mc.fontRenderer, I18n.translateToLocal("gui.screenpokechecker.lvl") + " " + this.pokemon.getLevel(), 10, -14, hexColor);
        this.drawString(this.mc.fontRenderer, I18n.translateToLocal("gui.screenpokechecker.number") + " " + this.pokemon.getSpecies().getNationalPokedexInteger(), -30, -14, hexColor);
        this.drawCenteredString(this.mc.fontRenderer, String.valueOf(this.pokemon.getOriginalTrainer()), 8, 126, hexColor);


        this.drawString(this.mc.fontRenderer, I18n.translateToLocal("gui.screenpokechecker.ot"), -32, 112, hexColor);
        this.drawString(this.mc.fontRenderer, I18n.translateToLocal("nbt.hp.name"), 60, -12, hexColor);
        String strHP = String.valueOf(HPEV);


        this.drawString(this.mc.fontRenderer, strHP, 200 - strHP.length() * 3, -12, hexColor);

        String strATK = String.valueOf(attackEV);

        this.drawString(this.mc.fontRenderer, I18n.translateToLocal("nbt.attack.name"), 60, 9, hexColor);
        this.drawString(this.mc.fontRenderer, strATK, 200 - strATK.length() * 3, 9, hexColor);

        String strDEF = String.valueOf(defenseEV);

        this.drawString(this.mc.fontRenderer, I18n.translateToLocal("nbt.defense.name"), 60, 28, hexColor);
        this.drawString(this.mc.fontRenderer, strDEF, 200 - strDEF.length() * 3, 28, hexColor);

        String strSATK = String.valueOf(spAttEV);

        this.drawString(this.mc.fontRenderer, I18n.translateToLocal("nbt.spattack.name"), 60, 48, hexColor);
        this.drawString(this.mc.fontRenderer, strSATK, 200 - strSATK.length() * 3, 48, hexColor);

        String strSDEF = String.valueOf(spDefEV);

        this.drawString(this.mc.fontRenderer, I18n.translateToLocal("nbt.spdefense.name"), 60, 69, hexColor);
        this.drawString(this.mc.fontRenderer, strSDEF, 200 - strSDEF.length() * 3, 69, hexColor);

        String strSPD = String.valueOf(speedEV);

        this.drawString(this.mc.fontRenderer, I18n.translateToLocal("nbt.speed.name"), 60, 88, hexColor);
        this.drawString(this.mc.fontRenderer, strSPD, 200 - strSPD.length() * 3, 88, hexColor);
        hexColor = this.hexWhite;
        this.drawCenteredString(this.mc.fontRenderer, "Gender", 95, 115, hexColor);
        this.drawCenteredString(this.mc.fontRenderer, "Total EV", 174, 115, hexColor);
        this.drawCenteredString(this.mc.fontRenderer, I18n.translateToLocal("gui.screenpokechecker.growth"), 8, 137, hexColor);

        int evSum = 0;
        for (int i : this.pokemon.getEVs().getArray())
            evSum += i;

        this.drawCenteredString(this.mc.fontRenderer, this.pokemon.getGender().toString(), 95, 130, hexColor);
        this.drawCenteredString(this.mc.fontRenderer, evSum + "/510", 174, 130, -1);
        this.drawCenteredString(this.mc.fontRenderer, "" + ChatFormatting.YELLOW + ChatFormatting.BOLD + String.valueOf((int)(((double)evSum/510)*100)) + "%",174, 144, -1);
        this.drawCenteredString(this.mc.fontRenderer, this.pokemon.getGrowth().getLocalizedName(), 8, 150, -1);


        this.drawString(this.mc.fontRenderer, I18n.translateToLocal("gui.screenpokechecker.stats"), 145, 166, hexColor);

        boolean isEgg = this.pokemon.isEgg();
        isEgg = false;
        drawBasePokemonInfo();
    }
}
