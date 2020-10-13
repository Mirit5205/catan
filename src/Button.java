import java.awt.*;
import java.util.List;

public class Button extends Sprite {

    private Rectangle buttonRectangle;
    private String buttonText;

    private static final int width = 200 ;
    private static final int height = 100;

    public Button(Point p, String s) {
        super(p);
        buttonRectangle = new Rectangle(p.x, p.y, width, height);
        buttonText = s;
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
        g.drawString(buttonText, this.startPoint.x + 72,
                this.startPoint.y + height / 2 );
    }

    @Override
    public Point getEndPoint() {
        return null;
    }

    public boolean IsPressed(List<Point> clicks) {
        boolean b = false;
        for (Point p : clicks) {
            if (this.buttonRectangle.contains(p)) {
                b = true;
            }
        }
        return b;
    }
}
