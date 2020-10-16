import java.awt.*;

public class City extends Sprite {

    private Settlement s1;
    private Settlement s2;

    public City(Point p, Game g) {
        super(p);
        this.s1 = new Settlement(p, g);
        this.s2 = new Settlement(new Point(p.x + Settlement.width / 3, p.y - Settlement.height / 2), g);
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {
        s1.render(g);
        s2.render(g);

    }

    @Override
    public Point getEndPoint() {
        return null;
    }

    public void setColor(Color c) {
        this.s1.setColor(c);
        this.s2.setColor(c);
    }
}
