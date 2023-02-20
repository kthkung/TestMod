//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.entities;

import com.pixelmonmod.pixelmon.blocks.enums.EnumPokeChestType;
import com.pixelmonmod.pixelmon.blocks.enums.EnumPokechestVisibility;
import com.pixelmonmod.pixelmon.blocks.machines.BlockShrine;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityPokeChest;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityShrine;
import com.pixelmonmod.pixelmon.entities.EntityWormhole;
import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.EVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.IVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Stats;
import com.pixelmonmod.pixelmon.enums.EnumBossMode;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import journeymap.gui.RadarGui;
import journeymap.map.MapRenderer;
import journeymap.map.PKMarker;
import journeymap.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EntityFinder {
   public static List<PKMarker> pkMarkers = new ArrayList();
   public static boolean evolved = false;
   private static final List<String> legendaryList;
   private static final List<String> dittoList;

   public EntityFinder() {
   }

   public static void getAndDraw(MapRenderer mp, Minecraft mc) {
      if (mc.world != null && mc.world.getLoadedEntityList().size() > 1) {
         List<Entity> entityList = mc.world.loadedEntityList;
         List<TileEntity> tileList = mc.world.loadedTileEntityList;
         Iterator entityIt = entityList.iterator();
         Iterator tileIt = tileList.iterator();

         while(true) {
            Object entityObj;
            while(entityIt.hasNext()) {
               entityObj = entityIt.next();
               if (entityObj instanceof EntityPixelmon) {
                  EntityPixelmon entity = (EntityPixelmon)entityObj;

                  try {
                     Object spawnLocation = entity.getSpawnLocation();
                     Boolean hasOwner = entity.hasOwner();
                     String name = entity.getPokemonName();
                     EnumBossMode bossMode = entity.getBossMode();
                     Boolean isShiny = entity.getPokemonData().isShiny();
                     int level = entity.getPokemonData().getLevelContainer().getLevel();
                     BaseStats baseStats = entity.getBaseStats();
                     Stats stats = entity.getPokemonData().getStats();
                     EVStore eVsStore = entity.getPokemonData().getEVs();
                     IVStore ivStore = entity.getPokemonData().getIVs();
                     EnumNature nature = entity.getPokemonData().getNature();
                     int ivSum = 0;
                     EntityType type = EntityType.REGULAR;
                     String nameLower = name.toLowerCase();
                     String desc = "";
                     String levelStr = "Lvl " + level;
                     String ivStr = "IV " + (int)((double)ivSum / 186.0 * 100.0) + "%";
                     String natureStr = nature.getLocalizedName();
                     if (isShiny) {
                        type = EntityType.SHINY;
                     }

                     if (RadarGui.searchString != null) {
                        String[] nameArr = RadarGui.searchString.toLowerCase().split(",");

                        for(int na = 0; na < nameArr.length; ++na) {
                           if (nameArr[na].trim().length() >= 3 && nameLower.contains(nameArr[na].trim())) {
                              if (isShiny) {
                                 type = EntityType.SPECIAL;
                              } else {
                                 type = EntityType.SEARCHED;
                              }
                           }

                           // if (RadarGui.searchString.contains("Pokedex")) {
                           //    Pokedex playerDex;
                           //    if (mc.func_71356_B()) {
                           //       playerDex = Pixelmon.storageManager.getParty(mc.currentScreen.()).pokedex;
                           //       Pokedex.loadPokedex();
                           //       if (!playerDex.hasCaught(Pokedex.nameToID(entity.getLocalizedName()))) {
                           //          type = EntityType.DEX;
                           //          desc = "Missing in Pokedex";
                           //       }
                           //    } else {
                           //       playerDex = ClientStorageManager.party.pokedex;
                           //       Pokedex.loadPokedex();
                           //       if (!playerDex.hasCaught(Pokedex.nameToID(entity.getLocalizedName()))) {
                           //          type = EntityType.DEX;
                           //          desc = "Missing in Pokedex";
                           //       }
                           //    }
                           // }
                        }
                     }

                     if (RadarGui.getEV() != null && eVsStore != null) {
                        Boolean badEV = false;
                        if (entity.getPokemonData().getGrowth().index == 8 && !RadarGui.getEV().equals(GrowthType.Microscopic)) {
                           badEV = true;
                        }

                        if (entity.getPokemonData().getGrowth().index == 0 && !RadarGui.getEV().equals(GrowthType.Pygmy)) {
                           badEV = true;
                        }

                        if (entity.getPokemonData().getGrowth().index == 1 && !RadarGui.getEV().equals(GrowthType.Runt)) {
                           badEV = true;
                        }

                        if (entity.getPokemonData().getGrowth().index == 2 && !RadarGui.getEV().equals(GrowthType.Small)) {
                           badEV = true;
                        }

                        if (entity.getPokemonData().getGrowth().index == 3 && !RadarGui.getEV().equals(GrowthType.Ordinary)) {
                           badEV = true;
                        }

                        if (entity.getPokemonData().getGrowth().index == 4 && !RadarGui.getEV().equals(GrowthType.Huge)) {
                           badEV = true;
                        }

                        if (entity.getPokemonData().getGrowth().index == 5 && !RadarGui.getEV().equals(GrowthType.Giant)) {
                           badEV = true;
                        }

                        if (entity.getPokemonData().getGrowth().index == 6 && !RadarGui.getEV().equals(GrowthType.Enormous)) {
                           badEV = true;
                        }

                        if (entity.getPokemonData().getGrowth().index == 7 && !RadarGui.getEV().equals(GrowthType.Ginormous)) {
                           badEV = true;
                        }

                        if (badEV) {
                           type = isShiny ? type : EntityType.REGULAR;
                        } else if (isShiny) {
                           type = EntityType.SPECIAL;
                        } else {
                           type = EntityType.SEARCHED;
                        }
                     }

                     if (RadarGui.getNature() != null && nature != null && (type == EntityType.SEARCHED || type == EntityType.SPECIAL)) {
                        boolean foundNature = false;
                        if (RadarGui.getNature().equals(nature)) {
                           foundNature = true;
                        }

                        if (!foundNature) {
                           type = isShiny ? type : EntityType.REGULAR;
                        } else if (isShiny) {
                           type = EntityType.SPECIAL;
                        } else {
                           type = EntityType.SEARCHED;
                        }
                     }

                     if (legendaryList.contains(name)) {
                        type = EntityType.SPECIAL;
                        desc = "Legendary ";
                     }

                     if (dittoList.contains(name)) {
                        type = EntityType.DITTO;
                        desc = "Ditto ";
                     }

                     if (bossMode != EnumBossMode.NotBoss && bossMode != EnumBossMode.Equal) {
                        switch (bossMode) {
                           case Uncommon:
                              type = EntityType.BOSS_UNCOMMON;
                              break;
                           case Rare:
                              type = EntityType.BOSS_RARE;
                              break;
                           case Legendary:
                              type = EntityType.BOSS_LEGENDARY;
                              break;
                           case Ultimate:
                              type = EntityType.BOSS_ULTIMATE;
                        }

                        desc = desc + (desc.isEmpty() ? "" : " ") + bossMode.toString() + " Boss";
                     } else {
                        desc = desc + (desc.isEmpty() ? "" : " ") + levelStr;
                     }

                     if (spawnLocation != null && spawnLocation.toString().equals("Statue")) {
                        type = EntityType.STATUE;
                        desc = "Statue";
                     }

                     pkMarkers.add(new PKMarker(type, entity, name, desc, type.getRadius(), mp, mc.world.provider.getDimensionType().getId()));
                  } catch (Exception var37) {
                     var37.printStackTrace();
                  }
               } else if (entityObj instanceof EntityOtherPlayerMP) {
                  try {
                     EntityOtherPlayerMP entity = (EntityOtherPlayerMP)entityObj;
                     pkMarkers.add(new PKMarker(EntityType.PLAYER, entity, entity.getDisplayNameString(), "Player", EntityType.PLAYER.getRadius(), mp, mc.world.provider.getDimensionType().getId()));
                  } catch (SecurityException var35) {
                     var35.printStackTrace();
                  } catch (IllegalArgumentException var36) {
                     var36.printStackTrace();
                  }
               } else if (entityObj instanceof NPCTrainer) {
                  try {
                     NPCTrainer entity = (NPCTrainer)entityObj;
                     pkMarkers.add(new PKMarker(EntityType.NPC, entity, "NPC Trainer", "Level: " + entity.getDisplayText(), EntityType.NPC.getRadius(), mp, mc.world.provider.getDimensionType().getId()));
                  } catch (SecurityException var33) {
                     var33.printStackTrace();
                  } catch (IllegalArgumentException var34) {
                     var34.printStackTrace();
                  }
               } else if (entityObj instanceof EntityWormhole) {
                  try {
                     EntityWormhole entity = (EntityWormhole)entityObj;
                     pkMarkers.add(new PKMarker(EntityType.WORMHOLE, entity, "Wormhole", " ", EntityType.WORMHOLE.getRadius(), mp, mc.world.provider.getDimensionType().getId()));
                  } catch (SecurityException var31) {
                     var31.printStackTrace();
                  } catch (IllegalArgumentException var32) {
                     var32.printStackTrace();
                  }
               } else if (entityObj instanceof EntityNPC) {
                  try {
                     EntityNPC entity = (EntityNPC)entityObj;
                     pkMarkers.add(new PKMarker(EntityType.NPC, entity, entity.getDisplayText(), "NPC", EntityType.NPC.getRadius(), mp, mc.world.provider.getDimensionType().getId()));
                  } catch (SecurityException var29) {
                     var29.printStackTrace();
                  } catch (IllegalArgumentException var30) {
                     var30.printStackTrace();
                  }
               }
            }

            while(tileIt.hasNext()) {
               entityObj = tileIt.next();
               String desc;
               if (entityObj instanceof TileEntityPokeChest) {
                  TileEntityPokeChest tile = (TileEntityPokeChest)entityObj;
                  EnumPokeChestType chestType = tile.getType();
                  desc = "";
                  switch (chestType) {
                     case SPECIAL:
                        desc = "Special";
                        break;
                     case POKEBALL:
                        desc = "Tier 1";
                        break;
                     case ULTRABALL:
                        desc = "Tier 2";
                        break;
                     case MASTERBALL:
                        desc = "Tier 3";
                        break;
                     case BEASTBALL:
                        desc = "Tier 4";
                  }

                  if (tile.getVisibility() == EnumPokechestVisibility.Hidden) {
                     desc = "Hidden";
                  }

                  pkMarkers.add(new PKMarker(EntityType.LOOT, tile, "PokeLoot", desc, EntityType.LOOT.getRadius(), mp, mc.world.provider.getDimensionType().getId()));
               }

               if (entityObj instanceof TileEntityShrine) {
                  TileEntityShrine tile = (TileEntityShrine)entityObj;
                  BlockShrine blockShrine = (BlockShrine)tile.getBlockType();
                  desc = "";
                  if (blockShrine != null && blockShrine.rockType != null) {
                     desc = blockShrine.rockType.toString();
                  }

                  pkMarkers.add(new PKMarker(EntityType.SHRINE, tile, "PokeShrine", desc, EntityType.SHRINE.getRadius(), mp, mc.world.provider.getDimensionType().getId()));
               }
            }
            break;
         }
      }

   }

   static {
      legendaryList = Reference.LEGENDARIES;
      dittoList = Reference.DITTOS;
   }
}
