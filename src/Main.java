import biuoop.GUI;
import biuoop.DrawSurface;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    /*
        public static void drawBoard(DrawSurface d) {
            List<Hexagon> resourceList = new ArrayList<>();

            Hexagon[] hexagonsFirstLine = new Hexagon[3];
            Hexagon[] hexagonsSecondLine = new Hexagon[4];
            Hexagon[] hexagonsThirdLine = new Hexagon[5];
            Hexagon[] hexagonsFourthLine = new Hexagon[4];
            Hexagon[] hexagonsFifthLine = new Hexagon[3];

            Point firstTilePointA = new Point(750, 300);
            Point firstTilePointB = new Point(675, 240);

            hexagonsFirstLine[0] = new Hexagon(firstTilePointA,
                   firstTilePointB, Color.GRAY);
            hexagonsFirstLine[0].draw(d);
            int hexagonLength = hexagonsFirstLine[0].getA().y - hexagonsFirstLine[0].getD().y;
            int spaceBetweenHexagon = hexagonsFirstLine[0].getHexagonRectangleWidth();

            //draw first line of board
            for (int i = 1; i < hexagonsFirstLine.length; i++) {
                hexagonsFirstLine[i] = new Hexagon(new Point(firstTilePointA.x + (spaceBetweenHexagon * i),
                        firstTilePointA.y), new Point(firstTilePointB.x + (spaceBetweenHexagon * i)
                        , firstTilePointB.y), Color.red);
                //hexagonsFirstLine[i].draw(d);
                resourceList.add(hexagonsFirstLine[i]);
            }

            // draw second line of board
            for (int i = 0; i < hexagonsSecondLine.length ; i++) {
                hexagonsSecondLine[i] = new Hexagon(new Point(hexagonsFirstLine[0].getB().x + (spaceBetweenHexagon * i),
                        hexagonsFirstLine[0].getB().y + hexagonLength),
                        new Point(hexagonsFirstLine[0].getB().x -
                                (hexagonsFirstLine[0].getA().x -
                                        hexagonsFirstLine[0].getB().x) + (spaceBetweenHexagon * i) ,
                                hexagonsFirstLine[0].getA().y + hexagonsFirstLine[0].getEdgeLength()), Color.red);
                //hexagonsSecondLine[i].draw(d);
                resourceList.add(hexagonsSecondLine[i]);

            }

            // draw third line of board
            for (int i = 0; i < hexagonsThirdLine.length ; i++) {
                hexagonsThirdLine[i] = new Hexagon(new Point(hexagonsSecondLine[0].getB().x + (spaceBetweenHexagon * i),
                        hexagonsSecondLine[0].getB().y + hexagonLength),
                        new Point(hexagonsSecondLine[0].getB().x -
                                (hexagonsSecondLine[0].getA().x -
                                        hexagonsSecondLine[0].getB().x) + (spaceBetweenHexagon * i) ,
                                hexagonsSecondLine[0].getA().y + hexagonsSecondLine[0].getEdgeLength()), Color.red);
                //hexagonsThirdLine[i].draw(d);
                resourceList.add(hexagonsThirdLine[i]);

            }

            // draw fourth line of board
            for (int i = 0; i < hexagonsFourthLine.length ; i++) {
                hexagonsFourthLine[i] = new Hexagon(new Point(hexagonsFirstLine[0].getB().x + (spaceBetweenHexagon * i),
                        hexagonsThirdLine[0].getB().y + hexagonLength),
                        new Point(hexagonsFirstLine[0].getB().x -
                                (hexagonsFirstLine[0].getA().x -
                                        hexagonsFirstLine[0].getB().x) + (spaceBetweenHexagon * i),
                                hexagonsThirdLine[0].getA().y + hexagonsThirdLine[0].getEdgeLength()), Color.red);
                //hexagonsFourthLine[i].draw(d);
                resourceList.add(hexagonsFourthLine[i]);

            }

            // draw fifth line of board
            for (int i = 0; i < hexagonsFifthLine.length ; i++) {
                hexagonsFifthLine[i] = new Hexagon(new Point(firstTilePointA.x + (spaceBetweenHexagon * i),
                        hexagonsFourthLine[0].getB().y + hexagonLength),
                        new Point(firstTilePointB.x + (spaceBetweenHexagon * i),
                                hexagonsFourthLine[0].getA().y + hexagonsFourthLine[0].getEdgeLength()), Color.red);
                //hexagonsFifthLine[i].draw(d);
                resourceList.add(hexagonsFifthLine[i]);

            }

            for (Hexagon h : resourceList) {
                h.draw(d);
            }
        }

     */
    public static void drawBoard(DrawSurface d) {
        List<Resource> resourceList = new ArrayList<>();

        Resource[] resourcesFirstLine = new Resource[3];
        Resource[] resourcesSecondLine = new Resource[4];
        Resource[] resourcesThirdLine = new Resource[5];
        Resource[] resourcesFourthLine = new Resource[4];
        Resource[] resourcesFifthLine = new Resource[3];

        Point firstTilePointA = new Point(750, 300);
        Point firstTilePointB = new Point(675, 240);

        resourcesFirstLine[0] = new Resource(new Hexagon(firstTilePointA,
                firstTilePointB));
        //resourcesFirstLine[0].draw(d);

        int hexagonLength = resourcesFirstLine[0].getHexagon().getA().y
                - resourcesFirstLine[0].getHexagon().getD().y;
        int spaceBetweenHexagon = resourcesFirstLine[0].getHexagon().getHexagonRectangleWidth();

        //draw first line of board
        for (int i = 1; i < resourcesFirstLine.length; i++) {
            resourcesFirstLine[i] = new Resource(new Hexagon(new Point(firstTilePointA.x
                    + (spaceBetweenHexagon * i), firstTilePointA.y), new Point(firstTilePointB.x
                    + (spaceBetweenHexagon * i), firstTilePointB.y)));
            resourceList.add(resourcesFirstLine[i]);
        }

        // draw second line of board
        for (int i = 0; i < resourcesSecondLine.length ; i++) {
            resourcesSecondLine[i] = new Resource(new Hexagon(new Point(resourcesFirstLine[0].getHexagon().getB().x
                    + (spaceBetweenHexagon * i), resourcesFirstLine[0].getHexagon().getB().y
                    + hexagonLength), new Point(resourcesFirstLine[0].getHexagon().getB().x
                    - (resourcesFirstLine[0].getHexagon().getA().x
                    - resourcesFirstLine[0].getHexagon().getB().x)
                    + (spaceBetweenHexagon * i) , resourcesFirstLine[0].getHexagon().getA().y
                    + resourcesFirstLine[0].getHexagon().getEdgeLength())));
            resourceList.add(resourcesSecondLine[i]);

        }

        // draw third line of board
        for (int i = 0; i < resourcesThirdLine.length ; i++) {
            resourcesThirdLine[i] = new Resource(new Hexagon(new Point(resourcesSecondLine[0].getHexagon().getB().x
                    + (spaceBetweenHexagon * i), resourcesSecondLine[0].getHexagon().getB().y
                    + hexagonLength), new Point(resourcesSecondLine[0].getHexagon().getB().x
                    - (resourcesSecondLine[0].getHexagon().getA().x
                    - resourcesSecondLine[0].getHexagon().getB().x)
                    + (spaceBetweenHexagon * i) , resourcesSecondLine[0].getHexagon().getA().y
                    + resourcesSecondLine[0].getHexagon().getEdgeLength())));
            resourceList.add(resourcesThirdLine[i]);

        }

        // draw fourth line of board
        for (int i = 0; i < resourcesFourthLine.length ; i++) {
            resourcesFourthLine[i] = new Resource(new Hexagon(new Point(resourcesFirstLine[0].getHexagon().getB().x
                    + (spaceBetweenHexagon * i), resourcesThirdLine[0].getHexagon().getB().y
                    + hexagonLength), new Point(resourcesFirstLine[0].getHexagon().getB().x
                    - (resourcesFirstLine[0].getHexagon().getA().x
                    - resourcesFirstLine[0].getHexagon().getB().x)
                    + (spaceBetweenHexagon * i), resourcesThirdLine[0].getHexagon().getA().y
                    + resourcesThirdLine[0].getHexagon().getEdgeLength())));
            resourceList.add(resourcesFourthLine[i]);

        }

        // draw fifth line of board
        for (int i = 0; i < resourcesFifthLine.length ; i++) {
            resourcesFifthLine[i] = new Resource(new Hexagon(new Point(firstTilePointA.x
                    + (spaceBetweenHexagon * i), resourcesFourthLine[0].getHexagon().getB().y
                    + hexagonLength), new Point(firstTilePointB.x
                    + (spaceBetweenHexagon * i), resourcesFourthLine[0].getHexagon().getA().y
                    + resourcesFourthLine[0].getHexagon().getEdgeLength())));
            resourceList.add(resourcesFifthLine[i]);

        }

        for (Resource r : resourceList) {
            //r.draw(d);
        }
    }
        public static void main(String[] args) {

            GUI gui = new GUI("Random Circles Example", 2000, 1000);
            DrawSurface d = gui.getDrawSurface();
            Board b = new Board();
            b.drawBoard(d);
            //drawBoard(d);
            //drawSecondBoardLine(d);
            //drawThirdtBoardLine(d);
            //drawFourthBoardLine(d);
            //drawFifthBoardLine(d);
           /* Hexagon[] hexagonsArr = new Hexagon[19];
            for ( int i = 0; i < 19; i++) {
                hexagonsArr[i] = new Hexagon(baseX + 80 * i, baseY, Color.RED);
                if (i == 3) {
                    hexagonsArr[i] = new Hexagon(baseX + 80 * i, baseY + 80, Color.RED);
                }
                if (i == 7) {
                    hexagonsArr[i] = new Hexagon(baseX + 80 * i, baseY + 160, Color.RED);
                }
                if (i == 12) {
                    hexagonsArr[i] = new Hexagon(baseX + 80 * i, baseY + 240, Color.RED);
                }
                if (i == 16) {
                    hexagonsArr[i] = new Hexagon(baseX + 80 * i, baseY + 400, Color.RED);
                }
                hexagonsArr[i].draw(d);
            }

            */
            gui.show(d);

        }
    }

