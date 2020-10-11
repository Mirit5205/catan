import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Line extends Line2D {

    private Point start;
    private Point end;


    public Line(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public double getX1() {
        return this.start.x;
    }

    @Override
    public double getY1() {
        return this.start.y;
    }

    @Override
    public Point2D getP1() {
        return this.start;
    }

    @Override
    public double getX2() {
        return this.end.x;
    }

    @Override
    public double getY2() {
        return this.end.y;
    }

    @Override
    public Point2D getP2() {
        return this.end;
    }

    @Override
    public void setLine(double x1, double y1, double x2, double y2) {

    }

    @Override
    public Rectangle2D getBounds2D() {
        return null;
    }

    public static void main(String[] args) {
        Line l = new Line(new Point(0, 0), new Point(10, 10));
        System.out.println(l.ptSegDist(new Point(10, 0)));

    }
}
