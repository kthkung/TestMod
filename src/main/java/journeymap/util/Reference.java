//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.util;

import com.google.common.collect.Sets;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

public final class Reference {
   public static final String MOD_ID = "pixelradar";
   public static final String MOD_NAME = "PixelRadar";
   public static final String AUTHOR = "RoxoR";
   public static final String VERSION = "5.1";
   public static final String ACCEPTABLE_MC_VERSIONS = "[1.12.2]";
   public static final String MOD_GUIFACTORY_CLASS = "pixelradar.gui.ModGuiFactoryHandler";
   public static final String CLIENT_PROXY_CLASS = "pixelradar.forge.ClientProxy";
   public static final String SERVER_PROXY_CLASS = "pixelradar.forge.CommonProxy";
   public static final String ACCEPTABLE_REMOTE_VERSIONS = "*";
   public static final boolean CLIENT_ONLY = true;
   public static final String DEPENDENCIES = "";
   public static final String BRANDING = "PixelRadar v5.1 by RoxoR";
   public static final String CHAT_BRANDING;
   public static final List<String> LEGENDARIES;
   public static final List<String> DITTOS;
   public static final String VersionURL = "https://goo.gl/T20VFb";
   public static final String ForgeVersionURL = "https://raw.githubusercontent.com/Vectron/Versions/master/ForgeMwVersion.json";
   public static final String catOptions = "mw.configgui.ctgy.general";
   public static final String catLargeMapConfig = "largemap";
   public static final String catSmallMapConfig = "smallmap";
   public static final String catFullMapConfig = "fullscreenmap";
   public static final String catPixelRadarConfig = "pixelradar";
   public static final String catMapPos = "mappos";
   public static final String PlayerTrailName = "player";
   public static final Pattern patternInvalidChars;
   public static final Pattern patternInvalidChars2;
   public static final String catWorld = "world";
   public static final String catMarkers = "markers";
   public static final String worldDirConfigName = "pixelradar.cfg";
   public static final String blockColourSaveFileName = "MapWriterBlockColours.txt";
   public static final String blockColourOverridesFileName = "MapWriterBlockColourOverrides.txt";
   public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";
   public static final ResourceLocation backgroundTexture;
   public static final ResourceLocation roundMapTexture;
   public static final ResourceLocation squareMapTexture;
   public static final ResourceLocation playerArrowTexture;
   public static final ResourceLocation northArrowTexture;
   public static final ResourceLocation leftArrowTexture;
   public static final ResourceLocation rightArrowTexture;
   public static final ResourceLocation DummyMapTexture;
   public static final HashSet<String> PROTOCOLS;

   public Reference() {
   }

   static {
      CHAT_BRANDING = ChatFormatting.BOLD + "" + ChatFormatting.WHITE + "--- " + ChatFormatting.AQUA + "Pixel" + ChatFormatting.WHITE + "Radar ---" + ChatFormatting.RESET;
      LEGENDARIES = Arrays.asList("Articuno", "Zapdos", "Moltres", "Mewtwo", "Mew", "Entei", "Raikou", "Suicune", "Ho-oh", "Ho-Oh", "HoOh", "Lugia", "Celebi", "Regirock", "Regice", "Registeel", "Latios", "Latias", "Groudon", "Kyogre", "Rayquaza", "Jirachi", "Deoxys", "Uxie", "Azelf", "Mesprit", "Dialga", "Palkia", "Giratina", "Cresselia", "Darkrai", "Manaphy", "Phione", "Heatran", "Regigigas", "Shaymin", "Arceus", "Victini", "Cobalion", "Terrakion", "Virizion", "Keldeo", "Thundurus", "Tornadus", "Landorus", "Zekrom", "Reshiram", "Kyurem", "Genesect", "Meloetta", "Xerneas", "Yveltal", "Zygarde", "Diancie", "Hoopa", "Volcanion", "Cosmog", "Cosmoem", "Solgaleo", "Lunala", "Necrozma", "Magearna", "Marshadow", "TapuKoko", "TapuLele", "TapuBulu", "TapuFini", "Nihilego", "Buzzwole", "Pheromosa", "Xurkitree", "Celesteela", "Kartana", "Guzzlord", "Poipole", "Naganadel", "Stakataka", "Blacephalon", "Meltan", "Melmetal", "Zacian", "Zamazenta", "Eternatus", "Kubfu", "Urshifu", "Calyrex", "Zarude", "Regieleki", "Regidrago");
      DITTOS = Arrays.asList("Ditto");
      patternInvalidChars = Pattern.compile("[^\\p{L}\\p{Nd}_]");
      patternInvalidChars2 = Pattern.compile("[^\\p{L}\\p{Nd}_ -]");
      backgroundTexture = new ResourceLocation("mapwriter", "textures/map/background.png");
      roundMapTexture = new ResourceLocation("mapwriter", "textures/map/border_round.png");
      squareMapTexture = new ResourceLocation("mapwriter", "textures/map/border_square.png");
      playerArrowTexture = new ResourceLocation("mapwriter", "textures/map/arrow_player.png");
      northArrowTexture = new ResourceLocation("mapwriter", "textures/map/arrow_north.png");
      leftArrowTexture = new ResourceLocation("mapwriter", "textures/map/arrow_text_left.png");
      rightArrowTexture = new ResourceLocation("mapwriter", "textures/map/arrow_text_right.png");
      DummyMapTexture = new ResourceLocation("mapwriter", "textures/map/dummy_map.png");
      PROTOCOLS = Sets.newHashSet(new String[]{"http", "https"});
   }
}
