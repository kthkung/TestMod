//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.gui;

import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;

import java.util.ArrayList;
import java.util.List;

public class ButtonRotator {
   private int buttonId;
   private List<Enum<?>> items = new ArrayList();
   private Enum<?> defaultItem = (Enum)null;
   private String defaultLabel = "Off";
   private int index = 0;
   private List<GuiButton> buttonList;
   private FontRenderer fontRenderer;
   private int x = 0;
   private int y = 0;
   private int height = 20;
   private GuiButton button;

   public ButtonRotator(int buttonId, FontRenderer fontRenderer, int x, int y, List<GuiButton> buttonList, Enum<?>[] items, Enum<?> selected) {
      this.buttonId = buttonId;
      this.fontRenderer = fontRenderer;
      this.x = x;
      this.y = y;
      this.buttonList = buttonList;
      this.addItem(this.defaultItem);

      for(int i = 0; i < items.length; ++i) {
         this.addItem(items[i]);
      }

      this.setSelected(selected);
      String label = this.getSelectedLabel();
      this.button = new GuiButton(buttonId, x, y, fontRenderer.getStringWidth(label) + 15, this.height, label);
      this.buttonList.add(this.button);
   }

   public Enum<?> cycle() {
      this.buttonList.remove(this.button);
      if (this.index < this.items.size() - 1) {
         ++this.index;
         Enum<?> item = (Enum)this.items.get(this.index);
         String label = "";
         if (item instanceof EnumNature) {
            String properties = "";
            properties = properties + " +" + this.getNatureShorthand(((EnumNature)item).increasedStat);
            properties = properties + " -" + this.getNatureShorthand(((EnumNature)item).decreasedStat);
            label = ((Enum)this.items.get(this.index)).toString() + properties;
         } else {
            label = ((Enum)this.items.get(this.index)).toString();
         }

         this.button.setWidth(this.fontRenderer.getStringWidth(label) + 15);
         this.button.displayString = label;
         this.buttonList.add(this.button);
      } else {
         String label = this.defaultLabel;
         this.button.setWidth(this.fontRenderer.getStringWidth(defaultLabel) + 15);
         this.button.displayString = defaultLabel;
         this.buttonList.add(this.button);
         this.index = 0;
      }

      return this.getSelected();
   }

   private String getNatureShorthand(StatsType type) {
      switch (type) {
         case Accuracy:
            return "Acc";
         case HP:
            return "HP";
         case Speed:
            return "Speed";
         case Attack:
            return "Atk";
         case Defence:
            return "Def";
         case Evasion:
            return "Eva";
         case SpecialAttack:
            return "SpAtk";
         case SpecialDefence:
            return "SpDef";
         case None:
            return "None";
         default:
            return "";
      }
   }

   public GuiButton getButton() {
      return this.button;
   }

   public int getButtonId() {
      return this.buttonId;
   }

   public Enum<?> getSelected() {
      return (Enum)this.items.get(this.index);
   }

   public String getSelectedLabel() {
      Enum<?> item = (Enum)this.items.get(this.index);
      return item == null ? this.defaultLabel : item.toString();
   }

   public void setSelected(Enum<?> item) {
      this.index = this.items.indexOf(item);
   }

   public void addItem(Enum<?> item) {
      this.items.add(item);
   }

   public void removeItem(Enum<?> item) {
      this.items.remove(item);
   }

   public String getDefaultLabel() {
      return this.defaultLabel;
   }
}
