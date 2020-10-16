import java.awt.*;

public class ButtonDecorator {
    protected Button b;

    public ButtonDecorator(Button decoratedButton){
        this.b = decoratedButton;
    }

    public void render(Graphics g){
        b.render(g);
    }
}
