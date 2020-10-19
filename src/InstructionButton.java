import java.awt.*;

public class InstructionButton extends GameAidButton {
    public InstructionButton(Point p, int width, int height) {
        super(p, width, height);
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        g.drawString("i", this.startPoint.x + 10,
                this.startPoint.y + (int) this.buttonRectangle.getHeight() - 5);
    }
}
