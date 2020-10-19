import javax.swing.*;
import java.awt.*;

/**
 * create secondary frame that show how much resources
 * from each type the player has.
 */
public class ResourcesPanel extends JPanel {

    private Player p;
    private int FONT_SIZE = 30;
    private int RESORCES_STRING_X = 60;
    private int PLAYER_STRING_X = Test1.RESOURCE_FRAME_WIDTH / 2 - 2 * FONT_SIZE;
    private int PLAYER_STRING_Y = 50;
    private int COLOR_RECT_X = PLAYER_STRING_X + 3 * FONT_SIZE + 15;


    public ResourcesPanel(Player p) {
        setBorder(BorderFactory.createLineBorder(Color.black));
        this.p = p;
    }

    public Dimension getPreferredSize() {
        return new Dimension(Test1.RESOURCE_FRAME_WIDTH, Test1.RESOURCE_FRAME_HEIGHT);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //check the quantity of each resource;
        int numOfMuds = Test1.getPlayerNumberOfSpecificResource(p.getResourcesList(), Test1.MUD);
        int numOfWoods = Test1.getPlayerNumberOfSpecificResource(p.getResourcesList(), Test1.WOOD);
        int numOfSheeps = Test1.getPlayerNumberOfSpecificResource(p.getResourcesList(), Test1.SHEEP);
        int numOfIrons = Test1.getPlayerNumberOfSpecificResource(p.getResourcesList(), Test1.IRON);
        int numOfHays = Test1.getPlayerNumberOfSpecificResource(p.getResourcesList(), Test1.HAY);

        //draw player color
        g.setFont(new Font("TimesRoman", Font.PLAIN, FONT_SIZE));
        g.drawString( "Player ", PLAYER_STRING_X, PLAYER_STRING_Y);
        g.setColor(p.getColor());
        g.fillRect(COLOR_RECT_X, PLAYER_STRING_Y - 20, 30, 30);

        // Draw quantity and color for every resource
        g.setColor(Color.BLACK);
        g.drawString(" Woods " + Integer.toString(numOfWoods), RESORCES_STRING_X, PLAYER_STRING_Y + 2 * FONT_SIZE);
        g.drawString(" Sheeps " +Integer.toString(numOfSheeps), RESORCES_STRING_X, PLAYER_STRING_Y + 4 * FONT_SIZE);
        g.drawString(" Hays " + Integer.toString(numOfHays), RESORCES_STRING_X, PLAYER_STRING_Y + 6 * FONT_SIZE);
        g.drawString(" Irons " + Integer.toString(numOfIrons), RESORCES_STRING_X, PLAYER_STRING_Y + 8 * FONT_SIZE);
        g.drawString( " Muds " + Integer.toString(numOfMuds), RESORCES_STRING_X,  PLAYER_STRING_Y + 10 * FONT_SIZE);

        g.setColor(new Color(0, 153, 0));
        g.fillRect(COLOR_RECT_X, PLAYER_STRING_Y +  FONT_SIZE + 5, 30, 30);
        g.setColor(new Color(0, 153, 0));
        g.fillRect(COLOR_RECT_X, PLAYER_STRING_Y + 3 * FONT_SIZE + 5, 30, 30);
        g.setColor(new Color(255, 255, 0));
        g.fillRect(COLOR_RECT_X, PLAYER_STRING_Y + 5 * FONT_SIZE + 5, 30, 30);
        g.setColor(new Color(160, 160, 160));
        g.fillRect(COLOR_RECT_X, PLAYER_STRING_Y + 7 * FONT_SIZE + 5, 30, 30);
        g.setColor(new Color(153, 76, 0));
        g.fillRect(COLOR_RECT_X, PLAYER_STRING_Y + 9 * FONT_SIZE + 5, 30, 30);


    }
}
