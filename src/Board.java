import biuoop.DrawSurface;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
    List<Integer> BoardListOfNumbers = new ArrayList<>();
    List<String> boardResourcesList = new ArrayList<>();

    public static final Color DESERT_COLOR = Color.black;
    public static final Color HOT_NUMBER_COLOR = Color.red;



    public Board() {
        initialBoard();
    }
    private void initialBoard() {
        int[] boardNumbers = {2, 12, 3, 3, 4, 4, 5, 5, 6, 6, 8, 8, 9, 9, 10, 10, 11, 11};
        for (int i : boardNumbers) {
            BoardListOfNumbers.add(i);
        }

    /*
    h - hay
    m - mud
    i - iron
    s - sheep
    w - wood
    d- desert
     */
        String[] boardSources = {"h", "h", "h", "h",
                "m", "m", "m", "m",
                "i", "i", "i",
                "s", "s", "s", "s",
                "w", "w", "w",
                "d"};
        for (String s : boardSources) {
            boardResourcesList.add(s);
        }
    }
        public void drawBoard(DrawSurface d) {
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
            resourceList.add(resourcesFirstLine[0]);

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

            Random rand = new Random();

            //create duplicate list of resource types in order to not change the original list
            List<String> duplicateListOfBoardResources = new ArrayList<>();
            duplicateListOfBoardResources.addAll(this.boardResourcesList);

            //arrange the board randomly
            for (Resource r : resourceList) {
                int elementNumber = rand.nextInt(duplicateListOfBoardResources.size());
                r.setResourceType(duplicateListOfBoardResources.get(elementNumber));
                duplicateListOfBoardResources.remove(elementNumber);
                System.out.println(duplicateListOfBoardResources);

                r.draw(d);
            }

            //create duplicate list of stats in order to not change the original list
            List<Integer> duplicateListOfBoardNumbers = new ArrayList<>();
            duplicateListOfBoardNumbers.addAll(this.BoardListOfNumbers);

            //arrange the stats randomly
            /*for ( int i = 0; i < duplicateListOfBoardNumbers.size(); i++) {
                duplicateListOfBoardNumbers[i]
            }
             */
            for (Resource r : resourceList) {
                if (isTileIsDesert(r.getColor())) {
                    continue;
                }
                int elementNumber = rand.nextInt(duplicateListOfBoardNumbers.size());
                r.setStat(duplicateListOfBoardNumbers.get(elementNumber));
                duplicateListOfBoardNumbers.remove(elementNumber);
                System.out.println(duplicateListOfBoardNumbers);

                r.draw(d);
            }
    }

    public static boolean isTileIsDesert(Color c) {
        return c == DESERT_COLOR;
    }

    public static boolean IsHotNumber(int n) {
        return n == 6 || n == 8;
    }
}