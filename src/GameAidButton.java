import java.awt.*;

/**
 * abstract class that represent buttons that gives player information.
 */
public abstract class GameAidButton extends Button{

    public GameAidButton(Point p, int width, int height) {
        super(p, width, height);
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.lightGray);
        g.fillRect(this.startPoint.x, this.startPoint.y, buttonRectangle.width, buttonRectangle.height);
        g.setColor(Color.black);
    }
}
