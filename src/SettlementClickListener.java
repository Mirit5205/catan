import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SettlementClickListener extends MouseAdapter {

    private SettlementPanel panel;

    public SettlementClickListener(SettlementPanel s) {
        this.panel = s;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        //Settlement s = new Settlement(new Point(e.getX(), e.getY()));
        //this.panel.addSettlement(s);
        System.out.println(e.getX() + " " + e.getY());


    }
}
