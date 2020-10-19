import java.awt.*;

/**
 * abstract class that represent buttons that let the player
 * preform actions.
 */
public abstract class GameRunTimeButton extends Button {

    public GameRunTimeButton(Point p, int width, int height) {
        super(p, width, height);
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(this.startPoint.x, this.startPoint.y, buttonRectangle.width, buttonRectangle.height);
        g.setColor(Color.white);
    }
}
