/******************************************************************************
 *  Compilation:  javac Percolation.java
 *  Execution:    java Percolation
 *  Dependencies: WeightedQuickUnionUF.java
 *
 * Percolation
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    
    /**
     * The grid.
     * 
     * First index is row, second is col.
     * Value false if closed, true if open.
     */
    private boolean[][] grid;
    
    /**
     * The number of columns/rows in the grid.
     */
    private int size;
    
    /**
     * The number of open sites.
     */
    private int openSites;
    
    /**
     * The union-find data structure. Used for calculating percolation.
     */
    private WeightedQuickUnionUF unionFind;
    
    /**
     * A second union-find data structure without a virtual bottom, to prevent
     * backwash. Used for calculating fullness.
     */
    private WeightedQuickUnionUF unionFindNoVirtualBottom;
        
    public Percolation(int n) {
        this.size = n;
        
        // Add 2 for virtual top and virtual bottom.
        // Index 0 = virtual top
        // Index n * n + 1 = virtual bottom.
        this.unionFind = new WeightedQuickUnionUF(n * n + 2);
        this.unionFindNoVirtualBottom = new WeightedQuickUnionUF(n * n + 1);
        // create n-by-n grid, with all sites blocked
        if (n <= 0) {
            throw new java.lang.IllegalArgumentException("illegal n");
        }
        this.grid = new boolean[this.size + 1][this.size + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                // closed
                this.grid[i][j] = false;
            }
        }
        this.openSites = 0;
    }
    
    public    void open(int row, int col) {
        validateIndexes(row, col);
        // open site (row, col) if it is not open already
        if (this.isOpen(row, col)) {
            return;
        }
        this.grid[row][col] = true;
        this.openSites++;
        // Link the site in question to its open neighbors.
        // Link to virtual top.
        if (row == 1) {
            this.unionFind.union(xyTo1D(row, col), 0);
            this.unionFindNoVirtualBottom.union(xyTo1D(row, col), 0);
        }
        // Link to top.
        if (row > 1) {
            this.connectIfToIsOpen(row, col, row - 1, col);
        }
        // Link to bottom.
        if (row < this.size) {
            this.connectIfToIsOpen(row, col, row + 1, col);
        }
        // Link to virtual bottom.
        if (row == this.size) {
            this.unionFind.union(xyTo1D(row, col), this.size * this.size + 1);
        }
        // Link to left.
        if (col > 1) {
            this.connectIfToIsOpen(row, col, row, col - 1);
        }
        // Link to right.
        if (col < this.size) {
            this.connectIfToIsOpen(row, col, row, col + 1);
        }
            
    }
    
    private void connectIfToIsOpen(int fromX, int fromY, int toX, int toY) {
        if (this.isOpen(toX, toY)) {
            this.unionFind.union(xyTo1D(fromX, fromY), xyTo1D(toX, toY));
            this.unionFindNoVirtualBottom.union(xyTo1D(fromX, fromY), xyTo1D(toX, toY));
        }
    }
    
    public boolean isOpen(int row, int col) {
        validateIndexes(row, col);
        // is site (row, col) open?
        return this.grid[row][col];
    }
    
    public boolean isFull(int row, int col) {
        validateIndexes(row, col);
        // is site (row, col) full?
        return this.isOpen(row, col) && this.unionFindNoVirtualBottom.connected(0, this.xyTo1D(row, col));
    }
    
    public     int numberOfOpenSites() {
        return this.openSites;
    }
    
    public boolean percolates() {
        // does the system percolate?
        // check if virtual top and virtual bottom are connected.
        return this.unionFind.connected(0, this.size * this.size + 1);
    }

    public static void main(String[] args)  {
        Percolation percolation = new Percolation(4);
        percolation.open(1, 1);
        percolation.open(2, 1);
        percolation.open(3, 1);
        if (percolation.unionFind.connected(percolation.xyTo1D(1, 1), percolation.xyTo1D(2, 1))) {
            System.out.println("connected");
        }
        else {
            System.out.println("error!");
        }
    } 
    
    private int xyTo1D(int row, int col) {
        // Adding +1 for the virtual top.
        return (row - 1) + (col - 1) * this.size + 1;
    }
    
    private void validateIndexes(int row, int col) {
        if (row < 1 || col < 1 || row > this.size || col > this.size) {
            throw new java.lang.IndexOutOfBoundsException("index out of bounds");
        }
    }
}