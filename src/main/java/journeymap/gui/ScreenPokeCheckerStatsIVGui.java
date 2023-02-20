package journeymap.gui;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PokemonStorage;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.client.gui.pokechecker.GuiPokeCheckerTabs;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.IVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.awt.Color;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.translation.I18n;
import org.lwjgl.opengl.GL11;

public class ScreenPokeCheckerStatsIVGui extends ScreenPokeCheckerStatsGui {
    private int hexWhite = Color.WHITE.getRGB();
  
    Pokemon p1 = this.storage.get(this.position);
  
  public ScreenPokeCheckerStatsIVGui(PokemonStorage selected, StoragePosition b, GuiScreen box) {
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
        IVStore IVs = Pixelmon.storageManager.getParty(this.pokemon.getOwnerPlayerUUID()).get(this.position).getIVs();
        int HPIV = IVs.get(StatsType.HP);
        int attackIV = IVs.get(StatsType.Attack);
        int defenseIV = IVs.get(StatsType.Defence);
        int spAttIV = IVs.get(StatsType.SpecialAttack);
        int spDefIV = IVs.get(StatsType.SpecialDefence);
        int speedIV = IVs.get(StatsType.Speed);
        int hexColor = this.hexWhite;
        GL11.glNormal3f(0.0F, -1.0F, 0.0F);

        this.drawString(this.mc.fontRenderer, I18n.translateToLocal("gui.screenpokechecker.lvl") + " " + this.pokemon.getLevel(), 10, -14, hexColor);
        this.drawString(this.mc.fontRenderer, I18n.translateToLocal("gui.screenpokechecker.number") + " " + this.pokemon.getSpecies().getNationalPokedexInteger(), -30, -14, hexColor);
        this.drawCenteredString(this.mc.fontRenderer, String.valueOf(this.pokemon.getOriginalTrainer()), 8, 126, hexColor);


        this.drawString(this.mc.fontRenderer, I18n.translateToLocal("gui.screenpokechecker.ot"), -32, 112, hexColor);
        this.drawString(this.mc.fontRenderer, I18n.translateToLocal("nbt.hp.name"), 60, -12, hexColor);
        String strHP = String.valueOf(HPIV);


        this.drawString(this.mc.fontRenderer, strHP, 200 - strHP.length() * 3, -12, hexColor);

        String strATK = String.valueOf(attackIV);

        this.drawString(this.mc.fontRenderer, I18n.translateToLocal("nbt.attack.name"), 60, 9, hexColor);
        this.drawString(this.mc.fontRenderer, strATK, 200 - strATK.length() * 3, 9, hexColor);

        String strDEF = String.valueOf(defenseIV);

        this.drawString(this.mc.fontRenderer, I18n.translateToLocal("nbt.defense.name"), 60, 28, hexColor);
        this.drawString(this.mc.fontRenderer, strDEF, 200 - strDEF.length() * 3, 28, hexColor);

        String strSATK = String.valueOf(spAttIV);

        this.drawString(this.mc.fontRenderer, I18n.translateToLocal("nbt.spattack.name"), 60, 48, hexColor);
        this.drawString(this.mc.fontRenderer, strSATK, 200 - strSATK.length() * 3, 48, hexColor);

        String strSDEF = String.valueOf(spDefIV);

        this.drawString(this.mc.fontRenderer, I18n.translateToLocal("nbt.spdefense.name"), 60, 69, hexColor);
        this.drawString(this.mc.fontRenderer, strSDEF, 200 - strSDEF.length() * 3, 69, hexColor);

        String strSPD = String.valueOf(speedIV);

        this.drawString(this.mc.fontRenderer, I18n.translateToLocal("nbt.speed.name"), 60, 88, hexColor);
        this.drawString(this.mc.fontRenderer, strSPD, 200 - strSPD.length() * 3, 88, hexColor);
        hexColor = this.hexWhite;
        this.drawCenteredString(this.mc.fontRenderer, "Hidden Power", 95, 115, hexColor);
        this.drawCenteredString(this.mc.fontRenderer, "Total IV", 174, 115, hexColor);
        this.drawCenteredString(this.mc.fontRenderer, I18n.translateToLocal("gui.screenpokechecker.growth"), 8, 137, hexColor);

        int ivSum = HPIV + attackIV + defenseIV + spAttIV + spDefIV + speedIV;

        EnumType hiddenPower = EnumType.Normal;
      int hp = HPIV % 2;
      int atk = attackIV % 2;
      int def = defenseIV % 2;
      int sp = speedIV % 2;
      int spa = spAttIV % 2;
      int spd = spDefIV % 2;
      double top = (32 * spd + 16 * spa + 8 * sp + 4 * def + 2 * atk + hp);
      int typeHidden = (int)Math.floor(top * 15.0D / 63.0D);
      switch (typeHidden) {
        case 0:
          hiddenPower = EnumType.Fighting;
          break;
        case 1:
          hiddenPower = EnumType.Flying;
          break;
        case 2:
          hiddenPower = EnumType.Poison;
          break;
        case 3:
          hiddenPower = EnumType.Ground;
          break;
        case 4:
          hiddenPower = EnumType.Rock;
          break;
        case 5:
          hiddenPower = EnumType.Bug;
          break;
        case 6:
          hiddenPower = EnumType.Ghost;
          break;
        case 7:
          hiddenPower = EnumType.Steel;
          break;
        case 8:
          hiddenPower = EnumType.Fire;
          break;
        case 9:
          hiddenPower = EnumType.Water;
          break;
        case 10:
          hiddenPower = EnumType.Grass;
          break;
        case 11:
          hiddenPower = EnumType.Electric;
          break;
        case 12:
          hiddenPower = EnumType.Psychic;
          break;
        case 13:
          hiddenPower = EnumType.Ice;
          break;
        case 14:
          hiddenPower = EnumType.Dragon;
          break;
        case 15:
          hiddenPower = EnumType.Dark;
          break;
      } 

        this.drawCenteredString(this.mc.fontRenderer, hiddenPower.getLocalizedName(), 95, 130, hexColor);
        this.drawCenteredString(this.mc.fontRenderer, ivSum + "/186", 174, 130, -1);
        this.drawCenteredString(this.mc.fontRenderer, "" + ChatFormatting.YELLOW + ChatFormatting.BOLD + String.valueOf((int)(((double)ivSum/186)*100)) + "%",174, 144, -1);
        this.drawCenteredString(this.mc.fontRenderer, this.pokemon.getGrowth().getLocalizedName(), 8, 150, -1);


        this.drawString(this.mc.fontRenderer, I18n.translateToLocal("gui.screenpokechecker.stats"), 145, 166, hexColor);

        boolean isEgg = this.pokemon.isEgg();
        isEgg = false;
        drawBasePokemonInfo();
    }
}
