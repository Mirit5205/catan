import biuoop.DrawSurface;

import java.awt.*;

public class Hexagon implements Shape {
    private Color color;
    private Point a;
    private Point b;
    private Point c;
    private Point d;
    private Point e;
    private  Point f;
    private int edgeLength;
    private int hexagonRectangleWidth;

    public Hexagon (Point a, Point b) {
        this.a = a;
        this.b = b;
        this.edgeLength = (int)this.a.distance(this.b);
        this.c = new Point(b.x, b.y -  this.edgeLength);
        this.d = new Point(a.x, c.y - (a.y - b.y));
        this.e = new Point(d.x + (d.x - c.x) ,c.y);
        this.f = new Point(e.x, e.y +  this.edgeLength);
        this.hexagonRectangleWidth = this.e.x - this.c.x;
    }

    public void setColor(Color c) {
        this.color = c;
    }

    public int getHexagonRectangleWidth() {
        return hexagonRectangleWidth;
    }

    public Point getA() {
        return a;
    }

    public Point getB() {
        return b;
    }

    public Point getD() {
        return d;
    }

    public int getEdgeLength(){
        return this.edgeLength;
    }

    public void draw (DrawSurface surface) {
        int distance = (int)this.a.distance(this.b);
        Point c = new Point(b.x, b.y - distance);
        Point d = new Point(a.x, c.y - (a.y - b.y));
        Point e = new Point(d.x + (d.x - c.x) ,c.y);
        Point f = new Point(e.x, e.y + distance);

        surface.setColor(this.color);
        surface.drawLine(a.x, a.y, b.x, b.y);
        surface.drawLine(b.x, b.y, c.x, c.y);
        surface.drawLine(c.x, c.y, d.x, d.y);
        surface.drawLine(d.x, d.y, e.x, e.y);
        surface.drawLine(e.x, e.y, f.x, f.y);
        surface.drawLine(f.x, f.y, a.x, a.y);
        surface.fillRectangle(c.x, c.y, e.x - c.x, distance);

        for ( int i = 0; i <= (e.x - c.x) / 2; i++) {
            surface.drawLine(c.x + i ,c.y, d.x, d.y);
            surface.drawLine(e.x - i, e.y, d.x, d.y );
            surface.drawLine(b.x + i, b.y, a.x, a.y);
            surface.drawLine(f.x - i, f.y, a.x, a.y);

        }


    }
}
