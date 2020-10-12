import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

public class MouseManager implements MouseListener, MouseMotionListener {

    //public List<Point> settelments = new ArrayList<>();
    //public List<Point> roads = new ArrayList<>();
    public List<Point> DoubleClickScreenLocations = new ArrayList<>();



    public void tick() {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2 && !e.isConsumed()) {
            e.consume();
            //this.settelments.add(new Point(e.getX(), e.getY()));
            this.DoubleClickScreenLocations.add(new Point(e.getX(), e.getY()));
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
        //System.out.println("pressed");

    }


    @Override
    public void mouseReleased(MouseEvent e) {
        //System.out.println("Released");

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //System.out.println("Entered");

    }

    @Override
    public void mouseExited(MouseEvent e) {
        //System.out.println("mouseExited");

    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
