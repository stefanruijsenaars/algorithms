/******************************************************************************
 *  Compilation:  javac Permutation.java
 *  Execution:    java Permutation NUMBER_OF_LINES < FILENAME
 *  Example:      java Permutation 8 < duplicates.txt
 *  Dependencies: StdIn.java, StdOut.java
 *
 * Permutation
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    
    public static void main(String[] args) {
        RandomizedQueue<String> queue = new RandomizedQueue<String>();
        int k = Integer.parseInt(args[0]);
        while (StdIn.hasNextLine() && !StdIn.isEmpty()) {
            queue.enqueue(StdIn.readString());
        }
        for (int j = 0; j < k; j++) {
            StdOut.println(queue.dequeue());
        }
    }
    
}