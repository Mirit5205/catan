import java.awt.*;

public class Settlement extends Sprite {
    //private Point startLocation;
    private Rectangle r;
    //private int width = 20;
    //private int height = 20;
    public static int width = 20;
    public static int height = 20;
    private Point topOfRoof;
    private Color c = Color.MAGENTA;

    private Game game;

    public Settlement(Point p, Game g) {
        super(p.x, p.y);
        this.r = new Rectangle(p.x, p.y, this.width, this.height);
        this.topOfRoof = new Point(p.x +  10, p.y - 10);
        this.game = g;
    }

    @Override
    public void render(Graphics g) {
        g.setColor(this.c);
        g.fillRect(r.x, r.y, r.width, r.height);
        for ( int i = 0; i <= 10 ; i++) {
            g.drawLine(this.r.x + i, this.r.y, this.topOfRoof.x, this.topOfRoof.y);
            g.drawLine(this.r.x + 20 - i, this.r.y, this.topOfRoof.x, this.topOfRoof.y);
        }

    }

    @Override
    public void tick() {

    }

}
