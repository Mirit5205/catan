import java.awt.*;

public class RunTimeGameButtonDecorator extends ButtonDecorator {

    private static final int WIDTH = 30;
    private static final int HEIGHT = 30;
    private static final Color COLOR = Color.RED;


    public RunTimeGameButtonDecorator(Button decoratedButton) {
        super(decoratedButton);
    }

    @Override
    public void render(Graphics g) {
        this.b.setHeight(WIDTH);
        this.b.setWidth(HEIGHT);
        this.b.setColor(COLOR);
    }

    public void CreateButtonRectangle() {

    }
}
