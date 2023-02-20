//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap;

import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.biome.Biome;
import journeymap.region.BlockColours;
import journeymap.util.Logging;
import journeymap.util.Render;
import journeymap.util.Texture;

public class BlockColourGen {
   public BlockColourGen() {
   }

   private static int getIconMapColour(TextureAtlasSprite icon, Texture terrainTexture) {
      int iconX = Math.round((terrainTexture.w) * Math.min(icon.getMinU(), icon.getMaxU()));
      int iconY = Math.round((terrainTexture.h) * Math.min(icon.getMinV(), icon.getMaxV()));
      int iconWidth = Math.round((terrainTexture.w) * Math.abs(icon.getMaxU() - icon.getMinU()));
      int iconHeight = Math.round((terrainTexture.h) * Math.abs(icon.getMaxV() - icon.getMinV()));
      int[] pixels = new int[iconWidth * iconHeight];
      terrainTexture.getRGB(iconX, iconY, iconWidth, iconHeight, pixels, 0, iconWidth, icon);
      return Render.getAverageColourOfArray(pixels);
   }

   private static void genBiomeColours(BlockColours bc) {
      Iterator var1 = Biome.REGISTRY.iterator();

      while(var1.hasNext()) {
         Biome biome = (Biome)var1.next();
         if (biome != null) {
            double temp = (double)MathHelper.clamp(biome.getDefaultTemperature(), 0.0F, 1.0F);
            double rain = (double)MathHelper.clamp(biome.getRainfall(), 0.0F, 1.0F);
            int grasscolor = ColorizerGrass.getGrassColor(temp, rain);
            int foliagecolor = ColorizerFoliage.getFoliageColor(temp, rain);
            int watercolor = biome.getWaterColorMultiplier();
            bc.setBiomeData(biome.getBiomeName(), watercolor & 16777215, grasscolor & 16777215, foliagecolor & 16777215);
         }
      }

   }

   public static void genBlockColours(BlockColours bc) {
      Logging.log("generating block map colours from textures", new Object[0]);
      int terrainTextureId = Minecraft.getMinecraft().renderEngine.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).getGlTextureId();
      if (terrainTextureId == 0) {
         Logging.log("error: could get terrain texture ID", new Object[0]);
      } else {
         Texture terrainTexture = new Texture(terrainTextureId);
         double u1Last = 0.0;
         double u2Last = 0.0;
         double v1Last = 0.0;
         double v2Last = 0.0;
         int blockColourLast = 0;
         int e_count = 0;
         int b_count = 0;
         int s_count = 0;
         Iterator var15 = Block.REGISTRY.iterator();

         while(var15.hasNext()) {
            Object oblock = var15.next();
            Block block = (Block)oblock;
            int blockID = Block.getIdFromBlock(block);

            for(int dv = 0; dv < 16; ++dv) {
               int blockColour = 0;
               if (block != null && block.getRenderType(block.getDefaultState()) != EnumBlockRenderType.INVISIBLE) {
                  TextureAtlasSprite icon = null;

                  try {
                     icon = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(block.getStateFromMeta(dv));
                  } catch (Exception var30) {
                     ++e_count;
                  }

                  if (icon != null) {
                     double u1 = (double)icon.getMinU();
                     double u2 = (double)icon.getMaxU();
                     double v1 = (double)icon.getMinV();
                     double v2 = (double)icon.getMaxV();
                     if (u1 == u1Last && u2 == u2Last && v1 == v1Last && v2 == v2Last) {
                        blockColour = blockColourLast;
                        ++s_count;
                     } else {
                        blockColour = getIconMapColour(icon, terrainTexture);
                        if (((ResourceLocation)Block.REGISTRY.getNameForObject(block)).getNamespace().contains("CarpentersBlocks")) {
                        }

                        u1Last = u1;
                        u2Last = u2;
                        v1Last = v1;
                        v2Last = v2;
                        blockColourLast = blockColour;
                        ++b_count;
                     }
                  }
               }

               bc.setColour(block.delegate.name().toString(), String.valueOf(dv), blockColour);
            }
         }

         Logging.log("processed %d block textures, %d skipped, %d exceptions", new Object[]{b_count, s_count, e_count});
         genBiomeColours(bc);
      }
   }
}
