import javax.swing.*;
import java.awt.*;

/**
 * create secondary frame that show how much development cards
 * from each type the player has.
 */
public class CardsPanel extends JPanel {
    private Player p;

    private int FONT_SIZE = 30;
    private int CARD_STRING_X = 60;
    private int PLAYER_STRING_X = Test1.CARDS_FRAME_WIDTH / 2 - 2 * FONT_SIZE;
    private int PLAYER_STRING_Y = 50;
    private int COLOR_RECT_X = PLAYER_STRING_X + 3 * FONT_SIZE + 15;
    private int WIDTH = 500;
    private int COUNTER_STRING_X = 450;


    public CardsPanel(Player p) {
        setBorder(BorderFactory.createLineBorder(Color.black));
        this.p = p;
    }

    /**
     * @return the size of this panel;
     */
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, Test1.RESOURCE_FRAME_HEIGHT);
    }

    /**
     * draw the information on the panel.
     * @param g is the graphic board.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        /*
            BufferedImage myPicture = ImageIO.read(new File("path-to-file"));
            JLabel picLabel = new JLabel(new ImageIcon(myPicture));
            add(picLabel);

         */

        //check the quantity of each resource;
        int victoryPoint = p.getNumOfSpecificCard(Test1.SCORE);
        int yearOfPlenty = p.getNumOfSpecificCard(Test1.PLENTY);
        int knight = p.getNumOfSpecificCard(Test1.KNIGHT);
        int usedKnights = p.getNumOfUsedKnights();
        int monopoly = p.getNumOfSpecificCard(Test1.MONOPOLY);
        int roadBuilding = p.getNumOfSpecificCard(Test1.ROADS_BUILDER);

        //draw player color
        g.setFont(new Font("TimesRoman", Font.PLAIN, FONT_SIZE));
        g.drawString( "Player ", PLAYER_STRING_X, PLAYER_STRING_Y);
        g.setColor(p.getColor());
        g.fillRect(COLOR_RECT_X, PLAYER_STRING_Y - 20, 30, 30);

        // draw each card type and quantity
        g.setColor(Color.BLACK);
        g.drawString(" year of plenty ", CARD_STRING_X, PLAYER_STRING_Y + 2 * FONT_SIZE);
        g.drawString(Integer.toString(yearOfPlenty), COUNTER_STRING_X, PLAYER_STRING_Y + 2 * FONT_SIZE);

        g.drawString(" knight ",  CARD_STRING_X, PLAYER_STRING_Y + 4 * FONT_SIZE);
        g.drawString(Integer.toString(knight), COUNTER_STRING_X, PLAYER_STRING_Y + 4 * FONT_SIZE);
        g.setColor(Color.RED);
        g.drawString( Integer.toString(usedKnights), COUNTER_STRING_X + 2 * FONT_SIZE, PLAYER_STRING_Y + 4 * FONT_SIZE);

        g.setColor(Color.BLACK);
        g.drawString(" roads building ", CARD_STRING_X, PLAYER_STRING_Y + 6 * FONT_SIZE);
        g.drawString(Integer.toString(roadBuilding), COUNTER_STRING_X, PLAYER_STRING_Y + 6 * FONT_SIZE);

        g.drawString(" monopoly ", CARD_STRING_X, PLAYER_STRING_Y + 8 * FONT_SIZE);
        g.drawString(Integer.toString(monopoly), COUNTER_STRING_X, PLAYER_STRING_Y + 8 * FONT_SIZE);

        g.drawString( " victory point ", CARD_STRING_X, PLAYER_STRING_Y + 10 * FONT_SIZE);
        g.drawString(Integer.toString(victoryPoint), COUNTER_STRING_X,  PLAYER_STRING_Y + 10 * FONT_SIZE);


    }
}
