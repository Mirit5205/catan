import java.awt.*;

public class EndTurnButton extends GameRunTimeButton {

    public EndTurnButton(Point p, int width, int height) {
        super(p, width, height);
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        g.drawString("End Turn", this.startPoint.x + 10,
                this.startPoint.y + (int) this.buttonRectangle.getHeight() / 2);
    }
}
