import java.awt.*;
import java.util.Random;

public class DiceButton extends Sprite {

    private Rectangle button;
    private Random rand;
    private int diceVal;

    private static final int width = 200 ;
    private static final int height = 100;

    public DiceButton(Point p) {
        super(p);
        button = new Rectangle(p.x, p.y, width, height);
        this.rand = new Random();
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(this.startPoint.x, this.startPoint.y, width, height);
        g.setColor(Color.white);
        g.drawString("Roll The Dice", this.startPoint.x + 10,
                this.startPoint.y + height / 2);
    }

    @Override
    public Point getEndPoint() {
        return null;
    }

    public Rectangle getButtonRectengle() {
        return this.button;
    }

    public void rollTheDice() {
        this.diceVal = 2 + this.rand.nextInt(5) + this.rand.nextInt(5);
    }

    public int getDiceVal() {
        return this.diceVal;
    }
}
