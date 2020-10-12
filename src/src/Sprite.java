import java.awt.*;

public abstract class Sprite {
    protected Point startPoint;

    public Sprite(Point p) {
        this.startPoint = p;
    }
    public abstract void tick();
    public abstract void render(Graphics g);

    public abstract Point getEndPoint();


}
