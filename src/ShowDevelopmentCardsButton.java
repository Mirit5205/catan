import java.awt.*;

public class ShowDevelopmentCardsButton extends GameAidButton {
    public ShowDevelopmentCardsButton(Point p, int width, int height) {
        super(p, width, height);
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        g.drawString("c", this.startPoint.x + 10,
                this.startPoint.y + (int) this.buttonRectangle.getHeight() - 5);
    }
}
