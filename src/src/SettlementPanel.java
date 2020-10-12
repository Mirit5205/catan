import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SettlementPanel extends JPanel {
    private List<Settlement> settlements = new ArrayList<>();

    public void addSettlement( Settlement s) {
        settlements.add(s);
        this.repaint();
    }

    @Override
    public void paint(Graphics g) {
        for (Settlement s : settlements) {
            s.render(g);
        }
    }
}
