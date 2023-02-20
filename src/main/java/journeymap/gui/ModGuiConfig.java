//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.*;
import journeymap.config.ConfigurationHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ModGuiConfig extends GuiConfig {
   public ModGuiConfig(GuiScreen guiScreen) {
      super(guiScreen, getConfigElements(), "pixelradar", "mw.configgui.ctgy.general", false, false, GuiConfig.getAbridgedConfigPath(ConfigurationHandler.configuration.toString()));
   }

   private static List<IConfigElement> getConfigElements() {
      List<IConfigElement> list = new ArrayList();
      list.add(new DummyConfigElement.DummyCategoryElement("mw.configgui.ctgy.general", "mw.configgui.ctgy.general", (new ConfigElement(ConfigurationHandler.configuration.getCategory("mw.configgui.ctgy.general"))).getChildElements()));
      list.add(new DummyConfigElement.DummyCategoryElement("fullscreenmap", "mw.configgui.ctgy.fullScreenMap", (new ConfigElement(ConfigurationHandler.configuration.getCategory("fullscreenmap"))).getChildElements(), MapModeConfigEntry.class));
      list.add(new DummyConfigElement.DummyCategoryElement("largemap", "mw.configgui.ctgy.largeMap", (new ConfigElement(ConfigurationHandler.configuration.getCategory("largemap"))).getChildElements(), MapModeConfigEntry.class));
      list.add(new DummyConfigElement.DummyCategoryElement("smallmap", "mw.configgui.ctgy.smallMap", (new ConfigElement(ConfigurationHandler.configuration.getCategory("smallmap"))).getChildElements(), MapModeConfigEntry.class));
      list.add(new DummyConfigElement.DummyCategoryElement("pixelradar", "mw.configgui.ctgy.pixelradar", (new ConfigElement(ConfigurationHandler.configuration.getCategory("pixelradar"))).getChildElements(), MapModeConfigEntry.class));
      return list;
   }

   public static class ModNumberSliderEntry extends GuiConfigEntries.NumberSliderEntry {
      private boolean enabled = true;

      public ModNumberSliderEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
         super(owningScreen, owningEntryList, configElement);
         ((GuiSlider)this.btnValue).precision = 2;
         this.updateValueButtonText();
      }

      public void setValue(double val) {
         ((GuiSlider)this.btnValue).setValue(val);
         ((GuiSlider)this.btnValue).updateSlider();
      }

      public boolean enabled() {
         return this.owningScreen.isWorldRunning ? !this.owningScreen.allRequireWorldRestart && !this.configElement.requiresWorldRestart() && this.enabled : this.enabled;
      }

      public void setEnabled(boolean enabled) {
         this.enabled = enabled;
      }
   }

   public static class ModBooleanEntry extends GuiConfigEntries.ButtonEntry {
      protected final boolean beforeValue;
      protected boolean currentValue;

      public ModBooleanEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
         super(owningScreen, owningEntryList, configElement);
         this.beforeValue = Boolean.valueOf(configElement.get().toString());
         this.currentValue = this.beforeValue;
         this.btnValue.enabled = this.enabled();
         this.updateValueButtonText();
      }

      public void updateValueButtonText() {
         this.btnValue.displayString = I18n.format(String.valueOf(this.currentValue), new Object[0]);
         this.btnValue.packedFGColour = this.currentValue ? GuiUtils.getColorCode('2', true) : GuiUtils.getColorCode('4', true);
      }

      public void valueButtonPressed(int slotIndex) {
         if (this.enabled()) {
            this.currentValue = !this.currentValue;
         }

      }

      public boolean isDefault() {
         return this.currentValue == Boolean.valueOf(this.configElement.getDefault().toString());
      }

      public void setToDefault() {
         if (this.enabled()) {
            this.currentValue = Boolean.valueOf(this.configElement.getDefault().toString());
            this.updateValueButtonText();
         }

      }

      public boolean isChanged() {
         return this.currentValue != this.beforeValue;
      }

      public void undoChanges() {
         if (this.enabled()) {
            this.currentValue = this.beforeValue;
            this.updateValueButtonText();
         }

      }

      public boolean saveConfigElement() {
         if (this.enabled() && this.isChanged()) {
            this.configElement.set(this.currentValue);
            return this.configElement.requiresMcRestart();
         } else {
            return false;
         }
      }

      public Boolean getCurrentValue() {
         return this.currentValue;
      }

      public Boolean[] getCurrentValues() {
         return new Boolean[]{this.getCurrentValue()};
      }

      public boolean enabled() {
         Iterator var1 = this.owningEntryList.listEntries.iterator();

         GuiConfigEntries.IConfigEntry entry;
         do {
            if (!var1.hasNext()) {
               return true;
            }

            entry = (GuiConfigEntries.IConfigEntry)var1.next();
         } while(!entry.getName().equals("circular") || !(entry instanceof GuiConfigEntries.BooleanEntry));

         return Boolean.valueOf(entry.getCurrentValue().toString());
      }
   }

   public static class MapModeConfigEntry extends GuiConfigEntries.CategoryEntry {
      public MapModeConfigEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
         super(owningScreen, owningEntryList, configElement);
      }

      protected GuiScreen buildChildScreen() {
         String QualifiedName = this.configElement.getQualifiedName();
         return new GuiConfig(this.owningScreen, this.getConfigElement().getChildElements(), this.owningScreen.modID, QualifiedName, this.configElement.requiresWorldRestart() || this.owningScreen.allRequireWorldRestart, this.configElement.requiresMcRestart() || this.owningScreen.allRequireMcRestart, this.owningScreen.title);
      }
   }
}
