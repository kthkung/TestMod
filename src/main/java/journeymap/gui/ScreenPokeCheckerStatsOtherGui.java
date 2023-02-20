// package pixelradar.gui;

// import com.google.common.collect.UnmodifiableIterator;
// import com.mojang.realmsclient.gui.ChatFormatting;
// import com.pixelmonmod.pixelmon.Pixelmon;
// import com.pixelmonmod.pixelmon.api.spawning.SpawnInfo;
// import com.pixelmonmod.pixelmon.api.spawning.SpawnSet;
// import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.pokemon.SpawnInfoPokemon;
// import com.pixelmonmod.pixelmon.api.spawning.util.SetLoader;
// import com.pixelmonmod.pixelmon.api.storage.PokemonStorage;
// import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
// import com.pixelmonmod.pixelmon.api.world.WorldTime;
// import com.pixelmonmod.pixelmon.client.gui.pokechecker.GuiPokeCheckerTabs;
// import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
// import com.pixelmonmod.pixelmon.config.PixelmonConfig;
// import com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats.LakeTrioStats;
// import com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats.MewStats;
// import com.pixelmonmod.pixelmon.enums.EnumEggGroup;
// import com.pixelmonmod.pixelmon.enums.EnumType;
// import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
// import java.awt.Color;
// import java.io.IOException;
// import java.util.ArrayList;
// import java.util.Collections;
// import java.util.Iterator;
// import net.minecraft.client.gui.GuiButton;
// import net.minecraft.client.gui.GuiScreen;
// import net.minecraft.item.ItemStack;
// import net.minecraft.nbt.NBTTagCompound;
// import net.minecraft.util.text.TextFormatting;
// import net.minecraft.util.text.translation.I18n;
// import net.minecraft.world.biome.Biome;
// import net.minecraftforge.fml.common.registry.GameRegistry;
// import org.lwjgl.opengl.GL11;

// public class ScreenPokeCheckerStatsOtherGui extends ScreenPokeCheckerStatsGui {
//    private int hexWhite;

//    public ScreenPokeCheckerStatsOtherGui(PokemonStorage selected, StoragePosition b, GuiScreen box) {
//       super(selected, b, box);
//       this.hexWhite = Color.WHITE.getRGB();
//    }

//    public void func_73866_w_() {
//       super.func_73866_w_();
//       this.field_146292_n.add(new GuiPokeCheckerTabs(2, 200, this.field_146294_l / 2 + 36, this.field_146295_m / 2 + 80, 69, 15, I18n.func_74838_a("gui.screenpokechecker.stats")));
//    }

//    public void func_146284_a(GuiButton button) throws IOException {
//       if (button.field_146127_k == 200) {
//          this.mc.displayGuiScreen(new ScreenPokeCheckerStatsGui(this.pokemon.getStorage(), this.isPC, this.box));
//       } else {
//          super.func_146284_a(button);
//       }

//    }

//    public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
//       int hexColor;
//       int ivSum;
//       String strAP;
//       String strHP;
//       int def;
//       int sp;
//       SpawnInfo info;
//       PlayerPartyStorage storage;
//       NBTTagCompound nbt;
//       int[] var25;
//       int steps;
//       EnumType hiddenPower;
//       ArrayList allBiomes;
//       int atk;
//       UnmodifiableIterator var35;
//       ArrayList allBiomes;
//       Object set;
//       Iterator var39;
//       Iterator var41;
//       Biome biome3;
//       int i;
//       int i;
//       double top;
//       String strAP;
//       String strXP;
//       int typeHidden;
//       boolean isEgg;
//       if (this.mc.func_71356_B()) {
//          hexColor = this.hexWhite;
//          GL11.glNormal3f(0.0F, -1.0F, 0.0F);
//          this.func_73731_b(this.mc.field_71466_p, I18n.func_74838_a("gui.screenpokechecker.lvl") + " " + this.pokemon.getLevel(), 10, -14, hexColor);
//          this.func_73731_b(this.mc.field_71466_p, I18n.func_74838_a("gui.screenpokechecker.number") + " " + this.pokemon.getSpecies().getNationalPokedexInteger(), -30, -14, hexColor);
//          this.func_73732_a(this.mc.field_71466_p, String.valueOf(this.pokemon.getOriginalTrainer()), 8, 126, hexColor);
//          this.func_73731_b(this.mc.field_71466_p, I18n.func_74838_a("gui.screenpokechecker.ot"), -32, 112, hexColor);
//          String strHP;
//          if (this.pokemon.getSpecies().getNationalPokedexInteger() != 480 && this.pokemon.getSpecies().getNationalPokedexInteger() != 481 && this.pokemon.getSpecies().getNationalPokedexInteger() != 482) {
//             if (this.pokemon.getSpecies().getNationalPokedexInteger() == 151) {
//                ivSum = ((MewStats)this.pokemon.getExtraStats()).numCloned;
//                this.func_73731_b(this.mc.field_71466_p, I18n.func_74838_a(ChatFormatting.YELLOW + "Held Item"), 60, 9, hexColor);
//                strAP = String.valueOf(this.pokemon.getHeldItem().func_82833_r());
//                this.func_73731_b(this.mc.field_71466_p, strAP, 175 - strAP.length() * 3, 9, hexColor);
//                this.func_73731_b(this.mc.field_71466_p, I18n.func_74838_a(ChatFormatting.YELLOW + "Number of Clones"), 60, -12, hexColor);
//                strHP = String.valueOf(ivSum);
//                this.func_73731_b(this.mc.field_71466_p, strHP, 200 - strHP.length() * 3, -12, hexColor);
//                hexColor = this.hexWhite;
//             } else if (this.pokemon.getPosition().order <= 5 && !this.pokemon.isInRanch() && !this.pokemon.isEgg()) {
//                storage = Pixelmon.storageManager.getParty(this.pokemon.getOwnerPlayerUUID());
//                EnumEggGroup[] eggGroups = this.pokemon.getBaseStats().eggGroups;
//                StringBuilder msg = new StringBuilder();
//                EnumEggGroup[] var30 = eggGroups;
//                def = eggGroups.length;

//                for(sp = 0; sp < def; ++sp) {
//                   EnumEggGroup egg = var30[sp];
//                   msg.append(TextFormatting.DARK_AQUA).append(egg.name()).append(TextFormatting.GOLD).append(", ");
//                }

//                allBiomes = new ArrayList();
//                var35 = SetLoader.getAllSets().iterator();

//                while(true) {
//                   if (!var35.hasNext()) {
//                      allBiomes = new ArrayList();
//                      var39 = GameRegistry.findRegistry(Biome.class).iterator();

//                      while(var39.hasNext()) {
//                         biome3 = (Biome)var39.next();
//                         allBiomes.add(biome3);
//                      }

//                      UnmodifiableIterator var42 = SetLoader.getAllSets().iterator();

//                      while(var42.hasNext()) {
//                         Object set = var42.next();
//                         if (allBiomes.isEmpty()) {
//                            break;
//                         }

//                         Iterator var48 = ((SpawnSet)set).spawnInfos.iterator();

//                         while(var48.hasNext()) {
//                            SpawnInfo info = (SpawnInfo)var48.next();
//                            if (info instanceof SpawnInfoPokemon && ((SpawnInfoPokemon)info).getPokemonSpec().name.equalsIgnoreCase(this.pokemon.getSpecies().name)) {
//                               if (((SpawnInfoPokemon)info).getPokemonSpec().form != null) {
//                                  if (this.pokemon.getFormEnum().getForm() == ((SpawnInfoPokemon)info).getPokemonSpec().form) {
//                                     if (info.condition != null && info.condition.biomes != null && !info.condition.biomes.isEmpty()) {
//                                        allBiomes.removeIf((biome) -> {
//                                           return !info.condition.biomes.contains(biome);
//                                        });
//                                     }

//                                     if (info.anticondition != null && info.anticondition.biomes != null && !info.anticondition.biomes.isEmpty()) {
//                                        allBiomes.removeIf((biome) -> {
//                                           return info.anticondition.biomes.contains(biome);
//                                        });
//                                     }

//                                     if (info.compositeCondition != null) {
//                                        if (info.compositeCondition.conditions != null) {
//                                           info.compositeCondition.conditions.forEach((condition) -> {
//                                              if (condition.biomes != null && !condition.biomes.isEmpty()) {
//                                                 allBiomes.removeIf((biome) -> {
//                                                    return !condition.biomes.contains(biome);
//                                                 });
//                                              }

//                                           });
//                                        }

//                                        if (info.compositeCondition.anticonditions != null) {
//                                           info.compositeCondition.anticonditions.forEach((anticondition) -> {
//                                              if (anticondition.biomes != null && anticondition.biomes.isEmpty()) {
//                                                 allBiomes.removeIf((biome) -> {
//                                                    return anticondition.biomes.contains(biome);
//                                                 });
//                                              }

//                                           });
//                                        }
//                                     }
//                                  }
//                               } else {
//                                  if (info.condition != null && info.condition.biomes != null && !info.condition.biomes.isEmpty()) {
//                                     allBiomes.removeIf((biome) -> {
//                                        return !info.condition.biomes.contains(biome);
//                                     });
//                                  }

//                                  if (info.anticondition != null && info.anticondition.biomes != null && !info.anticondition.biomes.isEmpty()) {
//                                     allBiomes.removeIf((biome) -> {
//                                        return info.anticondition.biomes.contains(biome);
//                                     });
//                                  }

//                                  if (info.compositeCondition != null) {
//                                     if (info.compositeCondition.conditions != null) {
//                                        info.compositeCondition.conditions.forEach((condition) -> {
//                                           if (condition.biomes != null && !condition.biomes.isEmpty()) {
//                                              allBiomes.removeIf((biome) -> {
//                                                 return !condition.biomes.contains(biome);
//                                              });
//                                           }

//                                        });
//                                     }

//                                     if (info.compositeCondition.anticonditions != null) {
//                                        info.compositeCondition.anticonditions.forEach((anticondition) -> {
//                                           if (anticondition.biomes != null && anticondition.biomes.isEmpty()) {
//                                              allBiomes.removeIf((biome) -> {
//                                                 return anticondition.biomes.contains(biome);
//                                              });
//                                           }

//                                        });
//                                     }
//                                  }
//                               }
//                            }
//                         }
//                      }

//                      ArrayList<String> biomeNames = new ArrayList();
//                      var41 = allBiomes.iterator();

//                      while(var41.hasNext()) {
//                         Biome biome3 = (Biome)var41.next();
//                         biomeNames.add(biome3.func_185359_l());
//                      }

//                      StringBuilder message = new StringBuilder(TextFormatting.RED + "None.");
//                      if (!biomeNames.isEmpty()) {
//                         Collections.sort(biomeNames);
//                         message = new StringBuilder(TextFormatting.DARK_AQUA + (String)biomeNames.get(0));

//                         for(i = 1; i < biomeNames.size(); ++i) {
//                            message.append(TextFormatting.GOLD + ", " + TextFormatting.DARK_AQUA).append((String)biomeNames.get(i));
//                         }
//                      }

//                      this.func_73731_b(this.mc.field_71466_p, I18n.func_74838_a(ChatFormatting.YELLOW + "Held Item"), 60, 9, hexColor);
//                      strAP = String.valueOf(this.pokemon.getHeldItem().func_82833_r());
//                      this.func_73731_b(this.mc.field_71466_p, strAP, 175 - strAP.length() * 3, 9, hexColor);
//                      this.func_73731_b(this.mc.field_71466_p, I18n.func_74838_a(ChatFormatting.YELLOW + "EggGroup"), 60, 30, hexColor);
//                      String strHP = msg.substring(0, msg.length() - 2);
//                      this.func_73731_b(this.mc.field_71466_p, strHP, 200 - strHP.length() * 3, 30, hexColor);
//                      this.func_73731_b(this.mc.field_71466_p, I18n.func_74838_a(ChatFormatting.YELLOW + "Spawntime"), 60, 51, hexColor);
//                      StringBuilder msg2 = new StringBuilder();
//                      if (allBiomes != null) {
//                         Iterator var14 = allBiomes.iterator();

//                         while(var14.hasNext()) {
//                            WorldTime time = (WorldTime)var14.next();
//                            msg2.append(TextFormatting.DARK_AQUA).append(time.name()).append(TextFormatting.GOLD).append(", ");
//                         }
//                      } else {
//                         msg2.append(ChatFormatting.DARK_AQUA + "-  ");
//                      }

//                      strXP = msg2.substring(0, msg2.length() - 2);
//                      this.func_73731_b(this.mc.field_71466_p, strXP, 200 - strXP.length() * 3, 51, hexColor);
//                      this.func_73731_b(this.mc.field_71466_p, I18n.func_74838_a(ChatFormatting.YELLOW + "Biome"), 60, 72, hexColor);
//                      String strWP = message.toString();
//                      this.func_73731_b(this.mc.field_71466_p, strWP, 200 - strWP.length() * 3, 72, hexColor);
//                      float rarity = -1.0F;
//                      UnmodifiableIterator var17 = SetLoader.getAllSets().iterator();

//                      while(var17.hasNext()) {
//                         Object set = var17.next();
//                         Iterator var19 = ((SpawnSet)set).spawnInfos.iterator();

//                         while(var19.hasNext()) {
//                            SpawnInfo info = (SpawnInfo)var19.next();
//                            if (info instanceof SpawnInfoPokemon && ((SpawnInfoPokemon)info).getPokemonSpec().name.equalsIgnoreCase(this.pokemon.getSpecies().name)) {
//                               rarity = info.rarity;
//                            }
//                         }
//                      }

//                      this.func_73731_b(this.mc.field_71466_p, I18n.func_74838_a(ChatFormatting.YELLOW + "Rarity"), 60, 93, hexColor);
//                      String strLP = String.valueOf(rarity);
//                      this.func_73731_b(this.mc.field_71466_p, ChatFormatting.DARK_AQUA + strLP, 200 - strLP.length() * 3, 93, hexColor);
//                      hexColor = this.hexWhite;
//                      break;
//                   }

//                   set = var35.next();
//                   var41 = ((SpawnSet)set).spawnInfos.iterator();

//                   while(var41.hasNext()) {
//                      info = (SpawnInfo)var41.next();
//                      if (info instanceof SpawnInfoPokemon && ((SpawnInfoPokemon)info).getPokemonSpec().name.equalsIgnoreCase(this.pokemon.getSpecies().name)) {
//                         allBiomes = info.condition.times;
//                      }
//                   }
//                }
//             } else if (this.pokemon.isEgg() && this.pokemon.getPosition().order <= 5 && !this.pokemon.isInRanch()) {
//                storage = Pixelmon.storageManager.getParty(this.pokemon.getOwnerPlayerUUID());
//                nbt = storage.get(this.pokemon.getPosition().order).getPersistentData();
//                steps = (Pixelmon.storageManager.getParty(this.pokemon.getOwnerPlayerUUID()).get(this.position).getEggCycles() + 1) * PixelmonConfig.stepsPerEggCycle - Pixelmon.storageManager.getParty(this.mc.field_71439_g.func_110124_au()).get(this.position).getEggSteps();
//                this.func_73731_b(this.mc.field_71466_p, I18n.func_74838_a(ChatFormatting.YELLOW + "Eggsteps needed"), 60, -12, hexColor);
//                strHP = String.valueOf(steps);
//                this.func_73731_b(this.mc.field_71466_p, strHP, 200 - strHP.length() * 3, -12, hexColor);
//                hexColor = this.hexWhite;
//             } else {
//                this.func_73731_b(this.mc.field_71466_p, I18n.func_74838_a(ChatFormatting.RED + "No Infos about that Pokemon"), 60, -12, hexColor);
//                if (this.pokemon.getHeldItem() != ItemStack.field_190927_a) {
//                   this.func_73731_b(this.mc.field_71466_p, I18n.func_74838_a(ChatFormatting.YELLOW + "Held Item"), 60, 9, hexColor);
//                   String strHP = String.valueOf(this.pokemon.getHeldItem().func_82833_r());
//                   this.func_73731_b(this.mc.field_71466_p, strHP, 175 - strHP.length() * 3, 9, hexColor);
//                   hexColor = this.hexWhite;
//                }
//             }
//          } else {
//             ivSum = ((LakeTrioStats)this.pokemon.getExtraStats()).numEnchanted;
//             strAP = String.valueOf(ivSum);
//             this.func_73731_b(this.mc.field_71466_p, I18n.func_74838_a(ChatFormatting.YELLOW + "Held Item"), 60, 9, hexColor);
//             strHP = String.valueOf(this.pokemon.getHeldItem().func_82833_r());
//             this.func_73731_b(this.mc.field_71466_p, strHP, 175 - strHP.length() * 3, 9, hexColor);
//             this.func_73731_b(this.mc.field_71466_p, I18n.func_74838_a(ChatFormatting.YELLOW + "Number of Enchantments"), 60, -12, hexColor);
//             this.func_73731_b(this.mc.field_71466_p, strAP, 200 - strAP.length() * 3, -12, hexColor);
//             hexColor = this.hexWhite;
//          }

//          hexColor = this.hexWhite;
//          this.func_73732_a(this.mc.field_71466_p, "Ability", 95, 115, hexColor);
//          this.func_73732_a(this.mc.field_71466_p, "Owner", 174, 115, hexColor);
//          this.func_73732_a(this.mc.field_71466_p, I18n.func_74838_a("gui.screenpokechecker.growth"), 8, 137, hexColor);
//          ivSum = 0;
//          var25 = this.pokemon.getIVs().getArray();
//          steps = var25.length;

//          for(atk = 0; atk < steps; ++atk) {
//             def = var25[atk];
//             ivSum += def;
//          }

//          hiddenPower = EnumType.Normal;
//          steps = this.pokemon.getIVs().hp % 2;
//          atk = this.pokemon.getIVs().attack % 2;
//          def = this.pokemon.getIVs().defence % 2;
//          sp = this.pokemon.getIVs().speed % 2;
//          i = this.pokemon.getIVs().specialAttack % 2;
//          i = this.pokemon.getIVs().specialDefence % 2;
//          top = (double)(32 * i + 16 * i + 8 * sp + 4 * def + 2 * atk + steps);
//          typeHidden = (int)Math.floor(top * 15.0D / 63.0D);
//          switch(typeHidden) {
//          case 0:
//             hiddenPower = EnumType.Fighting;
//             break;
//          case 1:
//             hiddenPower = EnumType.Flying;
//             break;
//          case 2:
//             hiddenPower = EnumType.Poison;
//             break;
//          case 3:
//             hiddenPower = EnumType.Ground;
//             break;
//          case 4:
//             hiddenPower = EnumType.Rock;
//             break;
//          case 5:
//             hiddenPower = EnumType.Bug;
//             break;
//          case 6:
//             hiddenPower = EnumType.Ghost;
//             break;
//          case 7:
//             hiddenPower = EnumType.Steel;
//             break;
//          case 8:
//             hiddenPower = EnumType.Fire;
//             break;
//          case 9:
//             hiddenPower = EnumType.Water;
//             break;
//          case 10:
//             hiddenPower = EnumType.Grass;
//             break;
//          case 11:
//             hiddenPower = EnumType.Electric;
//             break;
//          case 12:
//             hiddenPower = EnumType.Psychic;
//             break;
//          case 13:
//             hiddenPower = EnumType.Ice;
//             break;
//          case 14:
//             hiddenPower = EnumType.Dragon;
//             break;
//          case 15:
//             hiddenPower = EnumType.Dark;
//          }

//          this.func_73732_a(this.mc.field_71466_p, Pixelmon.storageManager.getParty(this.pokemon.getOwnerPlayerUUID()).get(this.position).getAbility().getLocalizedName(), 95, 130, hexColor);
//          this.func_73732_a(this.mc.field_71466_p, Pixelmon.storageManager.getParty(this.pokemon.getOwnerPlayerUUID()).get(this.position).getOwnerPlayer().func_70005_c_(), 174, 130, -1);
//          this.func_73732_a(this.mc.field_71466_p, this.pokemon.getGrowth().getLocalizedName(), 8, 150, -1);
//          this.func_73731_b(this.mc.field_71466_p, I18n.func_74838_a("gui.screenpokechecker.stats"), 145, 166, hexColor);
//          isEgg = this.pokemon.isEgg();
//          isEgg = false;
//          this.drawBasePokemonInfo();
//       } else {
//          hexColor = this.hexWhite;
//          GL11.glNormal3f(0.0F, -1.0F, 0.0F);
//          this.func_73731_b(this.mc.field_71466_p, I18n.func_74838_a("gui.screenpokechecker.lvl") + " " + this.pokemon.getLevel(), 10, -14, hexColor);
//          this.func_73731_b(this.mc.field_71466_p, I18n.func_74838_a("gui.screenpokechecker.number") + " " + this.pokemon.getSpecies().getNationalPokedexInteger(), -30, -14, hexColor);
//          this.func_73732_a(this.mc.field_71466_p, String.valueOf(this.pokemon.getOriginalTrainer()), 8, 126, hexColor);
//          this.func_73731_b(this.mc.field_71466_p, I18n.func_74838_a("gui.screenpokechecker.ot"), -32, 112, hexColor);
//          if (this.pokemon.getSpecies().getNationalPokedexInteger() != 480 && this.pokemon.getSpecies().getNationalPokedexInteger() != 481 && this.pokemon.getSpecies().getNationalPokedexInteger() != 482) {
//             if (this.pokemon.getSpecies().getNationalPokedexInteger() == 151) {
//                ivSum = ((MewStats)this.pokemon.getExtraStats()).numCloned;
//                this.func_73731_b(this.mc.field_71466_p, I18n.func_74838_a(ChatFormatting.YELLOW + "Number of Clones"), 60, -12, hexColor);
//                strAP = String.valueOf(ivSum);
//                this.func_73731_b(this.mc.field_71466_p, strAP, 200 - strAP.length() * 3, -12, hexColor);
//                hexColor = this.hexWhite;
//             } else if (this.pokemon.isEgg() && this.pokemon.getPosition().order <= 5 && !this.pokemon.isInRanch()) {
//                storage = ClientStorageManager.party;
//                nbt = storage.get(this.pokemon.getPosition().order).getPersistentData();
//                steps = (ClientStorageManager.party.get(this.position).getEggCycles() + 1) * PixelmonConfig.stepsPerEggCycle - ClientStorageManager.party.get(this.position).getEggSteps();
//                this.func_73731_b(this.mc.field_71466_p, I18n.func_74838_a(ChatFormatting.YELLOW + "Eggsteps needed"), 60, -12, hexColor);
//                strHP = String.valueOf(steps);
//                this.func_73731_b(this.mc.field_71466_p, strHP, 200 - strHP.length() * 3, -12, hexColor);
//                hexColor = this.hexWhite;
//             } else {
//                EnumEggGroup[] eggGroups = this.pokemon.getBaseStats().eggGroups;
//                StringBuilder msg = new StringBuilder();
//                EnumEggGroup[] var32 = eggGroups;
//                atk = eggGroups.length;

//                for(def = 0; def < atk; ++def) {
//                   EnumEggGroup egg = var32[def];
//                   msg.append(TextFormatting.DARK_AQUA).append(egg.name()).append(TextFormatting.GOLD).append(", ");
//                }

//                ArrayList<WorldTime> times = new ArrayList();
//                UnmodifiableIterator var37 = SetLoader.getAllSets().iterator();

//                while(true) {
//                   if (!var37.hasNext()) {
//                      allBiomes = new ArrayList();
//                      Iterator var43 = GameRegistry.findRegistry(Biome.class).iterator();

//                      while(var43.hasNext()) {
//                         Biome biome2 = (Biome)var43.next();
//                         allBiomes.add(biome2);
//                      }

//                      var35 = SetLoader.getAllSets().iterator();

//                      while(var35.hasNext()) {
//                         set = var35.next();
//                         if (allBiomes.isEmpty()) {
//                            break;
//                         }

//                         var41 = ((SpawnSet)set).spawnInfos.iterator();

//                         while(var41.hasNext()) {
//                            info = (SpawnInfo)var41.next();
//                            if (info instanceof SpawnInfoPokemon && ((SpawnInfoPokemon)info).getPokemonSpec().name.equalsIgnoreCase(this.pokemon.getSpecies().name)) {
//                               if (((SpawnInfoPokemon)info).getPokemonSpec().form != null) {
//                                  if (this.pokemon.getFormEnum().getForm() == ((SpawnInfoPokemon)info).getPokemonSpec().form) {
//                                     if (info.condition != null && info.condition.biomes != null && !info.condition.biomes.isEmpty()) {
//                                        allBiomes.removeIf((biome) -> {
//                                           return !info.condition.biomes.contains(biome);
//                                        });
//                                     }

//                                     if (info.anticondition != null && info.anticondition.biomes != null && !info.anticondition.biomes.isEmpty()) {
//                                        allBiomes.removeIf((biome) -> {
//                                           return info.anticondition.biomes.contains(biome);
//                                        });
//                                     }

//                                     if (info.compositeCondition != null) {
//                                        if (info.compositeCondition.conditions != null) {
//                                           info.compositeCondition.conditions.forEach((condition) -> {
//                                              if (condition.biomes != null && !condition.biomes.isEmpty()) {
//                                                 allBiomes.removeIf((biome) -> {
//                                                    return !condition.biomes.contains(biome);
//                                                 });
//                                              }

//                                           });
//                                        }

//                                        if (info.compositeCondition.anticonditions != null) {
//                                           info.compositeCondition.anticonditions.forEach((anticondition) -> {
//                                              if (anticondition.biomes != null && anticondition.biomes.isEmpty()) {
//                                                 allBiomes.removeIf((biome) -> {
//                                                    return anticondition.biomes.contains(biome);
//                                                 });
//                                              }

//                                           });
//                                        }
//                                     }
//                                  }
//                               } else {
//                                  if (info.condition != null && info.condition.biomes != null && !info.condition.biomes.isEmpty()) {
//                                     allBiomes.removeIf((biome) -> {
//                                        return !info.condition.biomes.contains(biome);
//                                     });
//                                  }

//                                  if (info.anticondition != null && info.anticondition.biomes != null && !info.anticondition.biomes.isEmpty()) {
//                                     allBiomes.removeIf((biome) -> {
//                                        return info.anticondition.biomes.contains(biome);
//                                     });
//                                  }

//                                  if (info.compositeCondition != null) {
//                                     if (info.compositeCondition.conditions != null) {
//                                        info.compositeCondition.conditions.forEach((condition) -> {
//                                           if (condition.biomes != null && !condition.biomes.isEmpty()) {
//                                              allBiomes.removeIf((biome) -> {
//                                                 return !condition.biomes.contains(biome);
//                                              });
//                                           }

//                                        });
//                                     }

//                                     if (info.compositeCondition.anticonditions != null) {
//                                        info.compositeCondition.anticonditions.forEach((anticondition) -> {
//                                           if (anticondition.biomes != null && anticondition.biomes.isEmpty()) {
//                                              allBiomes.removeIf((biome) -> {
//                                                 return anticondition.biomes.contains(biome);
//                                              });
//                                           }

//                                        });
//                                     }
//                                  }
//                               }
//                            }
//                         }
//                      }

//                      allBiomes = new ArrayList();
//                      var39 = allBiomes.iterator();

//                      while(var39.hasNext()) {
//                         biome3 = (Biome)var39.next();
//                         allBiomes.add(biome3.func_185359_l());
//                      }

//                      StringBuilder message = new StringBuilder(TextFormatting.RED + "None.");
//                      if (!allBiomes.isEmpty()) {
//                         Collections.sort(allBiomes);
//                         message = new StringBuilder(TextFormatting.DARK_AQUA + (String)allBiomes.get(0));

//                         for(i = 1; i < allBiomes.size(); ++i) {
//                            message.append(TextFormatting.GOLD + ", " + TextFormatting.DARK_AQUA).append((String)allBiomes.get(i));
//                         }
//                      }

//                      this.func_73731_b(this.mc.field_71466_p, I18n.func_74838_a(ChatFormatting.YELLOW + "Held Item"), 60, 9, hexColor);
//                      String strAP = String.valueOf(this.pokemon.getHeldItem().func_82833_r());
//                      this.func_73731_b(this.mc.field_71466_p, strAP, 175 - strAP.length() * 3, 9, hexColor);
//                      this.func_73731_b(this.mc.field_71466_p, I18n.func_74838_a(ChatFormatting.YELLOW + "EggGroup"), 60, 30, hexColor);
//                      strAP = msg.substring(0, msg.length() - 2);
//                      this.func_73731_b(this.mc.field_71466_p, strAP, 200 - strAP.length() * 3, 30, hexColor);
//                      this.func_73731_b(this.mc.field_71466_p, I18n.func_74838_a(ChatFormatting.YELLOW + "Spawntime"), 60, 51, hexColor);
//                      StringBuilder msg2 = new StringBuilder();
//                      if (times != null) {
//                         Iterator var61 = times.iterator();

//                         while(var61.hasNext()) {
//                            WorldTime time = (WorldTime)var61.next();
//                            msg2.append(TextFormatting.DARK_AQUA).append(time.name()).append(TextFormatting.GOLD).append(", ");
//                         }
//                      } else {
//                         msg2.append(ChatFormatting.DARK_AQUA + "-  ");
//                      }

//                      String strXP = msg2.substring(0, msg2.length() - 2);
//                      this.func_73731_b(this.mc.field_71466_p, strXP, 200 - strXP.length() * 3, 51, hexColor);
//                      this.func_73731_b(this.mc.field_71466_p, I18n.func_74838_a(ChatFormatting.YELLOW + "Biome"), 60, 72, hexColor);
//                      strXP = message.toString();
//                      this.func_73731_b(this.mc.field_71466_p, strXP, 200 - strXP.length() * 3, 72, hexColor);
//                      float rarity = -1.0F;
//                      UnmodifiableIterator var70 = SetLoader.getAllSets().iterator();

//                      while(var70.hasNext()) {
//                         Object set = var70.next();
//                         Iterator var74 = ((SpawnSet)set).spawnInfos.iterator();

//                         while(var74.hasNext()) {
//                            SpawnInfo info = (SpawnInfo)var74.next();
//                            if (info instanceof SpawnInfoPokemon && ((SpawnInfoPokemon)info).getPokemonSpec().name.equalsIgnoreCase(this.pokemon.getSpecies().name)) {
//                               rarity = info.rarity;
//                            }
//                         }
//                      }

//                      this.func_73731_b(this.mc.field_71466_p, I18n.func_74838_a(ChatFormatting.YELLOW + "Rarity"), 60, 93, hexColor);
//                      String strLP = String.valueOf(rarity);
//                      this.func_73731_b(this.mc.field_71466_p, ChatFormatting.DARK_AQUA + strLP, 200 - strLP.length() * 3, 93, hexColor);
//                      hexColor = this.hexWhite;
//                      break;
//                   }

//                   Object set = var37.next();
//                   var39 = ((SpawnSet)set).spawnInfos.iterator();

//                   while(var39.hasNext()) {
//                      SpawnInfo info = (SpawnInfo)var39.next();
//                      if (info instanceof SpawnInfoPokemon && ((SpawnInfoPokemon)info).getPokemonSpec().name.equalsIgnoreCase(this.pokemon.getSpecies().name)) {
//                         times = info.condition.times;
//                      }
//                   }
//                }
//             }
//          } else {
//             ivSum = ((LakeTrioStats)this.pokemon.getExtraStats()).numEnchanted;
//             strAP = String.valueOf(ivSum);
//             this.func_73731_b(this.mc.field_71466_p, I18n.func_74838_a(ChatFormatting.YELLOW + "Number of Enchantments"), 60, -12, hexColor);
//             this.func_73731_b(this.mc.field_71466_p, strAP, 200 - strAP.length() * 3, -12, hexColor);
//             hexColor = this.hexWhite;
//          }

//          hexColor = this.hexWhite;
//          this.func_73732_a(this.mc.field_71466_p, "Ability", 95, 115, hexColor);
//          this.func_73732_a(this.mc.field_71466_p, I18n.func_74838_a("gui.screenpokechecker.growth"), 8, 137, hexColor);
//          ivSum = 0;
//          var25 = this.pokemon.getIVs().getArray();
//          steps = var25.length;

//          for(atk = 0; atk < steps; ++atk) {
//             def = var25[atk];
//             ivSum += def;
//          }

//          hiddenPower = EnumType.Normal;
//          steps = this.pokemon.getIVs().hp % 2;
//          atk = this.pokemon.getIVs().attack % 2;
//          def = this.pokemon.getIVs().defence % 2;
//          sp = this.pokemon.getIVs().speed % 2;
//          i = this.pokemon.getIVs().specialAttack % 2;
//          i = this.pokemon.getIVs().specialDefence % 2;
//          top = (double)(32 * i + 16 * i + 8 * sp + 4 * def + 2 * atk + steps);
//          typeHidden = (int)Math.floor(top * 15.0D / 63.0D);
//          switch(typeHidden) {
//          case 0:
//             hiddenPower = EnumType.Fighting;
//             break;
//          case 1:
//             hiddenPower = EnumType.Flying;
//             break;
//          case 2:
//             hiddenPower = EnumType.Poison;
//             break;
//          case 3:
//             hiddenPower = EnumType.Ground;
//             break;
//          case 4:
//             hiddenPower = EnumType.Rock;
//             break;
//          case 5:
//             hiddenPower = EnumType.Bug;
//             break;
//          case 6:
//             hiddenPower = EnumType.Ghost;
//             break;
//          case 7:
//             hiddenPower = EnumType.Steel;
//             break;
//          case 8:
//             hiddenPower = EnumType.Fire;
//             break;
//          case 9:
//             hiddenPower = EnumType.Water;
//             break;
//          case 10:
//             hiddenPower = EnumType.Grass;
//             break;
//          case 11:
//             hiddenPower = EnumType.Electric;
//             break;
//          case 12:
//             hiddenPower = EnumType.Psychic;
//             break;
//          case 13:
//             hiddenPower = EnumType.Ice;
//             break;
//          case 14:
//             hiddenPower = EnumType.Dragon;
//             break;
//          case 15:
//             hiddenPower = EnumType.Dark;
//          }

//          this.func_73732_a(this.mc.field_71466_p, ClientStorageManager.party.get(this.position).getAbility().getLocalizedName(), 95, 130, hexColor);
//          this.func_73732_a(this.mc.field_71466_p, this.pokemon.getGrowth().getLocalizedName(), 8, 150, -1);
//          this.func_73731_b(this.mc.field_71466_p, I18n.func_74838_a("gui.screenpokechecker.stats"), 145, 166, hexColor);
//          isEgg = this.pokemon.isEgg();
//          isEgg = false;
//          this.drawBasePokemonInfo();
//       }

//    }
// }
