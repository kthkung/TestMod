//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.region;

import journeymap.util.Logging;
import journeymap.util.Render;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.io.*;
import java.util.*;

public class BlockColours {
   public static final int MAX_META = 16;
   public static final String biomeSectionString = "[biomes]";
   public static final String blockSectionString = "[blocks]";
   private LinkedHashMap<String, BiomeData> biomeMap = new LinkedHashMap();
   private LinkedHashMap<String, BlockData> bcMap = new LinkedHashMap();

   public BlockColours() {
   }

   public String CombineBlockMeta(String BlockName, int meta) {
      return BlockName + " " + meta;
   }

   public String CombineBlockMeta(String BlockName, String meta) {
      return BlockName + " " + meta;
   }

   public int getColour(String BlockName, int meta) {
      String BlockAndMeta = this.CombineBlockMeta(BlockName, meta);
      String BlockAndWildcard = this.CombineBlockMeta(BlockName, "*");
      BlockData data = new BlockData();
      if (this.bcMap.containsKey(BlockAndMeta)) {
         data = (BlockData)this.bcMap.get(BlockAndMeta);
      } else if (this.bcMap.containsKey(BlockAndWildcard)) {
         data = (BlockData)this.bcMap.get(BlockAndWildcard);
      }

      return data.color;
   }

   public int getColour(IBlockState BlockState) {
      Block block = BlockState.getBlock();
      int meta = block.getMetaFromState(BlockState);
      if (block.delegate == null) {
         Logging.logError("Delegate was Null when getting colour, Block in: %s", new Object[]{block.toString()});
         return 0;
      } else if (block.delegate.name() == null) {
         Logging.logError("Block Name was Null when getting colour, Block in: %s, Delegate: %s", new Object[]{block.toString(), block.delegate.toString()});
         return 0;
      } else {
         return this.getColour(block.delegate.name().toString(), meta);
      }
   }

   public void setColour(String BlockName, String meta, int colour) {
      String BlockAndMeta = this.CombineBlockMeta(BlockName, meta);
      if (meta.equals("*")) {
         for(int i = 0; i < 16; ++i) {
            this.setColour(BlockName, String.valueOf(i), colour);
         }
      }

      BlockData data;
      if (this.bcMap.containsKey(BlockAndMeta)) {
         data = (BlockData)this.bcMap.get(BlockAndMeta);
         data.color = colour;
      } else {
         data = new BlockData();
         data.color = colour;
         this.bcMap.put(BlockAndMeta, data);
      }

   }

   private int getGrassColourMultiplier(String biomeName) {
      if (!biomeName.equals("Plains")) {
         boolean var2 = false;
      }

      BiomeData data = (BiomeData)this.biomeMap.get(biomeName);
      return data != null ? data.grassMultiplier : 16777215;
   }

   private int getWaterColourMultiplier(String biomeName) {
      BiomeData data = (BiomeData)this.biomeMap.get(biomeName);
      return data != null ? data.waterMultiplier : 16777215;
   }

   private int getFoliageColourMultiplier(String biomeName) {
      BiomeData data = (BiomeData)this.biomeMap.get(biomeName);
      return data != null ? data.foliageMultiplier : 16777215;
   }

   public int getBiomeColour(String BlockName, int meta, String biomeName) {
      int colourMultiplier = 16777215;
      if (this.bcMap.containsKey(this.CombineBlockMeta(BlockName, meta))) {
         switch (((BlockData)this.bcMap.get(this.CombineBlockMeta(BlockName, meta))).type) {
            case GRASS:
               colourMultiplier = this.getGrassColourMultiplier(biomeName);
               break;
            case LEAVES:
            case FOLIAGE:
               colourMultiplier = this.getFoliageColourMultiplier(biomeName);
               break;
            case WATER:
               colourMultiplier = this.getWaterColourMultiplier(biomeName);
               break;
            default:
               colourMultiplier = 16777215;
         }
      }

      return colourMultiplier;
   }

   public int getBiomeColour(IBlockState BlockState, int biomeId) {
      String biomeName = "";
      Biome biome = Biome.getBiomeForId(biomeId);
      if (biomeId == 255) {
         biome = Biomes.PLAINS;
      }

      if (biome != null) {
         biomeName = biome.getBiomeName();
      }

      Block block = BlockState.getBlock();
      int meta = block.getMetaFromState(BlockState);
      return this.getBiomeColour(block.delegate.name().toString(), meta, biomeName);
   }

   public void setBiomeData(String biomeName, int waterShading, int grassShading, int foliageShading) {
      BiomeData data = new BiomeData();
      data.foliageMultiplier = foliageShading;
      data.grassMultiplier = grassShading;
      data.waterMultiplier = waterShading;
      this.biomeMap.put(biomeName, data);
   }

   private static BlockType getBlockTypeFromString(String typeString) {
      BlockType blockType = BlockColours.BlockType.NORMAL;
      if (typeString.equalsIgnoreCase("normal")) {
         blockType = BlockColours.BlockType.NORMAL;
      } else if (typeString.equalsIgnoreCase("grass")) {
         blockType = BlockColours.BlockType.GRASS;
      } else if (typeString.equalsIgnoreCase("leaves")) {
         blockType = BlockColours.BlockType.LEAVES;
      } else if (typeString.equalsIgnoreCase("foliage")) {
         blockType = BlockColours.BlockType.FOLIAGE;
      } else if (typeString.equalsIgnoreCase("water")) {
         blockType = BlockColours.BlockType.WATER;
      } else if (typeString.equalsIgnoreCase("opaque")) {
         blockType = BlockColours.BlockType.OPAQUE;
      } else {
         Logging.logWarning("unknown block type '%s'", new Object[]{typeString});
      }

      return blockType;
   }

   private static String getBlockTypeAsString(BlockType blockType) {
      String s = "normal";
      switch (blockType) {
         case GRASS:
            s = "grass";
            break;
         case LEAVES:
            s = "leaves";
            break;
         case FOLIAGE:
            s = "foliage";
            break;
         case WATER:
            s = "water";
            break;
         case NORMAL:
            s = "normal";
            break;
         case OPAQUE:
            s = "opaque";
      }

      return s;
   }

   public BlockType getBlockType(String BlockName, int meta) {
      String BlockAndMeta = this.CombineBlockMeta(BlockName, meta);
      String BlockAndWildcard = this.CombineBlockMeta(BlockName, "*");
      BlockData data = new BlockData();
      if (this.bcMap.containsKey(BlockAndMeta)) {
         data = (BlockData)this.bcMap.get(BlockAndMeta);
      } else if (this.bcMap.containsKey(BlockAndWildcard)) {
         data = (BlockData)this.bcMap.get(BlockAndWildcard);
      }

      return data.type;
   }

   public BlockType getBlockType(int BlockAndMeta) {
      Block block = Block.getBlockById(BlockAndMeta >> 4);
      int meta = BlockAndMeta & 15;
      return this.getBlockType(block.delegate.name().toString(), meta);
   }

   public void setBlockType(String BlockName, String meta, BlockType type) {
      String BlockAndMeta = this.CombineBlockMeta(BlockName, meta);
      if (!meta.equals("*")) {
         BlockData data;
         if (this.bcMap.containsKey(BlockAndMeta)) {
            data = (BlockData)this.bcMap.get(BlockAndMeta);
            data.type = type;
            data.color = adjustBlockColourFromType(BlockName, meta, type, data.color);
         } else {
            data = new BlockData();
            data.type = type;
            this.bcMap.put(BlockAndMeta, data);
         }

      } else {
         for(int i = 0; i < 16; ++i) {
            this.setBlockType(BlockName, String.valueOf(i), type);
         }

      }
   }

   private static int adjustBlockColourFromType(String BlockName, String meta, BlockType type, int blockColour) {
      Block block = Block.getBlockFromName(BlockName);
      switch (type) {
         case GRASS:
            blockColour = -6579301;
            break;
         case LEAVES:
            blockColour |= -16777216;
         case FOLIAGE:
         case WATER:
         default:
            break;
         case OPAQUE:
            blockColour |= -16777216;
         case NORMAL:
            try {
               World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);
               int renderColour = block.getBlockState().getBaseState().getMaterial().getMaterialMapColor().colorValue;
               if (renderColour != 16777215) {
                  blockColour = Render.multiplyColours(blockColour, -16777216 | renderColour);
               }
            } catch (RuntimeException var7) {
            }
      }

      return blockColour;
   }

   public static int getColourFromString(String s) {
      return (int)(Long.parseLong(s, 16) & 4294967295L);
   }

   private void loadBiomeLine(String[] split) {
      try {
         int waterMultiplier = getColourFromString(split[2]) & 16777215;
         int grassMultiplier = getColourFromString(split[3]) & 16777215;
         int foliageMultiplier = getColourFromString(split[4]) & 16777215;
         this.setBiomeData(split[1], waterMultiplier, grassMultiplier, foliageMultiplier);
      } catch (NumberFormatException var5) {
         Logging.logWarning("invalid biome colour line '%s %s %s %s %s'", new Object[]{split[0], split[1], split[2], split[3], split[4]});
      }

   }

   private void loadBlockLine(String[] split) {
      try {
         int colour = getColourFromString(split[3]);
         this.setColour(split[1], split[2], colour);
      } catch (NumberFormatException var3) {
         Logging.logWarning("invalid block colour line '%s %s %s %s'", new Object[]{split[0], split[1], split[2], split[3]});
      }

   }

   private void loadBlockTypeLine(String[] split) {
      try {
         BlockType type = getBlockTypeFromString(split[3]);
         this.setBlockType(split[1], split[2], type);
      } catch (NumberFormatException var3) {
         Logging.logWarning("invalid block colour line '%s %s %s %s'", new Object[]{split[0], split[1], split[2], split[3]});
      }

   }

   public void loadFromFile(File f) {
      Scanner fin = null;

      try {
         fin = new Scanner(new FileReader(f));

         while(true) {
            while(true) {
               String line;
               do {
                  if (!fin.hasNextLine()) {
                     return;
                  }

                  line = fin.nextLine().split("#")[0].trim();
               } while(line.length() <= 0);

               String[] lineSplit = line.split(" ");
               if (lineSplit[0].equals("biome") && lineSplit.length == 5) {
                  this.loadBiomeLine(lineSplit);
               } else if (lineSplit[0].equals("block") && lineSplit.length == 4) {
                  this.loadBlockLine(lineSplit);
               } else if (lineSplit[0].equals("blocktype") && lineSplit.length == 4) {
                  this.loadBlockTypeLine(lineSplit);
               } else if (!lineSplit[0].equals("version:")) {
                  Logging.logWarning("invalid map colour line '%s'", new Object[]{line});
               }
            }
         }
      } catch (IOException var8) {
         Logging.logError("loading block colours: no such file '%s'", new Object[]{f});
      } finally {
         if (fin != null) {
            fin.close();
         }

      }

   }

   public void saveBiomes(Writer fout) throws IOException {
      fout.write("biome * ffffff ffffff ffffff\n");
      Iterator var2 = this.biomeMap.entrySet().iterator();

      while(true) {
         String biomeName;
         BiomeData data;
         do {
            if (!var2.hasNext()) {
               return;
            }

            Map.Entry<String, BiomeData> entry = (Map.Entry)var2.next();
            biomeName = (String)entry.getKey();
            data = (BiomeData)entry.getValue();
         } while(data.waterMultiplier == 16777215 && data.grassMultiplier == 16777215 && data.foliageMultiplier == 16777215);

         fout.write(String.format("biome %s %06x %06x %06x\n", biomeName, data.waterMultiplier, data.grassMultiplier, data.foliageMultiplier));
      }
   }

   private static String getMostOccurringKey(Map<String, Integer> map, String defaultItem) {
      int maxCount = 1;
      String mostOccurringKey = defaultItem;
      Iterator var4 = map.entrySet().iterator();

      while(var4.hasNext()) {
         Map.Entry<String, Integer> entry = (Map.Entry)var4.next();
         String key = (String)entry.getKey();
         int count = (Integer)entry.getValue();
         if (count > maxCount) {
            maxCount = count;
            mostOccurringKey = key;
         }
      }

      return mostOccurringKey;
   }

   private static void writeMinimalBlockLines(Writer fout, String lineStart, List<String> items, String defaultItem) throws IOException {
      Map<String, Integer> frequencyMap = new HashMap();

      String item;
      int count;
      for(Iterator var5 = items.iterator(); var5.hasNext(); frequencyMap.put(item, count + 1)) {
         item = (String)var5.next();
         count = 0;
         if (frequencyMap.containsKey(item)) {
            count = (Integer)frequencyMap.get(item);
         }
      }

      String mostOccurringItem = getMostOccurringKey(frequencyMap, defaultItem);
      if (!mostOccurringItem.equals(defaultItem)) {
         fout.write(String.format("%s * %s\n", lineStart, mostOccurringItem));
      }

      int meta = 0;

      for(Iterator var11 = items.iterator(); var11.hasNext(); ++meta) {
         String s = (String)var11.next();
         if (!s.equals(mostOccurringItem) && !s.equals(defaultItem)) {
            fout.write(String.format("%s %d %s\n", lineStart, meta, s));
         }
      }

   }

   public void saveBlocks(Writer fout) throws IOException {
      fout.write("block * * 00000000\n");
      String LastBlock = "";
      List<String> colours = new ArrayList();

      String block;
      for(Iterator var4 = this.bcMap.entrySet().iterator(); var4.hasNext(); LastBlock = block) {
         Map.Entry<String, BlockData> entry = (Map.Entry)var4.next();
         String[] BlockAndMeta = ((String)entry.getKey()).split(" ");
         block = BlockAndMeta[0];
         String color = String.format("%08x", ((BlockData)entry.getValue()).color);
         if (!LastBlock.equals(block) && !LastBlock.isEmpty()) {
            String lineStart = String.format("block %s", LastBlock);
            writeMinimalBlockLines(fout, lineStart, colours, "00000000");
            colours.clear();
         }

         colours.add(color);
      }

   }

   public void saveBlockTypes(Writer fout) throws IOException {
      fout.write("blocktype * * normal\n");
      String LastBlock = "";
      List<String> blockTypes = new ArrayList();

      String block;
      for(Iterator var4 = this.bcMap.entrySet().iterator(); var4.hasNext(); LastBlock = block) {
         Map.Entry<String, BlockData> entry = (Map.Entry)var4.next();
         String[] BlockAndMeta = ((String)entry.getKey()).split(" ");
         block = BlockAndMeta[0];
         String Type = getBlockTypeAsString(((BlockData)entry.getValue()).type);
         if (!LastBlock.equals(block) && !LastBlock.isEmpty()) {
            String lineStart = String.format("blocktype %s", LastBlock);
            writeMinimalBlockLines(fout, lineStart, blockTypes, getBlockTypeAsString(BlockColours.BlockType.NORMAL));
            blockTypes.clear();
         }

         blockTypes.add(Type);
      }

   }

   public void saveToFile(File f) {
      Writer fout = null;

      try {
         fout = new OutputStreamWriter(new FileOutputStream(f));
         fout.write(String.format("version: %s\n", "5.1"));
         this.saveBiomes(fout);
         this.saveBlockTypes(fout);
         this.saveBlocks(fout);
      } catch (IOException var12) {
         Logging.logError("saving block colours: could not write to '%s'", new Object[]{f});
      } finally {
         if (fout != null) {
            try {
               fout.close();
            } catch (IOException var11) {
            }
         }

      }

   }

   public static void writeOverridesFile(File f) {
      Writer fout = null;

      try {
         fout = new OutputStreamWriter(new FileOutputStream(f));
         fout.write(String.format("version: %s\n", "5.1"));
         fout.write("block minecraft:yellow_flower * 60ffff00\t# make dandelions more yellow\nblock minecraft:red_flower 0 60ff0000\t\t# make poppy more red\nblock minecraft:red_flower 1 601c92d6\t\t# make Blue Orchid more red\nblock minecraft:red_flower 2 60b865fb\t\t# make Allium more red\nblock minecraft:red_flower 3 60e4eaf2\t\t# make Azure Bluet more red\nblock minecraft:red_flower 4 60d33a17\t\t# make Red Tulip more red\nblock minecraft:red_flower 5 60e17124\t\t# make Orange Tulip more red\nblock minecraft:red_flower 6 60ffffff\t\t# make White Tulip more red\nblock minecraft:red_flower 7 60eabeea\t\t# make Pink Tulip more red\nblock minecraft:red_flower 8 60eae6ad\t\t# make Oxeye Daisy more red\nblock minecraft:double_plant 0 60ffff00\t\t# make Sunflower more Yellow-orrange\nblock minecraft:double_plant 1 d09f78a4\t\t# make Lilac more pink\nblock minecraft:double_plant 4 60ff0000\t\t# make Rose Bush more red\nblock minecraft:double_plant 5 d0e3b8f7\t\t# make Peony more red\nblocktype minecraft:grass * grass\t\t\t# grass block\nblocktype minecraft:flowing_water * water\t# flowing water block\nblocktype minecraft:water * water\t\t\t# still water block\nblocktype minecraft:leaves * leaves    \t\t# leaves block\nblocktype minecraft:leaves2 * leaves    \t\t# leaves block\nblocktype minecraft:leaves 1 opaque    \t\t# pine leaves (not biome colorized)\nblocktype minecraft:leaves 2 opaque    \t\t# birch leaves (not biome colorized)\nblocktype minecraft:tallgrass * grass     \t# tall grass block\nblocktype minecraft:vine * foliage  \t\t\t# vines block\nblocktype BiomesOPlenty:grass * grass\t\t# BOP grass block\nblocktype BiomesOPlenty:plant_0 * grass\t\t# BOP plant block\nblocktype BiomesOPlenty:plant_1 * grass\t\t# BOP plant block\nblocktype BiomesOPlenty:leaves_0 * leaves\t# BOP Leave block\nblocktype BiomesOPlenty:leaves_1 * leaves\t# BOP Leave block\nblocktype BiomesOPlenty:leaves_2 * leaves\t# BOP Leave block\nblocktype BiomesOPlenty:leaves_3 * leaves\t# BOP Leave block\nblocktype BiomesOPlenty:leaves_4 * leaves\t# BOP Leave block\nblocktype BiomesOPlenty:leaves_5 * leaves\t# BOP Leave block\nblocktype BiomesOPlenty:tree_moss * foliage\t# biomes o plenty tree moss\n");
      } catch (IOException var11) {
         Logging.logError("saving block overrides: could not write to '%s'", new Object[]{f});
      } finally {
         if (fout != null) {
            try {
               fout.close();
            } catch (IOException var10) {
            }
         }

      }

   }

   public boolean CheckFileVersion(File fn) {
      String lineData = "";

      try {
         RandomAccessFile inFile = new RandomAccessFile(fn, "rw");
         lineData = inFile.readLine();
         inFile.close();
      } catch (IOException var4) {
         System.err.println(var4.getMessage());
      }

      return lineData.equals(String.format("version: %s", "5.1"));
   }

   public class BiomeData {
      private int waterMultiplier = 0;
      private int grassMultiplier = 0;
      private int foliageMultiplier = 0;

      public BiomeData() {
      }
   }

   public class BlockData {
      public int color = 0;
      public BlockType type;

      public BlockData() {
         this.type = BlockColours.BlockType.NORMAL;
      }
   }

   public static enum BlockType {
      NORMAL,
      GRASS,
      LEAVES,
      FOLIAGE,
      WATER,
      OPAQUE;

      private BlockType() {
      }
   }
}
