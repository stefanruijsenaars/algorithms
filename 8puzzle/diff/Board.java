/******************************************************************************
 *  Compilation:  javac Board.java
 *  Execution:    java Board
 *
 * Board
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stack;

public class Board {
    
    private int n;
    private int[][] storage;
    
    public Board(int[][] blocks) {
        // construct a board from an n-by-n array of blocks
        // (where blocks[i][j] = block in row i, column j)
        n = blocks.length;
        storage = blocks.clone();
    }
    
    public int dimension() {
        // board dimension n
        return n;
    }
    
    // number of blocks out of place
    public int hamming() {
        int numOutOfPlace = 0;

        for (int i = 1; i < n * n; i++) {
            if (storage[(i - 1) / n][(i - 1) % n] != i) {
              numOutOfPlace++;
            }
        }
        return numOutOfPlace;
    }
    
    public int manhattan() {
        // sum of Manhattan distances between blocks and goal
        int distancesSum = 0;
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int number = storage[i][j];
                if (number != 0) {
                    int iGoal = (number - 1) / n;
                    int jGoal = (number - 1) % n;
                    int iDiff = Math.abs(iGoal - i);
                    int jDiff = Math.abs(jGoal - j);
                    distancesSum += iDiff + jDiff;
                }
            }
        }
        return distancesSum;
    }
    
    public boolean isGoal() {
        // is this board the goal board?
        return hamming() == 0;
    }
    
    public Board twin() {
        // a board that is obtained by exchanging any pair of blocks
        int i1 = -1;
        int j1 = -1;
        int i2 = -1;
        int j2 = -1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (storage[i][j] != 0) {
                    // Find 2 non-empty items and return a new board where
                    // they are exchanged.
                    if (i1 == -1 && j1 == -1) {
                        i1 = i;
                        j1 = j;
                    }
                    else if (i2 == -1 && j2 == -1) {
                        i2 = i;
                        j2 = j;
                    }
                }
            }
        }
        // switch positions
        return switchAndCreateNewBoard(i1, j1, i2, j2);
    }
    
    public boolean equals(Object y)  {
        // does this board equal y?
        if (y == null) {
            return false;
        }
        if (y == this) {
            return true;
        }
        if (y.getClass() != this.getClass()) {
            return false;
        }
        Board that = (Board) y;
        if (this.dimension() != that.dimension()) {
            return false;
        }
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (this.storage[i][j] != that.storage[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public Iterable<Board> neighbors() {
        // all neighboring boards
        Stack<Board> neighborsStack = new Stack<Board>();
        // find the 0
        int i1 = -1;
        int j1 = -1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (storage[i][j] == 0) {
                    i1 = i;
                    j1 = j;
                }
            }
        }
        // switch with up
        if (i1 > 0) {
            neighborsStack.push(switchAndCreateNewBoard(i1, j1, i1 - 1, j1));
        }
        // switch with down
        if (i1 < n - 1) {
            neighborsStack.push(switchAndCreateNewBoard(i1, j1, i1 + 1, j1));
        }
        // switch with left
        if (j1 > 0) {
            neighborsStack.push(switchAndCreateNewBoard(i1, j1, i1, j1 - 1));
        }
        // switch with right
        if (j1 < n - 1) {
            neighborsStack.push(switchAndCreateNewBoard(i1, j1, i1, j1 + 1));
        }
        return neighborsStack;
    }
    
    private Board switchAndCreateNewBoard(int i1, int j1, int i2, int j2) {
        int[][] newStorage = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                newStorage[i][j] = storage[i][j];
            }
        }
        int temp = newStorage[i1][j1];
        newStorage[i1][j1] = newStorage[i2][j2];
        newStorage[i2][j2] = temp;
        return new Board(newStorage);
    }
    
    
    public String toString() {
        // string representation of this board (in the output format specified below)
        String output = "";
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                output += storage[i][j];
                output += "   ";
            }
            output += "\n";
        }
        return output;
    }

    public static void main(String[] args) {
        // unit tests (not graded)
        int[][] items = new int[3][3];
        items[0][0] = 8;
        items[0][1] = 1;
        items[0][2] = 3;
        items[1][0] = 4;
        items[1][1] = 0;
        items[1][2] = 2;
        items[2][0] = 7;
        items[2][1] = 6;
        items[2][2] = 5;
        Board board = new Board(items);
        Board board2 = new Board(items);
        StdOut.println(board.toString());
        StdOut.println(board.hamming()); // 5
        StdOut.println(board.manhattan()); // 10
        StdOut.println(board.dimension()); // 3
        StdOut.println(board2.equals(board)); // true
        StdOut.println(board.equals(board)); // true
        StdOut.println(board.twin()); // 1 and 8 are interchanged
        Iterable<Board> neighborBoards = board.neighbors();
        StdOut.println("Iterating...");
        for (Board neighborBoard : neighborBoards) {
            StdOut.println(neighborBoard.toString());
        }
    }
}

