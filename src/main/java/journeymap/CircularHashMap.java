//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CircularHashMap<K, V> {
   private Map<K, CircularHashMap<K, V>.Node> nodeMap = new HashMap();
   private CircularHashMap<K, V>.Node headNode = null;
   private CircularHashMap<K, V>.Node currentNode = null;

   public CircularHashMap() {
   }

   public V put(K key, V value) {
      CircularHashMap<K, V>.Node node = (Node)this.nodeMap.get(key);
      if (node == null) {
         node = new Node(key, value);
         this.nodeMap.put(key, node);
         if (this.headNode == null) {
            node.next = node;
            node.prev = node;
         } else {
            node.next = this.headNode.next;
            node.prev = this.headNode;
            this.headNode.next.prev = node;
            this.headNode.next = node;
         }

         if (this.currentNode == null) {
            this.currentNode = node;
         }

         this.headNode = node;
      } else {
         node.value = value;
      }

      return value;
   }

   public V remove(Object key) {
      CircularHashMap<K, V>.Node node = (Node)this.nodeMap.get(key);
      V value = null;
      if (node != null) {
         if (this.headNode == node) {
            this.headNode = node.next;
            if (this.headNode == node) {
               this.headNode = null;
            }
         }

         if (this.currentNode == node) {
            this.currentNode = node.next;
            if (this.currentNode == node) {
               this.currentNode = null;
            }
         }

         node.prev.next = node.next;
         node.next.prev = node.prev;
         node.next = null;
         node.prev = null;
         value = node.value;
         this.nodeMap.remove(key);
      }

      return value;
   }

   public void clear() {
      Iterator var1 = this.nodeMap.values().iterator();

      while(var1.hasNext()) {
         CircularHashMap<K, V>.Node node = (Node)var1.next();
         node.next = null;
         node.prev = null;
      }

      this.nodeMap.clear();
      this.headNode = null;
      this.currentNode = null;
   }

   public boolean containsKey(Object key) {
      return this.nodeMap.containsKey(key);
   }

   public int size() {
      return this.nodeMap.size();
   }

   public Set<K> keySet() {
      return this.nodeMap.keySet();
   }

   public Collection<V> values() {
      Collection<V> list = new ArrayList();
      Iterator var2 = this.nodeMap.values().iterator();

      while(var2.hasNext()) {
         CircularHashMap<K, V>.Node node = (Node)var2.next();
         list.add(node.value);
      }

      return list;
   }

   public Collection<Map.Entry<K, V>> entrySet() {
      return new ArrayList(this.nodeMap.values());
   }

   public V get(Object key) {
      CircularHashMap<K, V>.Node node = (Node)this.nodeMap.get(key);
      return node != null ? node.value : null;
   }

   public boolean isEmpty() {
      return this.nodeMap.isEmpty();
   }

   public Map.Entry<K, V> getNextEntry() {
      if (this.currentNode != null) {
         this.currentNode = this.currentNode.next;
      }

      return this.currentNode;
   }

   public Map.Entry<K, V> getPrevEntry() {
      if (this.currentNode != null) {
         this.currentNode = this.currentNode.prev;
      }

      return this.currentNode;
   }

   public void rewind() {
      this.currentNode = this.headNode != null ? this.headNode.next : null;
   }

   public boolean setPosition(K key) {
      CircularHashMap<K, V>.Node node = (Node)this.nodeMap.get(key);
      if (node != null) {
         this.currentNode = node;
      }

      return node != null;
   }

   public class Node implements Map.Entry<K, V> {
      private final K key;
      private V value;
      private CircularHashMap<K, V>.Node next;
      private CircularHashMap<K, V>.Node prev;

      Node(K key, V value) {
         this.key = key;
         this.value = value;
         this.next = this;
         this.prev = this;
      }

      public K getKey() {
         return this.key;
      }

      public V getValue() {
         return this.value;
      }

      public V setValue(V value) {
         V oldValue = this.value;
         this.value = value;
         return oldValue;
      }
   }
}
