import java.awt.*;

public class ShowResourceButton extends GameAidButton {

    public ShowResourceButton(Point p, int width, int height) {
        super(p, width, height);
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        g.drawString("r", this.startPoint.x + 10,
                this.startPoint.y + (int) this.buttonRectangle.getHeight() - 5);
    }
}
