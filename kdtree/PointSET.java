/******************************************************************************
 *  Compilation:  javac PointSET.java
 *  Execution:    java PointSET
 *  Dependencies:  
 * 
 *  Data type to represent a set of points in the unit square.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
/******************************************************************************
 *  Compilation:  javac PointSET.java
 *  Execution:    java PointSET
 *  Dependencies:  
 * 
 *  Brute force point set
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class PointSET {
    
    private SET<Point2D> storage;
    
    public PointSET() {
        // construct an empty set of points 
        this.storage = new SET<Point2D>();
    }
    
    public boolean isEmpty() {
        // is the set empty? 
        return this.storage.isEmpty();
    }
    
    public int size()    {
        // number of points in the set 
        return this.storage.size();
    }
    
    public void insert(Point2D p)  {
        // add the point to the set (if it is not already in the set)
        if (p == null) {
           throw new java.lang.NullPointerException();
        }
        this.storage.add(p);
    }
    
    public boolean contains(Point2D p) {
        // does the set contain point p? 
        if (p == null) {
           throw new java.lang.NullPointerException();
        }
        return this.storage.contains(p);
    }
    
    public void draw() {
        // draw all points to standard draw 
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        for (Point2D p : this.storage) {
            StdDraw.point(p.x(), p.y());
        }
    }
    
    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle 
        if (rect == null) {
           throw new java.lang.NullPointerException();
        }
        SET<Point2D> r = new SET<Point2D>();
        for (Point2D p : this.storage) {
            if (rect.contains(p)) {
                r.add(p);
            }
        }
        return r;
    }
    
    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty 
        if (p == null) {
           throw new java.lang.NullPointerException();
        }
        double minDist = Double.POSITIVE_INFINITY;
        Point2D currentNearest = null;
        
        for (Point2D point : this.storage) {
            double dist = p.distanceTo(point);
            if (dist < minDist) {
                minDist = dist;
                currentNearest = point;
            }
        }
        return currentNearest;
    }

    public static void main(String[] args) {
        // unit testing of the methods (optional) 
        PointSET ps = new PointSET();
        StdOut.println(ps.size()); // 0
        StdOut.println(ps.isEmpty()); // true
        ps.insert(new Point2D(0.2, 0.2));
        StdOut.println(ps.size()); // 1
        StdOut.println(ps.isEmpty()); // false
        Point2D another = new Point2D(0.2, 0.2);
        Point2D yetAnother = new Point2D(0.1, 0.1);
        StdOut.println(ps.contains(another)); // true
        StdOut.println(ps.contains(yetAnother)); // false
        ps.insert(yetAnother);
        Point2D zeroPoint = new Point2D(0, 0);
        StdOut.println(ps.nearest(zeroPoint).toString()); // 0.1, 0.1
        Iterable<Point2D> s = ps.range(new RectHV(0, 0, 0.15, 0.15));
        StdOut.println(s.toString()); // 0.1, 0.1
        ps.draw();
    }
}
