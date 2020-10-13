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

    @Override
    public boolean equals(Object o){
        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof Line)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        Line other = (Line) o;

        // Compare the data members and return accordingly
        return this.getP1().distance(other.getP1()) == 0
                && this.getP2().distance(other.getP2()) == 0;
    }
}

