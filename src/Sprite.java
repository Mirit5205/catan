import java.awt.*;

public abstract class Sprite {
    protected double x,y;

    public Sprite(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public abstract void tick();
    public abstract void render(Graphics g);
    

}
