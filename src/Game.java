import javax.swing.*;
import java.awt.*;

import java.awt.Canvas;
import java.awt.Graphics;

public class Game  extends Canvas {
    public static void main(String[] args) {
        Frame frame = new JFrame("My Drawing");
        Canvas canvas = new Game();
        canvas.setSize(400, 400);
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
}

    public void paint(Graphics g) {
        g.fillOval(100, 100, 200, 200);
    }
}
