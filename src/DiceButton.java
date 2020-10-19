import java.awt.*;
import java.util.Random;

public class DiceButton extends GameRunTimeButton {

    //private String buttonText;
    private Random rand;
    private int diceVal;

    //private static final int width = 200 ;
    //private static final int height = 100;

    public DiceButton(Point p, int width, int height) {
        super(p, width, height);
        this.rand = new Random();
    }


    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        g.drawString("Roll The Dice", this.startPoint.x + 10,
                this.startPoint.y + (int) this.buttonRectangle.getHeight() / 2);
    }

    @Override
    public Point getEndPoint() {
        return null;
    }

    public Rectangle getButtonRectengle() {
        return this.buttonRectangle;
    }

    public void rollTheDice() {
        this.diceVal = 2 + this.rand.nextInt(5) + this.rand.nextInt(5);
    }

    public int getDiceVal() {
        return this.diceVal;
    }
}
