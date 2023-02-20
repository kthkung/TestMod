//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.forge;

import com.mojang.realmsclient.RealmsMainScreen;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.gui.ChatFormatting;
import com.mojang.realmsclient.gui.screens.RealmsConfigureWorldScreen;
import com.mojang.realmsclient.gui.screens.RealmsLongRunningMcoTaskScreen;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.client.gui.pokechecker.GuiRenamePokemon;
import com.pixelmonmod.pixelmon.client.gui.pokechecker.GuiScreenPokeChecker;
import com.pixelmonmod.pixelmon.client.gui.pokechecker.GuiScreenPokeCheckerMoves;
import com.pixelmonmod.pixelmon.client.gui.pokechecker.GuiScreenPokeCheckerStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.EVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.IVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Stats;
import com.pixelmonmod.pixelmon.enums.EnumBossMode;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenRealmsProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.commons.lang3.reflect.FieldUtils;
import journeymap.Mw;
import journeymap.config.Config;
import journeymap.gui.RadarGui;
import journeymap.gui.RenamePokemonGui;
import journeymap.gui.ScreenPokeCheckerGui;
import journeymap.gui.ScreenPokeCheckerMovesGui;
import journeymap.gui.ScreenPokeCheckerStatsGui;
import journeymap.overlay.OverlaySlime;
import journeymap.util.Logging;
import journeymap.util.Reference;
import journeymap.util.Utils;

public class EventHandler {
   Mw mw;
   private static boolean firstDraw = false;

   public EventHandler(Mw mw) {
      this.mw = mw;
   }

   @SubscribeEvent
   public void eventChunkLoad(ChunkEvent.Load event) {
      if (event.getWorld().isRemote) {
         this.mw.onChunkLoad(event.getChunk());
      }

   }

   @SubscribeEvent
   public void eventChunkUnload(ChunkEvent.Unload event) {
      if (event.getWorld().isRemote) {
         this.mw.onChunkUnload(event.getChunk());
      }

   }

   @SubscribeEvent
   public void onClientChat(ClientChatReceivedEvent event) {
      if (!OverlaySlime.seedFound && OverlaySlime.seedAsked) {
         try {
            if (event.getMessage() instanceof TextComponentTranslation) {
               TextComponentTranslation component = (TextComponentTranslation)event.getMessage();
               if (component.getKey().equals("commands.seed.success")) {
                  Long lSeed = 0L;
                  if (component.getFormatArgs()[0] instanceof Long) {
                     lSeed = (Long)component.getFormatArgs()[0];
                  } else {
                     lSeed = Long.parseLong((String)component.getFormatArgs()[0]);
                  }

                  OverlaySlime.setSeed(lSeed);
                  event.setCanceled(true);
               }
            } else if (event.getMessage() instanceof TextComponentString) {
               TextComponentString component = (TextComponentString)event.getMessage();
               String msg = component.getUnformattedText();
               if (msg.startsWith("Seed: ")) {
                  OverlaySlime.setSeed(Long.parseLong(msg.substring(6)));
                  event.setCanceled(true);
               }
            }
         } catch (Exception var4) {
            Logging.logError("Something went wrong getting the seed. %s", new Object[]{var4.toString()});
         }

      }
   }

   @SubscribeEvent
   public void renderMap(RenderGameOverlayEvent.Post event) {
      if (event.getType() == ElementType.HOTBAR) {
         Mw.getInstance().onTick();
      }

   }

   @SubscribeEvent
   public void onTextureStitchEventPost(TextureStitchEvent.Post event) {
      if (Config.reloadColours) {
         Logging.logInfo("Skipping the first generation of blockcolours, models are not loaded yet", (Object[])null);
      } else {
         this.mw.reloadBlockColours();
      }

   }

   @SubscribeEvent
   public void renderWorldLastEvent(RenderWorldLastEvent event) {
      if (Mw.getInstance().ready) {
         Mw.getInstance().markerManager.drawMarkersWorld(event.getPartialTicks());
      }

   }

   @SubscribeEvent
   public void onPlayerInteractEvent(PlayerInteractEvent.EntityInteract event) {
      if (event.getTarget() instanceof EntityPixelmon && event.getHand() == EnumHand.MAIN_HAND && event.getItemStack().isEmpty()) {
         try {
            EntityPixelmon entity = (EntityPixelmon)event.getTarget();
            EntityPlayerMP player = entity.getPokemonData().getOwnerPlayer();
            EntityPlayer player1 = event.getEntityPlayer();
            String name = entity.getName();
            EnumBossMode bossMode = entity.getBossMode();
            Boolean isShiny = entity.getPokemonData().isShiny();
            int level = entity.getPokemonData().getLevelContainer().getLevel();
            BaseStats baseStats = entity.getBaseStats();
            Stats stats = entity.getPokemonData().getStats();
            Gender gender = entity.getPokemonData().getGender();
            EVStore eVsStore = entity.getPokemonData().getEVs();
            IVStore ivStore = entity.getPokemonData().getIVs();
            EnumNature nature = entity.getPokemonData().getNature();
            boolean isEgg = entity.getPokemonData().isEgg();
            int ivSum = ivStore.hp + ivStore.attack + ivStore.defence + ivStore.specialAttack + ivStore.specialDefence + ivStore.speed;
            int evSum = eVsStore.hp + eVsStore.attack + eVsStore.defence + eVsStore.specialAttack + eVsStore.specialDefence + eVsStore.speed;
            EnumType hiddenPower = EnumType.Normal;
            int form = entity.getPokemonData().getForm();
            String extra = "";
            if (Reference.LEGENDARIES.contains(name)) {
               extra = "Legendary";
            }

            if (Reference.DITTOS.contains(name)) {
               extra = "Ditto";
            }

            String owner = ChatFormatting.ITALIC + "WILD";
            if (entity.hasOwner()) {
               owner = entity.getOwner().getName();
            }

            player1.sendStatusMessage(new TextComponentString(""), false);
            player1.sendMessage(new TextComponentString(Reference.CHAT_BRANDING));
            player1.sendStatusMessage(new TextComponentString("Level: " + ChatFormatting.RESET + level + ChatFormatting.GREEN + " " + ChatFormatting.BOLD + name + ChatFormatting.RESET + (isShiny ? " (" + ChatFormatting.GREEN + "shiny" + ChatFormatting.RESET + ")" : "") + ChatFormatting.ITALIC + " " + ChatFormatting.DARK_AQUA + extra), false);
            player1.sendStatusMessage(new TextComponentString(ChatFormatting.GREEN + "Owner: " + ChatFormatting.RESET + owner), false);
            player1.sendStatusMessage(new TextComponentString(ChatFormatting.GREEN + "Ability: " + ChatFormatting.RESET + entity.getPokemonData().getAbilityName()), false);
            player1.sendStatusMessage(new TextComponentString(ChatFormatting.GREEN + "Growth: " + ChatFormatting.RESET + entity.getPokemonData().getGrowth().toString()), false);
            player1.sendStatusMessage(new TextComponentString(ChatFormatting.GREEN + "Nature: " + nature.toString() + ChatFormatting.RESET + " +" + RadarGui.getNatureShorthand(nature.increasedStat) + " -" + RadarGui.getNatureShorthand(nature.decreasedStat)), false);
            player1.sendStatusMessage(new TextComponentString(""), false);
         } catch (Exception var22) {
            var22.printStackTrace();
         }
      }

   }

   @SubscribeEvent
   public void onGuiOpenEvent(GuiOpenEvent event) {
      GuiScreen guiScreen = event.getGui();
      if (guiScreen instanceof GuiRenamePokemon && !(guiScreen instanceof RenamePokemonGui)) {
         GuiRenamePokemon gui = (GuiRenamePokemon)guiScreen;
         GuiScreenPokeChecker parent = (GuiScreenPokeChecker)ReflectionHelper.getPrivateValue(GuiRenamePokemon.class, gui, new String[]{"parent"});
         event.setGui(new RenamePokemonGui(parent));
      } else {
         GuiScreenPokeChecker gui;
         Pokemon pData;
         if (guiScreen instanceof GuiScreenPokeCheckerStats && !(guiScreen instanceof ScreenPokeCheckerStatsGui)) {
            gui = (GuiScreenPokeChecker)guiScreen;
            pData = (Pokemon)ReflectionHelper.getPrivateValue(GuiScreenPokeChecker.class, gui, new String[]{"pokemon"});
            GuiScreenPokeCheckerStats guiStats = (GuiScreenPokeCheckerStats)guiScreen;
            Pokemon pDatas = (Pokemon)ReflectionHelper.getPrivateValue(GuiScreenPokeChecker.class, guiStats, new String[]{"pokemon"});
            StoragePosition isPC = (StoragePosition)ReflectionHelper.getPrivateValue(GuiScreenPokeChecker.class, guiStats, new String[]{"position"});
            GuiScreen box = (GuiScreen)ReflectionHelper.getPrivateValue(GuiScreenPokeChecker.class, guiStats, new String[]{"parent"});
            event.setGui(new ScreenPokeCheckerStatsGui(pDatas.getStorage(), isPC, box));
         } else if (guiScreen instanceof GuiScreenPokeCheckerMoves && !(guiScreen instanceof ScreenPokeCheckerMovesGui)) {
            gui = (GuiScreenPokeChecker)guiScreen;
            pData = (Pokemon)ReflectionHelper.getPrivateValue(GuiScreenPokeChecker.class, gui, new String[]{"pokemon"});
            GuiScreenPokeCheckerMoves guiMoves = (GuiScreenPokeCheckerMoves)guiScreen;
            StoragePosition isPC = (StoragePosition)ReflectionHelper.getPrivateValue(GuiScreenPokeChecker.class, guiMoves, new String[]{"position"});
            GuiScreen box = (GuiScreen)ReflectionHelper.getPrivateValue(GuiScreenPokeChecker.class, guiMoves, new String[]{"parent"});
            event.setGui(new ScreenPokeCheckerMovesGui(pData.getStorage(), isPC, box));
         } else if (guiScreen instanceof GuiScreenPokeChecker && !(guiScreen instanceof ScreenPokeCheckerGui) && !(guiScreen instanceof ScreenPokeCheckerStatsGui)) {
            gui = (GuiScreenPokeChecker)guiScreen;
            pData = (Pokemon)ReflectionHelper.getPrivateValue(GuiScreenPokeChecker.class, gui, new String[]{"pokemon"});
            StoragePosition isPC = (StoragePosition)ReflectionHelper.getPrivateValue(GuiScreenPokeChecker.class, gui, new String[]{"position"});
            GuiScreen box = (GuiScreen)ReflectionHelper.getPrivateValue(GuiScreenPokeChecker.class, gui, new String[]{"parent"});
            event.setGui(new ScreenPokeCheckerGui(pData.getStorage(), isPC, box));
         } else if (guiScreen instanceof GuiMainMenu && Config.reloadColours) {
            this.mw.reloadBlockColours();
            Config.reloadColours = false;
         } else if (guiScreen instanceof GuiGameOver) {
            this.mw.onPlayerDeath();
         } else if (guiScreen instanceof GuiScreenRealmsProxy) {
            try {
               RealmsScreen proxy = ((GuiScreenRealmsProxy)guiScreen).getProxy();
               RealmsMainScreen parrent = null;
               if (proxy instanceof RealmsLongRunningMcoTaskScreen || proxy instanceof RealmsConfigureWorldScreen) {
                  Object obj = FieldUtils.readField(proxy, "lastScreen", true);
                  if (obj instanceof RealmsMainScreen) {
                     parrent = (RealmsMainScreen)obj;
                  }

                  if (parrent != null) {
                     long id = (Long)FieldUtils.readField(parrent, "selectedServerId", true);
                     if (id > 0L) {
                        ArrayList list = (ArrayList)FieldUtils.readField(parrent, "realmsServers", true);

                        StringBuilder builder;
                        for(Iterator var9 = list.iterator(); var9.hasNext(); Utils.RealmsWorldName = builder.toString()) {
                           Object item = var9.next();
                           RealmsServer server = (RealmsServer)item;
                           String Name = server.getName();
                           String Owner = server.owner;
                           builder = new StringBuilder();
                           builder.append(server.owner);
                           builder.append("_");
                           builder.append(server.getName());
                        }
                     }
                  }
               }
            } catch (IllegalAccessException var15) {
               Pokeradar.logger.error(var15.getMessage());
            }
         }
      }

   }
}
