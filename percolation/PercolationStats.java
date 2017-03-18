/******************************************************************************
 *  Compilation:  javac PercolationStats.java
 *  Execution:    java PercolationStats
 *  Dependencies: StdStats.java, StdRandom.java
 *
 * PercolationStats
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    
    private double[] results;
    private int trials;
        
    public PercolationStats(int n, int trials) {
        // perform trials independent experiments on an n-by-n grid
        if (n <= 0 || trials <= 0) {
            throw new java.lang.IllegalArgumentException("invalid n or trials");
        }
        this.trials = trials;
        this.results = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            double count = 0;
            while (!percolation.percolates()) {
                int randomRow = StdRandom.uniform(1, n + 1);
                int randomCol = StdRandom.uniform(1, n + 1);
                if (percolation.isOpen(randomRow, randomCol)) {
                    continue;
                }
                percolation.open(randomRow, randomCol);
                count++;
            }
            this.results[i] = count / (n * n);
        }
    }
    
    public double mean() {
        // sample mean of percolation threshold
        return StdStats.mean(this.results);
    }
    public double stddev() {
        // sample standard deviation of percolation threshold
        return StdStats.stddev(this.results);
    }
    public double confidenceLo() {
        // low  endpoint of 95% confidence interval
        return this.mean() - (1.96 * this.stddev()) / Math.sqrt(this.trials);
    }
    public double confidenceHi() {
        // high endpoint of 95% confidence interval
        return this.mean() + (1.96 * this.stddev()) / Math.sqrt(this.trials);
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(n, t);
        // test client (described below)
        System.out.println("mean                    = " + percolationStats.mean());
        System.out.println("stddev                  = " + percolationStats.stddev());
        System.out.println("95% confidence interval = [" 
                               + percolationStats.confidenceLo() 
                               + ", " + percolationStats.confidenceHi() 
                               + "]");
    }
}
