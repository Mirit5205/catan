import java.awt.*;

public class CardButton extends GameRunTimeButton {
    public CardButton(Point p, int width, int height) {
        super(p, width, height);
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        g.drawString("Take Card", this.startPoint.x + 10,
                this.startPoint.y + (int) this.buttonRectangle.getHeight() / 2);
    }
}
