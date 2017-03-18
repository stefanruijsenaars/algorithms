/******************************************************************************
 *  Compilation:  javac RandomizedQueue.java
 *  Execution:    java RandomizedQueue
 *  Dependencies: Iterator.java, StdRandom.java
 *
 * Similar to a stack or queue, except that the item removed is chosen uniformly
 * at random from items in the data structure.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
    
    private Item[] storage;
    private int currentIdx;
    private int storageSize;
    
    public RandomizedQueue() {
        storageSize = 2;
        // construct an empty randomized queue
        storage = (Item[]) new Object[storageSize];
        currentIdx = 0;
    }
    
    public boolean isEmpty() {
        // is the queue empty?
        return currentIdx == 0;
    }
    
    public int size()             {
        // return the number of items on the queue
        return currentIdx;
    }
    
    public void enqueue(Item item)  {
        if (item == null) {
            throw new java.lang.NullPointerException();
        }
        // resize the array, if necessary
        if (currentIdx == storageSize) {
            resize(storageSize * 2);
        }
        
        // add the item
        storage[currentIdx++] = item;
    }
    
    private void resize(int size) {
        int oldSize = storageSize;
        storageSize = size;
        Item[] copy = (Item[]) new Object[storageSize];
        for (int i = 0; i < currentIdx; i++) {
           copy[i] = storage[i];
        }
        storage = copy;
    }
    
    public Item dequeue() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        
        // remove and return a random item
        int randomIdx = StdRandom.uniform(0, currentIdx);
        Item value = storage[randomIdx];
        
        storage[randomIdx] = storage[currentIdx - 1];
        storage[currentIdx - 1] = null;
        currentIdx--;
        
        // resize the array, if necessary
        if (currentIdx > 0 && currentIdx == storage.length / 4) {
            resize(storageSize / 2);
        }
        
        return value;
    }
    
    public Item sample() {
        // return (but do not remove) a random item
        if (isEmpty()) {
          throw new java.util.NoSuchElementException();
        }
        int randomIdx = StdRandom.uniform(currentIdx);
        return storage[randomIdx];
    }
    
    public Iterator<Item> iterator() {
        // return an independent iterator over items in random order
        return new RandomizedQueueIterator();
    }
    
    private class RandomizedQueueIterator implements Iterator<Item> {

        private int[] randomIndexes;
        private int index = 0;
        
        public RandomizedQueueIterator() {
            randomIndexes = new int[currentIdx];
            for (int i = 0; i < randomIndexes.length; i++) {
                randomIndexes[i] = i;
            }
            StdRandom.shuffle(randomIndexes);
        }
        
        // constant time
        public boolean hasNext()  { 
            return index < randomIndexes.length;
        }
        
        public void remove()      { 
            throw new java.lang.UnsupportedOperationException(); 
        }
        
        // constant time
        public Item next() {
            // if no more items:
            if (!hasNext()) {
              throw new java.util.NoSuchElementException();
            }
            Item val = storage[randomIndexes[index]];
            index++;
            return val;
        }
   }
    
    public static void main(String[] args) {
        // unit testing (optional)
        RandomizedQueue<String> queue = new RandomizedQueue<String>();
        queue.enqueue("hello");
        queue.enqueue("world");
        System.out.println(queue.size()); // 2
        queue.enqueue("bla");
        System.out.println(queue.size()); // 3
        for (String el : queue) {
            System.out.println(el + "!"); // bla!, hello!, world! (randomized)
        }
        System.out.println(queue.dequeue() + "?"); // random
        System.out.println(queue.dequeue() + "?"); // random
        System.out.println(queue.dequeue() + "?"); // random
        queue.enqueue("hi");
        queue.enqueue("there");
        for (String el : queue) {
            System.out.println(el); // hi, there (randomized)
        }
                        
    }
}