// Java program to demonstrate implementation of our
// own hash table with chaining for collision detection
import java.util.ArrayList;
import java.util.Objects;

// A node of chains
class HashNode<K, V> {
  K key;
  V value;
  HashNode<K, V> next; // Reference to next node

  // Constructor
  public HashNode(K key, V value){
    this.key = key;
    this.value = value;
  }
}

// Class to represent entire hash table
public class HashTable<K, V> {
  // bucketArray is used to store array of chains
  private ArrayList<HashNode<K, V>> bucketArray;

  // Current capacity of array list
  private int numBuckets;

  // Current size of array list
  private int size;

  // Constructor (Initializes capacity, size and
  // empty chains.
  public HashTable(){
    bucketArray = new ArrayList<>();
    numBuckets = 10;
    size = 0;

    // Create empty chains
    for (int i = 0; i < numBuckets; i++){
      bucketArray.add(null);
    }
  }

  public int size() {
    return size;
  }
  
  public boolean isEmpty() {
    return size() == 0;
  }

  // This implements hash function to find index
  // for a key
  private int getBucketIndex(K key){
    int index = key.hashCode() % numBuckets;
    // key.hashCode() could be negative.
    index = index < 0 ? index * -1 : index;
    return index;
  }

  // Method to remove a given key
  public V remove(K key){
    // Apply hash function to find index for given key
    int bucketIndex = getBucketIndex(key);

    // Get head of chain
    HashNode<K, V> head = bucketArray.get(bucketIndex);

    // Search for key in its chain
    HashNode<K, V> prev = null;
    while (head != null) {
      // If Key found
      if (head.key.equals(key))
        break;

      // Else keep moving in chain
      prev = head;
      head = head.next;
    }

    // If key was not there
    if (head == null){
      return null;
    }

    // Reduce size
    size--;

    // Remove key
    if (prev != null){
      prev.next = head.next;
    }else{
      bucketArray.set(bucketIndex, head.next);
    }
    return head.value;
  }

  // Returns value for a key
  public V get(K key){
    // Find head of chain for given key
    int bucketIndex = getBucketIndex(key);
  
    HashNode<K, V> head = bucketArray.get(bucketIndex);

    // Search key in chain
    V result = null;
    while (head != null) {
      if (head.key.equals(key)){
        result = head.value;
        break;
      }
      head = head.next;
    }

    return result;
  }

  // Add a key value pair
  public void add(K key, V value){
    // Find head of chain for given key
    int bucketIndex = getBucketIndex(key);
    HashNode<K, V> head = bucketArray.get(bucketIndex);

    // Check if key is already present
    while (head != null){
      if (head.key.equals(key)){
        head.value = value;
        return;
      }
      head = head.next;
    }

    // Insert key in chain
    size++;
    head = bucketArray.get(bucketIndex);
    HashNode<K, V> newNode = 
      new HashNode<K, V>(key, value);
    newNode.next = head;
    bucketArray.set(bucketIndex, newNode);

    // If load factor goes beyond threshold, then
    // double hash table size
    if ((1.0 * size) / numBuckets >= 0.7) {
      ArrayList<HashNode<K, V> > temp = bucketArray;
      bucketArray = new ArrayList<>();
      numBuckets = 2 * numBuckets;
      size = 0;
      for (int i = 0; i < numBuckets; i++){
        bucketArray.add(null);
      }

      for (HashNode<K, V> headNode : temp){
        while (headNode != null){
          add(headNode.key, headNode.value);
          headNode = headNode.next;
        }
      }
    }
  }

  public String toString(){
    String result = "";
    for(int i=0; i<numBuckets; i++){
      result += "["+i+"]->";
      HashNode<K, V> head = bucketArray.get(i);
      while (head != null) {
        result += head.key+"->";
        head = head.next;
      }
      result += "\n";
    }
    return result;
  }

  // Driver method to test Map class
  public static void main(String[] args){
    HashTable<String, Integer> map = new HashTable<>();
    map.add("this", 1);
    map.add("coder", 2);
    map.add("this", 4);
    map.add("hi", 5);
    System.out.println(map);
    System.out.println(map.size());
    System.out.println(map.get("coder"));
    System.out.println(map.get("this"));
    System.out.println(map.remove("this"));
    System.out.println(map.remove("this"));
    System.out.println(map.size());
    System.out.println(map.isEmpty());
  
    map = new HashTable<>();
    for(int i=0; i<100; i++){
      String key = ""+i;
      System.out.println(((Object)key).toString());
      System.out.println(key.hashCode());
      map.add(key, 1);
    }
    System.out.println(map);
  }
}
