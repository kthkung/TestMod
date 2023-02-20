//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.gui;

import java.util.Arrays;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.ArrayUtils;

@SideOnly(Side.CLIENT)
public class GuiNatureList extends GuiListExtended {
   private final RadarGui controlsScreen;
   private final Minecraft mc;
   private final GuiListExtended.IGuiListEntry[] listEntries;
   private int maxListLabelWidth;

   public GuiNatureList(RadarGui controls, Minecraft mcIn) {
      super(mcIn, controls.width + 45, controls.height, 63, controls.height - 32, 20);
      this.controlsScreen = controls;
      this.mc = mcIn;
      KeyBinding[] akeybinding = (KeyBinding[])((KeyBinding[])ArrayUtils.clone(mcIn.gameSettings.keyBindings));
      this.listEntries = new GuiListExtended.IGuiListEntry[akeybinding.length + KeyBinding.getKeybinds().size()];
      Arrays.sort((Object[])akeybinding);
      int i = 0;
      String s = null;
      KeyBinding[] var6 = akeybinding;
      int var7 = akeybinding.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         KeyBinding keybinding = var6[var8];
         String s1 = keybinding.getKeyCategory();
         if (!s1.equals(s)) {
            s = s1;
            this.listEntries[i++] = new CategoryEntry(s1);
         }

         int j = mcIn.fontRenderer.getStringWidth(I18n.format(keybinding.getKeyDescription(), new Object[0]));
         if (j > this.maxListLabelWidth) {
            this.maxListLabelWidth = j;
         }

         this.listEntries[i++] = new KeyEntry(keybinding);
      }

   }

   protected int getSize() {
      return this.listEntries.length;
   }

   public GuiListExtended.IGuiListEntry getListEntry(int index) {
      return this.listEntries[index];
   }

   protected int getScrollBarX() {
      return super.getScrollBarX() + 35;
   }

   public int getListWidth() {
      return super.getListWidth() + 32;
   }

   @SideOnly(Side.CLIENT)
   public class KeyEntry implements GuiListExtended.IGuiListEntry {
      private final KeyBinding keybinding;
      private final String keyDesc;
      private final GuiButton btnChangeKeyBinding;
      private final GuiButton btnReset;

      private KeyEntry(KeyBinding name) {
         this.keybinding = name;
         this.keyDesc = I18n.format(name.getKeyDescription(), new Object[0]);
         this.btnChangeKeyBinding = new GuiButton(0, 0, 0, 95, 20, I18n.format(name.getKeyDescription(), new Object[0]));
         this.btnReset = new GuiButton(0, 0, 0, 50, 20, I18n.format("controls.reset", new Object[0]));
      }

      public void updatePosition(int slotIndex, int x, int y, float partialTicks) {
      }

      public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
         GuiNatureList.this.mc.fontRenderer.drawString(this.keyDesc, x + 90 - GuiNatureList.this.maxListLabelWidth, y + slotHeight / 2 - GuiNatureList.this.mc.fontRenderer.FONT_HEIGHT / 2, 16777215);
         this.btnReset.x = x + 210;
         this.btnReset.y = y;
         this.btnReset.enabled = !this.keybinding.isSetToDefaultValue();
         this.btnReset.drawButton(GuiNatureList.this.mc, mouseX, mouseY, partialTicks);
         this.btnChangeKeyBinding.x = x + 105;
         this.btnChangeKeyBinding.y = y;
         this.btnChangeKeyBinding.displayString = this.keybinding.getDisplayName();
         boolean keyCodeModifierConflict = true;
         if (this.keybinding.getKeyCode() != 0) {
            KeyBinding[] var11 = GuiNatureList.this.mc.gameSettings.keyBindings;
            int var12 = var11.length;

            for(int var13 = 0; var13 < var12; ++var13) {
               KeyBinding keybinding = var11[var13];
               if (keybinding != this.keybinding && keybinding.conflicts(this.keybinding)) {
                  keyCodeModifierConflict &= keybinding.hasKeyCodeModifierConflict(this.keybinding);
               }
            }
         }

         this.btnChangeKeyBinding.drawButton(GuiNatureList.this.mc, mouseX, mouseY, partialTicks);
      }

      public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
         if (this.btnChangeKeyBinding.mousePressed(GuiNatureList.this.mc, mouseX, mouseY)) {
            return true;
         } else if (this.btnReset.mousePressed(GuiNatureList.this.mc, mouseX, mouseY)) {
            this.keybinding.setToDefault();
            GuiNatureList.this.mc.gameSettings.setOptionKeyBinding(this.keybinding, this.keybinding.getKeyCodeDefault());
            KeyBinding.resetKeyBindingArrayAndHash();
            return true;
         } else {
            return false;
         }
      }

      public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
         this.btnChangeKeyBinding.mouseReleased(x, y);
         this.btnReset.mouseReleased(x, y);
      }

      public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {
      }
   }

   @SideOnly(Side.CLIENT)
   public class CategoryEntry implements GuiListExtended.IGuiListEntry {
      private final String labelText;
      private final int labelWidth;

      CategoryEntry(String name) {
         this.labelText = I18n.format(name, new Object[0]);
         this.labelWidth = GuiNatureList.this.mc.fontRenderer.getStringWidth(this.labelText);
      }

      public void updatePosition(int slotIndex, int x, int y, float partialTicks) {
      }

      public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
         GuiNatureList.this.mc.fontRenderer.drawString(this.labelText, ((GuiScreen)Objects.requireNonNull(GuiNatureList.this.mc.currentScreen)).width / 2 - this.labelWidth / 2, y + slotHeight - GuiNatureList.this.mc.fontRenderer.FONT_HEIGHT - 1, 16777215);
      }

      public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
         return false;
      }

      public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
      }

      public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {
      }
   }
}
