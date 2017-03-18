/******************************************************************************
 *  Compilation:  javac KdTree.java
 *  Execution:    java KdTree
 *  Dependencies:  
 * 
 *  Data type to represent a set of points in the unit square.
 *
 *  Grader: 83/100
 *  @todo improve timing, find problem in nearest()
 ******************************************************************************/

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.SET;

public class KdTree {
    
    private int treeSize = 0;
    private Node rootNode = null;
    
    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
    }
    
    public KdTree() {
      
    }
    
    public boolean isEmpty() {
        return this.treeSize == 0;
    }
    
    public int size()    {
        return this.treeSize;
    }
    
    public void insert(Point2D p)  {
        // add the point to the set (if it is not already in the set)
        if (p == null) {
            throw new java.lang.NullPointerException();
        }
        
        Node newNode = new Node();
        newNode.p = p;
        
        // true = vertical (x coordinate)
        // false = horizontal (y coordinate)
        boolean orientation = true;
        
        if (this.rootNode == null) {
            this.rootNode = newNode;
            // horizontal
            this.rootNode.rect = new RectHV(p.x(), 0, p.x(), 1);
        }
        else {
            Node currentNode = this.rootNode;
            Node parentNode;
            // @todo: split into helper functions
            while (true) {
                if (newNode.p.equals(currentNode.p)) {
                    // already exists
                    return;
                }
                parentNode = currentNode;
                if (orientation) {
                    // compare vertical.
                    if (newNode.p.x() < currentNode.p.x()) {
                        // point to be inserted has smaller x coordinate -> go left
                        if (currentNode.lb == null) {
                            currentNode.lb = newNode;
                            break;
                        }
                        else {
                            currentNode = currentNode.lb;
                        }
                    }
                    else {
                        // point to be inserted has larger or same x coordinate -> go right
                        if (currentNode.rt == null) {
                            currentNode.rt = newNode;
                            break;
                        }
                        else {
                            currentNode = currentNode.rt;
                        }
                    }
                }
                else {
                    // compare horizontal
                    if (newNode.p.y() < currentNode.p.y()) {
                        // point to be inserted has smaller y coordinate -> go left
                        if (currentNode.lb == null) {
                            currentNode.lb = newNode;
                            break;
                        }
                        else {
                            currentNode = currentNode.lb;
                        }
                    }
                    else {
                        // point to be inserted has larger or same y coordinate -> go right
                        if (currentNode.rt == null) {
                            currentNode.rt = newNode;
                            break;
                        }
                        else {
                            currentNode = currentNode.rt;
                        }
                    }
                }
                // flip orientation
                orientation = !orientation;
            }
            if (orientation) {
                // horizontal
                double cmp = p.x() - parentNode.p.x();
                if (cmp < 0) {
                    // horizontal (left of vertical parent)
                  newNode.rect = new RectHV(0, p.y(), parentNode.rect.xmin(), p.y());
                }
                else {
                  // horizontal  (right of vertical parent)
                  newNode.rect = new RectHV(parentNode.rect.xmin(), p.y(), 1, p.y());
                }

            }
            else {
                // vertical
                double cmp = p.y() - parentNode.p.y();
                if (cmp > 0) {
                  // vertical (above horizontal parent)
                  newNode.rect = new RectHV(p.x(), parentNode.rect.ymin(), p.x(), 1);
                }
                else {
                  // vertical (underneath horizontal parent)
                  newNode.rect = new RectHV(p.x(), 0, p.x(), parentNode.rect.ymin());
                }
            }
        }
        this.treeSize++;
    }
    
    public boolean contains(Point2D p) {
        // does the set contain point p? 
        if (p == null) {
            throw new java.lang.NullPointerException();
        }
        Node currentNode = this.rootNode;
        
        // true = vertical (x coordinate)
        // false = horizontal (y coordinate)
        boolean orientation = true;
        
        // @todo: split into helper functions
        while (true) {
            if (currentNode == null) {
                return false;
            }
            if (currentNode.p.equals(p)) {
                return true;
            }
            if (orientation) {
                // compare vertical.
                if (p.x() < currentNode.p.x()) {
                    // point to be searched has smaller x coordinate -> go left
                    currentNode = currentNode.lb;
                }
                else {
                    // point to be searched has larger or same x coordinate -> go right
                    currentNode = currentNode.rt;
                }
            }
            else {
                // compare horizontal
                if (p.y() < currentNode.p.y()) {
                    currentNode = currentNode.lb;
                }
                else {
                    // point to be searched has larger or same y coordinate -> go right
                    currentNode = currentNode.rt;
                }
            }
            // flip orientation
            orientation = !orientation;
        }
    }
    
    public void draw() {
        if (this.isEmpty()) {
            return;
        }
        // draw all points to standard draw 

        this.recursiveDraw(this.rootNode, true);
    }
    
    private void recursiveDraw(Node node, boolean orientation) {
        if (node == null) {
            return;
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(node.p.x(), node.p.y());
        StdDraw.setPenRadius();
        if (orientation) {
             // vertical
            StdDraw.setPenColor(StdDraw.RED);
        }
        else {
            // horizontal
            StdDraw.setPenColor(StdDraw.BLUE);
        }
        StdDraw.line(node.rect.xmin(), node.rect.ymin(), node.rect.xmax(), node.rect.ymax());
        recursiveDraw(node.rt, !orientation);
        recursiveDraw(node.lb, !orientation);
    }

    
    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle 
        if (rect == null) {
           throw new java.lang.NullPointerException();
        }
        SET<Point2D> r = new SET<Point2D>();
        if (this.isEmpty()) {
            return r;
        }
        this.findRange(rect, this.rootNode, r, true);
        return r;
    }
    
    private void findRange(RectHV rect, Node node, SET<Point2D> r, boolean orientation) {
        if (node == null) {
            return;
        }
        // check if point lies in given rectangle
        if (rect.contains(node.p)) {
            r.add(node.p);
        }
        // vertical and left bottom x of rectangle is smaller than line x
        // or horizontal and left bottom y of rectangle is smaller than line y
        if ((orientation && rect.xmin() <= node.p.x()) || (!orientation && rect.ymin() <= node.p.y())) {
          // if any could fall in rectangle, search left/bottom
          findRange(rect, node.lb, r, !orientation);
        }
        // vertical and right top x of rectangle is greater than line x
        // or horizontal and right top y of rectangle is greater than line y
        if ((orientation && rect.xmax() >= node.p.x()) || (!orientation && rect.ymax() >= node.p.y())) {
          // if any could fall in rectangle, search right/top
          findRange(rect, node.rt, r, !orientation);
        }
    }
    
    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty 
        if (p == null) {
           throw new java.lang.NullPointerException();
        }
        if (this.isEmpty()) {
            return null;
        }
        return findNearest(this.rootNode, this.rootNode, p, true).p;
    }
    
    private Node findNearest(Node node, Node nearestNode, Point2D p, boolean orientation) {
        if (node == null || node.rect.distanceSquaredTo(p) >= p.distanceSquaredTo(nearestNode.p)) {
            return nearestNode;
        }
        
        if (p.distanceSquaredTo(node.p) < p.distanceSquaredTo(nearestNode.p)) {
            nearestNode = node;
        }
        
        // Choose subtree that is on same side of the splitting line as query point p to go first
        if (orientation && p.x() < node.p.x() || !orientation && p.y() < node.p.y()) {
            nearestNode = findNearest(node.lb, nearestNode, p, !orientation);
            nearestNode = findNearest(node.rt, nearestNode, p, !orientation);
        }
        else {
            nearestNode = findNearest(node.rt, nearestNode, p, !orientation);
            nearestNode = findNearest(node.lb, nearestNode, p, !orientation);
        }
        
        return nearestNode;
    }

    public static void main(String[] args) {
        KdTree kt = new KdTree();
        StdOut.println(kt.size()); // 0
        StdOut.println(kt.isEmpty()); // true
        kt.insert(new Point2D(0.2, 0.2));
        StdOut.println(kt.size()); // 1
        StdOut.println(kt.isEmpty()); // false
        Point2D another = new Point2D(0.2, 0.2);
        Point2D yetAnother = new Point2D(0.1, 0.1);
        StdOut.println(kt.contains(another)); // true
        StdOut.println(kt.contains(yetAnother)); // false
        kt.insert(yetAnother);
        Point2D zeroPoint = new Point2D(0, 0);
        StdOut.println(kt.nearest(zeroPoint).toString()); // 0.1, 0.1
        Iterable<Point2D> s = kt.range(new RectHV(0, 0, 0.15, 0.15));
        StdOut.println(s.toString()); // 0.1, 0.1
        KdTree kt2 = new KdTree();
        kt2.insert(new Point2D(0.7, 0.2));
        kt2.insert(new Point2D(0.5, 0.4));
        kt2.insert(new Point2D(0.2, 0.3));
        kt2.insert(new Point2D(0.4, 0.7));
        kt2.insert(new Point2D(0.9, 0.6));
        for (Point2D point : kt2.range(new RectHV(0, 0, 1, 1))) {
            // all 5
            StdOut.println(point);
        }
        for (Point2D point : kt2.range(new RectHV(0, 0, 0.6, 1))) {
            // only 3 items
            StdOut.println(point);
        }
        kt2.draw();
        StdOut.println("Closest to (0,0): " + kt2.nearest(new Point2D(0, 0))); // (0.2, 0.3) 
        StdOut.println("Closest to (0.9, 0.7): " + kt2.nearest(new Point2D(0.9, 0.7))); // (0.9, 0.6)
        StdOut.println("Closest to (0.7, 0.3): " + kt2.nearest(new Point2D(0.7, 0.3))); // (0.7, 0.2)
    }
}
