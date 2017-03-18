/******************************************************************************
 *  Compilation:  javac Solver.java
 *  Execution:    java Solver
 *
 * Solver
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {
    
    private MinPQ<SearchNode> pq;
    private MinPQ<SearchNode> pqTwin;
    private Board initial;
    private int moves = 0;
    private Iterable<Board> solution;
    private boolean isSolvable;
    
    private class SearchNode implements Comparable<SearchNode> {
        public Board board;
        public int numMoves;
        public SearchNode previous;
        private int priority;
        
        public SearchNode (Board board, int numMoves, SearchNode previous) {
            this.board = board;
            this.numMoves = numMoves;
            this.previous = previous;
            this.priority = board.manhattan() + numMoves;
        }
        
        public int compareTo(SearchNode other) {
            if (this.priority > other.priority) {
                return 1;
            }
            if (this.priority < other.priority) {
                return -1;
            }
            return 0;
        }
        
        public Iterable<SearchNode> neighbors() {
            Stack<SearchNode> neighbors = new Stack<SearchNode>();
            for (Board boardNeighbor : this.board.neighbors()) {
                neighbors.push(new SearchNode(boardNeighbor, this.numMoves + 1, this));
            }
            return neighbors;
        }
    }
    
    public Solver(Board initial) {
        // find a solution to the initial board (using the A* algorithm)
        if (initial == null) {
            throw new java.lang.NullPointerException();
        }
        this.initial = initial;
        
        SearchNode node = new SearchNode(initial, 0, null);
        SearchNode twinNode = new SearchNode(initial.twin(), 0, null);
        pq = new MinPQ<SearchNode>();
        pqTwin = new MinPQ<SearchNode>();
        
        // insert search node into pq
        pq.insert(node);
        pqTwin.insert(twinNode);
        
        while (true) {
            // delete and get minimum
            SearchNode min = pq.delMin();
            SearchNode minTwin = pqTwin.delMin();
            
            if (min.board.isGoal()) {
                isSolvable = true;
                SearchNode current = min;
                Stack<Board> solution = new Stack<Board>();
                solution.push(current.board);
                while (current.previous != null) {
                    current = current.previous;
                    solution.push(current.board);
                    moves++;
                }
                return;
            }
            
            if (minTwin.board.isGoal()) {
                isSolvable = false;
                return;
            }
            
            // insert all neighboring search nodes
            Iterable<SearchNode> neighbors = min.neighbors();
            for (SearchNode neighbor : neighbors) {
                // except when the neighbor is the same as the previous
                if (min.previous == null || !neighbor.board.equals(min.previous.board)) {
                    pq.insert(neighbor);
                }
            }
            
            Iterable<SearchNode> neighborsTwin = minTwin.neighbors();
            for (SearchNode neighborTwin : neighborsTwin) {
                // except when the neighbor is the same as the previous
                if (minTwin.previous == null || !neighborTwin.board.equals(minTwin.previous.board)) {
                    pqTwin.insert(neighborTwin);
                }
            }
        }
    }
    
    public boolean isSolvable() {
        // is the initial board solvable?
        return isSolvable;
    }
    
    public int moves() {
        // min number of moves to solve initial board; -1 if unsolvable
        if (!isSolvable()) {
            return -1;
        }
        else {
            return moves;
        }
    }
    
    public Iterable<Board> solution() {
        // sequence of boards in a shortest solution; null if unsolvable
        if (!isSolvable()) {
            return null;
        }
        else {
            return solution;
        }
    }
    
    public static void main(String[] args) {
        
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
            blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        
        // solve the puzzle
        Solver solver = new Solver(initial);
        
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
    
}
