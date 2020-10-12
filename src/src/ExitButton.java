import java.awt.*;

public class ExitButton extends Sprite{

    private Rectangle button;

    private static final int width = 200 ;
    private static final int height = 100;

    public ExitButton(Point p) {
        super(p);
        button = new Rectangle(p.x, p.y, width, height);
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(this.startPoint.x, this.startPoint.y, width, height);
        g.setColor(Color.white);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 35));
        g.drawString("Exit", this.startPoint.x + 72,
                this.startPoint.y + height / 2 );
    }

    @Override
    public Point getEndPoint() {
        return null;
    }

    public Rectangle getButtonRectangle() {
        return this.button;
    }

}
