import javax.swing.*;
import java.awt.*;

public class Display {

    private JFrame frame;
    private Canvas canvas;

    private String title;
    private int width, height;

    public Display(String s, int w, int h) {
        this.title = s;
        this.width = w;
        this.height = h;

        createDisply();
    }
    public void createDisply() {
        this.frame = new JFrame(this.title);
        this.frame.setSize(this.width, this.height);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setResizable(false);
        this.frame.setVisible(true);

        this.canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(this.width, this.height));
        canvas.setMaximumSize(new Dimension(this.width, this.height));
        canvas.setMinimumSize(new Dimension(this.width, this.height));
        canvas.setFocusable(false);

        this.frame.add(this.canvas);
        this.frame.pack();
    }

    public Canvas getCanvas() {
        return this.canvas;
    }

    public JFrame getFrame() {
        return this.frame;
    }
}
