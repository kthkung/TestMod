//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.api;

import com.google.common.collect.HashBiMap;
import java.util.ArrayList;
import java.util.Collection;

public class MwAPI {
   private static HashBiMap<String, IMwDataProvider> dataProviders = HashBiMap.create();
   private static IMwDataProvider currentProvider = null;
   private static ArrayList<String> providerKeys = new ArrayList();

   public MwAPI() {
   }

   public static void registerDataProvider(String name, IMwDataProvider handler) {
      dataProviders.put(name, handler);
      providerKeys.add(name);
   }

   public static Collection<IMwDataProvider> getDataProviders() {
      return dataProviders.values();
   }

   public static IMwDataProvider getDataProvider(String name) {
      return (IMwDataProvider)dataProviders.get(name);
   }

   public static String getProviderName(IMwDataProvider provider) {
      return (String)dataProviders.inverse().get(provider);
   }

   public static IMwDataProvider getCurrentDataProvider() {
      return currentProvider;
   }

   public static String getCurrentProviderName() {
      return currentProvider != null ? getProviderName(currentProvider) : "None";
   }

   public static IMwDataProvider setCurrentDataProvider(String name) {
      currentProvider = (IMwDataProvider)dataProviders.get(name);
      return currentProvider;
   }

   public static IMwDataProvider setCurrentDataProvider(IMwDataProvider provider) {
      currentProvider = provider;
      return currentProvider;
   }

   public static IMwDataProvider setNextProvider() {
      if (currentProvider != null) {
         int index = providerKeys.indexOf(getCurrentProviderName());
         if (index + 1 >= providerKeys.size()) {
            currentProvider = null;
         } else {
            String nextKey = (String)providerKeys.get(index + 1);
            currentProvider = getDataProvider(nextKey);
         }
      } else if (providerKeys.size() > 0) {
         currentProvider = getDataProvider((String)providerKeys.get(0));
      }

      return currentProvider;
   }

   public static IMwDataProvider setPrevProvider() {
      if (currentProvider != null) {
         int index = providerKeys.indexOf(getCurrentProviderName());
         if (index - 1 < 0) {
            currentProvider = null;
         } else {
            String prevKey = (String)providerKeys.get(index - 1);
            currentProvider = getDataProvider(prevKey);
         }
      } else if (providerKeys.size() > 0) {
         currentProvider = getDataProvider((String)providerKeys.get(providerKeys.size() - 1));
      }

      return currentProvider;
   }
}
