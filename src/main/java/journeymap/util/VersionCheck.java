//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.util;

import net.minecraftforge.fml.common.Loader;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class VersionCheck implements Runnable {
   private static boolean isLatestVersion = true;
   private static boolean isPKVersion = false;
   private static String latestVersion = "5";
   private static String updateURL = "";

   public VersionCheck() {
   }

   public void run() {
      InputStream in = null;

      try {
         URL url = new URL("https://goo.gl/T20VFb");
         URLConnection con = url.openConnection();
         con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");
         con.setUseCaches(false);
         in = con.getInputStream();
      } catch (MalformedURLException var7) {
      } catch (IOException var8) {
      }

      try {
         List<String> list = IOUtils.readLines(in);
         int index = -100;

         for(int i = 0; i < list.size(); ++i) {
            if (((String)list.get(i)).contains(Loader.instance().getMCVersionString()) && ((String)list.get(i + 2)).contains("Pixelmon 7.0.8")) {
               index = i;
               break;
            }
         }

         String version = (String)list.get(index + 1);
         version = version.replace("\"modVersion\": \"", "");
         version = version.replace("\",", "");
         version = version.replace(" ", "");
         latestVersion = version;
         String pkVersion = (String)list.get(index + 2);
         pkVersion = pkVersion.replace("\"pkVersion\": \"", "");
         pkVersion = pkVersion.replace("\",", "");
         pkVersion = pkVersion.replace(" ", "");
         pkVersion = pkVersion.replace("Pixelmon", "");
         String updateURL = (String)list.get(index + 4);
         updateURL = updateURL.replace("\"updateURL\": \"", "");
         updateURL = updateURL.replace("\",", "");
         updateURL = updateURL.replace(" ", "");
         VersionCheck.updateURL = updateURL;
         isLatestVersion = "5.1".equals(version);
      } catch (IOException var9) {
      } catch (Exception var10) {
      }

   }

   public static boolean isLatestVersion() {
      return isLatestVersion;
   }

   public static String getLatestVersion() {
      return latestVersion;
   }

   public static String getUpdateURL() {
      return updateURL;
   }
}
