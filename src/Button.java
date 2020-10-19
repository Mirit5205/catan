import java.awt.*;
import java.util.List;

public abstract class Button extends Sprite {

    protected Rectangle buttonRectangle;
    //private Color color;

    public Button(Point p, int width, int height) {
        super(p);
        buttonRectangle = new Rectangle(p.x, p.y, width, height);
    }

    @Override
    public void tick() {

    }

    @Override
    public abstract void render(Graphics g);

    @Override
    public Point getEndPoint() {
        return null;
    }

    public boolean isPressed(List<Point> clicks) {
        boolean b = false;
        for (Point p : clicks) {
            if (this.buttonRectangle.contains(p)) {
                b = true;
            }
        }
        return b;
    }

    /*
    public void setWidth(int width) {
        this.w = width;
    }

    public void setHeight(int height) {
        this.h = height;
    }

    public void setColor(Color c) {
        this.color = c;
    }
     */

   /* private static final int width = 200 ;
    private static final int height = 100;

    public Button(Point p, String s) {
        super(p);
        buttonRectangle = new Rectangle(p.x, p.y, width, height);
        buttonText = s;
    }

    public Button(Point p, String s, String test) {
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

    public void setWidth(int width) {
        this.w = width;
    }

    public void setHeight(int height) {
        this.h = height;
    }

    public void setColor(Color c) {
        this.color = c;
    }

    */
}
