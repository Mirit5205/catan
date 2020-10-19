import java.awt.*;

public class UseCardButton extends GameRunTimeButton {

    public UseCardButton(Point p, int width, int height) {
        super(p, width, height);
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        g.drawString("Use Card", this.startPoint.x + 10,
                this.startPoint.y + (int) this.buttonRectangle.getHeight() / 2);
    }
}
