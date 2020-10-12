import biuoop.DrawSurface;
import sun.java2d.windows.GDIRenderer;

import java.awt.*;

public class Resource {
    private int stat;
    private Color color;
    private Hexagon h;
    private String resourceType;

    private static final int BOARD_NUMBERS_FONT_SIZE = 40;

    public Resource(Hexagon h) {
        this.h = h;
    }

    /**
     * set resource type - represent by string and
     * set the according color.
     * @param s is the resource type represent by string.
     */
    public void setResourceType(String s) {
        this.resourceType = s;
        setColorAccordingToString(resourceType);
        this.h.setColor(this.color);
    }

    /**
     * set resource stat.
     * @param s is the stat of the resource.
     */
    public void setStat(int s) {
        this.stat = s;
    }

    /**
     * @return resource color.
     */
    public Color getColor() {
        return this.color;
    }


    public int getStat() {
        return this.stat;
    }

    /**
     * draw tile - resource type and stat.
     * @param d is the draw surface.
     */
    public void draw(Graphics d) {
        h.draw(d);
        drawStat(d);
    }

    /**
     * @return resource hexagon.
     */
    public Hexagon getHexagon() {
        return this.h;
    }

    /**
     * give the according color to the resource.
     * @param s represent resource type.
     */
    public void setColorAccordingToString(String s) {
        if (s.equals("w")) {
            this.color = new Color(0, 153, 0);;
        } else if (s.equals("s")) {
            this.color = new Color(51, 255, 51);;
        } else if (s.equals("m")) {
            this.color = new Color(153, 76, 0);
        } else if (s.equals("i")) {
            this.color = new Color(160, 160, 160);
        } else if (s.equals("h")) {
            this.color = new Color(255, 255, 0);;
        } else {
            this.color = Color.black;
        }
    }

    /**
     * draw the stat on the tile.
     * if tile is desert - draw noting.
     * @param d is the draw surface.
     */
    public void drawStat(Graphics d) {
        d.setColor(Color.white);
        int hexagonLength = this.getHexagon().getA().y - this.getHexagon().getD().y;
        int xVal = this.getHexagon().getA().x;
        int yVal = this.getHexagon().getA().y - hexagonLength / 2;
        if (Board.isTileIsDesert(this.getColor())) {
            d.setColor(Board.DESERT_COLOR);
        }
        if (Board.IsHotNumber(this.stat)) {
            d.setColor(Board.HOT_NUMBER_COLOR);
        }
        d.setFont(new Font("TimesRoman", Font.PLAIN, 30));
        d.drawString(Integer.toString(this.stat), xVal - 5 , yVal);
    }

    public String getResourceType() {
        return this.resourceType;
    }
}
