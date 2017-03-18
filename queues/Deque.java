/******************************************************************************
 *  Compilation:  javac Deque.java
 *  Execution:    java Deque
 *  Dependencies: StdStats.java, StdRandom.java
 *
 * Implemented with a linked-list with pointers to start and end to ensure
 * constant worst-case time, and space proportional to items currently in the
 * queue.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    
    private Node<Item> first;
    private Node<Item> last;
    private int size;
    private Item item;
    
    public Deque() {
        // construct an empty deque
        first = null;
        last = null;
        size = 0;
    }
    
    public boolean isEmpty() {
        // is the deque empty?
        return first == null;
    }
    
    public int size() {
        // return the number of items on the deque
        return size;
    }
    
   public void addFirst(Item item) {

       // add the item to the front
       if (item == null) {
           throw new java.lang.NullPointerException();
       }

       Node<Item> node = new Node<Item>(item);
       if (isEmpty()) {
           first = node;
           last = node;
       } 
       else {
           node.next = first;
           first = node;
           first.next.previous = first;
       }
       size++;
   }
   
   public void addLast(Item item)   {
       // add the item to the end
       if (item == null) {
            throw new java.lang.NullPointerException();
       }
       
       Node<Item> node = new Node<Item>(item);
       if (isEmpty()) {
           first = node;
           last = node;
       }
       else {
         node.previous = last;
         last.next = node;
         last = node;
       }
       size++;
   }
   
   public Item removeFirst() {
      if (isEmpty()) {
           throw new java.util.NoSuchElementException();
       }
       // remove and return the item from the front
       Item val = first.item;
       if (first == last) {
           // only one item
           first = null;
           last = null;
       }
       else {
           first = first.next;
           first.previous = null;
       }
       size--;
       return val;
   }
   
   public Item removeLast() {
       if (isEmpty()) {
           throw new java.util.NoSuchElementException();
       }
       Item val = last.item;
       // remove and return the item from the end
       if (first == last) {
           // only one item
           first = null;
           last = null;
       }
       else {
         last = last.previous;
         last.next = null;
       }
       size--;
       return val;
   }
   
   public Iterator<Item> iterator() {
       return new DequeIterator();
   }
   
   private class DequeIterator implements Iterator<Item> {
        private Node<Item> current = first;
        
        public boolean hasNext()  { 
            return current != null;                  
        }
        
        public void remove()      { 
            throw new UnsupportedOperationException(); 
        }

        public Item next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            Item item = current.item;
            current = current.next; 
            return item;
        }
   }
   
   public static void main(String[] args) {
       Deque<String> deque = new Deque<String>();
       deque.addFirst("hi");
       System.out.println(deque.removeFirst()); // hi
       deque.addLast("bye");
       System.out.println(deque.removeLast()); // bye
       deque.addFirst("1");
       deque.addFirst("2");
       deque.addFirst("3");
       for (String i : deque) {
            System.out.println(i + "!"); // 3! 2! 1!
       }
       System.out.println(deque.removeFirst()); // 3
       System.out.println(deque.removeLast()); // 1
       System.out.println(deque.removeLast()); // 2
       System.out.println(deque.isEmpty()); // true
   }
   
    /**
     * Nested class for use with doubly-linked list.
     */
    private class Node<Item> {
        
        private Item item;
        private Node<Item> next;
        private Node<Item> previous;
        
        public Node(Item value) {
            item = value;
            next = null;
            previous = null;
        }
    }
}
