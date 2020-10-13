import java.awt.*;

public class Road extends Sprite {

    private Line l;
    private Color c;

    public Road(double x1, double y1, double x2, double y2) {
        super(new Point((int)x1, (int)y1));
        this.l = new Line(new Point((int) x1, (int) y1), new Point((int) x2, (int) y2));
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {
        g.setColor(this.c);
        for (int i = 0; i < 10; i++ )
        g.drawLine((int) l.getX1() + i, (int) l.getY1(),
                (int) l.getX2() + i, (int) l.getY2());
    }

    @Override
    public Point getEndPoint() {
        return (Point) this.l.getP2();
    }

    public void setColor(Color c) {
        this.c = c;
    }
}
