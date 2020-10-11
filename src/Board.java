import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board  {

    List<Resource> boardTiles = new ArrayList<>();

    List<Point> BoardVertexList = new ArrayList<>();
    List<Line> BoardEdgesList = new ArrayList<>();


    public static final Color DESERT_COLOR = Color.black;
    public static final Color HOT_NUMBER_COLOR = Color.red;


    /**
     * initial resource stat list.
     * @return initial resource stat list.
     */
    private List<Integer> initialBoardListNumbers() {
        List<Integer> BoardListOfNumbers = new ArrayList<>();
        int[] boardNumbers = {2, 12, 3, 3, 4, 4, 5, 5, 6, 6, 8, 8, 9, 9, 10, 10, 11, 11};
        for (int i : boardNumbers) {
            BoardListOfNumbers.add(i);
        }
        return BoardListOfNumbers;

    }

    /**
     * initial board resource list.
     * h - hay
     * m - mud
     * i - iron
     * s - sheep
     * w - wood
     * d- desert
     * @return the initial board resource list.
     */
    private List<String> initialBoardResourcesList() {
        List<String> boardResourcesList = new ArrayList<>();
        String[] boardSources = {"h", "h", "h", "h",
                "m", "m", "m", "m",
                "i", "i", "i",
                "s", "s", "s", "s",
                "w", "w", "w",
                "d"};
        for (String s : boardSources) {
            boardResourcesList.add(s);
        }
        return boardResourcesList;
    }

    /**
     * initial the board tiles randomly according to game's rules.
     */
    public void initBoardTiles() {
        List<String> boardResourcesList = initialBoardResourcesList();
        List<Integer> BoardListOfNumbers = initialBoardListNumbers();


        List<Resource> resourceList = new ArrayList<>();

            //arr for every line of the board.
            Resource[] resourcesFirstLine = new Resource[3];
            Resource[] resourcesSecondLine = new Resource[4];
            Resource[] resourcesThirdLine = new Resource[5];
            Resource[] resourcesFourthLine = new Resource[4];
            Resource[] resourcesFifthLine = new Resource[3];

            //create the first tile that we use as guideline for the rest tiles
            Point firstTilePointA = new Point(750, 300);
            Point firstTilePointB = new Point(675, 240);
            resourcesFirstLine[0] = new Resource(new Hexagon(firstTilePointA,
                    firstTilePointB));

            resourceList.add(resourcesFirstLine[0]);

            //calculate space between the tiles and between each line.
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
                        + (int) resourcesFirstLine[0].getHexagon().getEdgeLength())));
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
                        + (int) resourcesSecondLine[0].getHexagon().getEdgeLength())));
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
                        + (int) resourcesThirdLine[0].getHexagon().getEdgeLength())));
                resourceList.add(resourcesFourthLine[i]);

            }

            // draw fifth line of board
            for (int i = 0; i < resourcesFifthLine.length ; i++) {
                resourcesFifthLine[i] = new Resource(new Hexagon(new Point(firstTilePointA.x
                        + (spaceBetweenHexagon * i), resourcesFourthLine[0].getHexagon().getB().y
                        + hexagonLength), new Point(firstTilePointB.x
                        + (spaceBetweenHexagon * i), resourcesFourthLine[0].getHexagon().getA().y
                        + (int) resourcesFourthLine[0].getHexagon().getEdgeLength())));
                resourceList.add(resourcesFifthLine[i]);

            }

            Random rand = new Random();

            //create duplicate list of resource types in order to not change the original list
            List<String> duplicateListOfBoardResources = new ArrayList<>();
            duplicateListOfBoardResources.addAll(boardResourcesList);

            //arrange the board randomly
            for (Resource r : resourceList) {
                int elementNumber = rand.nextInt(duplicateListOfBoardResources.size());
                r.setResourceType(duplicateListOfBoardResources.get(elementNumber));
                duplicateListOfBoardResources.remove(elementNumber);
            }

            //create duplicate list of stats in order to not change the original list
            List<Integer> duplicateListOfBoardNumbers = new ArrayList<>();
            duplicateListOfBoardNumbers.addAll(BoardListOfNumbers);

            //arrange the stats randomly
            for (Resource r : resourceList) {
                if (isTileIsDesert(r.getColor())) {
                    continue;
                }
                int elementNumber = rand.nextInt(duplicateListOfBoardNumbers.size());
                r.setStat(duplicateListOfBoardNumbers.get(elementNumber));
                duplicateListOfBoardNumbers.remove(elementNumber);
            }
            this.boardTiles.addAll(resourceList);
            initMouseLocationPosibilities(resourceList);
    }

    /**
     * draw the board.
     * @param g is the grafhic surface.
     */
    public void render(Graphics g) {
        for (Resource r : this.boardTiles) {
            r.draw(g);
        }
    }

    /**
     * check if the tile is desert.
     * @param c is the color.
     * @return true if it is.
     */
    public static boolean isTileIsDesert(Color c) {
        return c == DESERT_COLOR;
    }

    /**
     * check if the number is 6 or 8.
     * @param n is the number
     * @return true if it is.
     */
    public static boolean IsHotNumber(int n) {
        return n == 6 || n == 8;
    }

    public void addHexagonEdgesToList(Hexagon h) {
        Line[] edgesArr = { new Line(h.getA(), h.getB()),
                new Line(h.getB(), h.getC()),
                new Line(h.getC(), h.getD()),
                new Line(h.getD(), h.getE()),
                new Line(h.getE(), h.getF()),
                new Line(h.getF(), h.getA()) };

        for (int i = 0; i < edgesArr.length; i++) {
            if (!this.BoardEdgesList.contains(edgesArr[i])) {
                this.BoardEdgesList.add(edgesArr[i]);
            }
        }

    }

    public void addHexagonVertexToList(Hexagon h) {
        Point[] vertexArr = { new Point(h.getA().x, h.getA().y),
                new Point(h.getB().x, h.getB().y),
                new Point(h.getC().x, h.getC().y),
                new Point(h.getD().x, h.getD().y),
                new Point(h.getE().x, h.getE().y ),
                new Point(h.getF().x, h.getF().y) };

        for (int i = 0; i < vertexArr.length; i++) {
            if (!this.BoardVertexList.contains(vertexArr[i])) {
                this.BoardVertexList.add(vertexArr[i]);
            }
        }

    }

    public void initMouseLocationPosibilities(List<Resource> boardTiles) {
        for (Resource r : boardTiles) {
            addHexagonVertexToList(r.getHexagon());
            addHexagonEdgesToList(r.getHexagon());
        }
    }

}