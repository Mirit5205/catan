import java.awt.*;

public class PressedButtonDecorator extends ButtonDecorator {
    public PressedButtonDecorator(Button decoratedButton) {
        super(decoratedButton);
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        g.setColor(Color.yellow);
        g.drawRect(this.b.buttonRectangle.x, this.b.buttonRectangle.y,
                this.b.buttonRectangle.width, this.b.buttonRectangle.y);
    }

}
