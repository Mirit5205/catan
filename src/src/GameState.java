import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameState extends State {

    private Player player;
    private Board board;
    private List<Sprite> spriteList = new ArrayList<>();
    private List<Point> bufferList = new ArrayList<>();
    private List<Point> clearVertexes;
    private List<Line> clearEdges;
    private DiceButton diceButton;

    private static String WOOD = "w";
    private static String SHEEP = "s";
    private static String HAY = "h";
    private static String IRON = "i";
    private static String MUD = "m";

    private static String ROAD = "road";
    private static String CITY = "city";
    private static String SETTLEMENT = "settlement";
    private static String CARD = "card";






    private static int DICE_BUTTON_X = 100;
    private static int DICE_BUTTON_y = 300;



    public GameState(Game g) {
        super(g);
        clearVertexes = this.game.getBoard().BoardVertexList;
        clearEdges = this.game.getBoard().BoardEdgesList;
        diceButton = new DiceButton(new Point(DICE_BUTTON_X, DICE_BUTTON_y));
        spriteList.add(diceButton);
        player = new Player();
    }

    @Override
    public void tick() {
        //make a copy of the list of the mouse click points that represent setelments.
        bufferList.addAll(this.game.getMouseManager().DoubleClickScreenLocations);

        //check if dice button is pressed
        if (checkIfDiceButtonIsPressed(bufferList)) {
            //roll the dice and print their value
            this.diceButton.rollTheDice();
            System.out.println("Dice show " + this.diceButton.getDiceVal());

            //take resources according to dice
            takeResourcesAccordingToDice(this.diceButton.getDiceVal());
            System.out.println(this.player.getResourcesList());

            //clear buffer and click list
            bufferList.clear();
            this.game.getMouseManager().DoubleClickScreenLocations.clear();

            return;
        }

        /* for every point in the list find the closest board location.
           if we get null - remove the point from the point list "doubleClicks" of the
           mouse manager.
           if we get actual point, check if it closer to a vertex or to an edge.
           in case it closer to vertex - build settelment.
           in case it closer to edge - build road.
           if it already represent setelment or road do nothing. (check it in the
           sprite list).
         */
        for (Point p : bufferList) {
            Point closestSettelmentPoint = findClosestVertex(p);
            Line closestRoadEdge = findClosestEdge(p);

            if (closestRoadEdge == null && closestSettelmentPoint == null) {
                //remove point from the list of mouse click points
                this.game.getMouseManager().DoubleClickScreenLocations.remove(p);
            } else if (closestSettelmentPoint != null) {

                if (this.clearVertexes.contains(closestSettelmentPoint)) {

                    //add point to game sprite list as setelment.
                    this.spriteList.add(new Settlement(closestSettelmentPoint, this.game));
                    this.player.getSettlementsList().add(new Settlement(closestSettelmentPoint, this.game));
                    removeUnReachableSettelmentesLocations(this.clearVertexes, closestSettelmentPoint);
                }
            } else {
                /*System.out.println(!checkIfRoadAlreadyExist(closestRoadEdge));
                System.out.println(this.clearEdges.contains(closestRoadEdge));
                System.out.println(isRoadLocationIsAccessible(closestRoadEdge));
                System.out.println(isEnoughResourcesForRoad(this.player.getResourcesList()));

                 */

                if (!checkIfRoadAlreadyExist(closestRoadEdge)
                        && this.clearEdges.contains(closestRoadEdge)
                        && isRoadLocationIsAccessible(closestRoadEdge)
                        && doesHaveEnoughResources(ROAD, this.player)) {
                    //add point to game sprite list as setelment.
                    this.spriteList.add(new Road(closestRoadEdge.getX1(),
                            closestRoadEdge.getY1(),
                            closestRoadEdge.getX2(),
                            closestRoadEdge.getY2()));
                    //removeUnReachableSettelmentesLocations(this.clearVertexes, closestSettelmentPoint);
                }
            }
        }
        bufferList.clear();
        this.game.getMouseManager().DoubleClickScreenLocations.clear();
    }

    @Override
    public void render(Graphics g) {
        board.render(g);
        if (this.spriteList.size() != 0) {
            for (Sprite s : this.spriteList) {
                s.render(g);
            }
        }

    }

    /**
     * set game board to game state.
     * @param b is the game board.
     */
    public void setBoard(Board b) {
        this.board = b;
    }

    /**
     * check if the point of the mouse click are
     * closest to any vertex.
     * @param p is the mouse click point.
     * @return null if the point is not close to any vertex,
     * else it return the closest vertex;
     */
    public Point findClosestVertex(Point p) {
        Point closestPoint = this.board.BoardVertexList.get(0);
        double d = p.distance(closestPoint);

        //check which of the point are the closest to p
        for (Point p1 : this.board.BoardVertexList) {
            if (p1.distance(p) < d) {
                d = (p1.distance(p));
                closestPoint = p1;
            }
        }

        //if there is not vertex in radius of 15 from p return null
        if (d >= 15) {
            return null;
        } else {
            return closestPoint;
        }

    }

    /**
     * find board edge that is the closest to given mouse click point p.
     * @param p is the given point.
     * @return the closest board edge.
     */
    public Line findClosestEdge(Point p) {
        Line closestEdge = this.board.BoardEdgesList.get(0);
        double d = closestEdge.ptSegDist(p);

        //check which of the edges are the closest to p
        for (Line l : this.board.BoardEdgesList) {
            if (l.ptSegDist(p) < d) {
                d = l.ptSegDist(p);
                closestEdge = l;
            }
        }

        //if there is not edge in radius of 15 from p return null
        if (d >= 15) {
            return null;
        } else {
            return closestEdge;
        }

    }

    /**
     * remove unreachable setelments locations from clear vertexes list
     * according to game rules.
     * @param clearVertexes is the clear vertexes list.
     * @param p is the point of the new setelment that cause the
     * changes.
     */
    public void removeUnReachableSettelmentesLocations(List<Point> clearVertexes, Point p) {
        //calculate tile hexagon length
        Hexagon h = this.board.boardTiles.get(0).getHexagon();
        double d = h.getA().distance(h.getB());

        List<Point> duplicateList = new ArrayList<>();
        duplicateList.addAll(clearVertexes);
        for (Point p1 : duplicateList) {
            if (p1.distance(p) - d <= 0.00000001) {
                clearVertexes.remove(p1);
            }
        }
        clearVertexes.remove(p);
    }

    /**
     * check if the desirable road location is accessible.
     * @param roadEdge is the given road
     * @return
     */
    public boolean isRoadLocationIsAccessible(Line roadEdge) {
        boolean b = false;
        for (Sprite s : this.spriteList) {
            if (isRoadIsNextToSettelment(s, roadEdge)) {
                b = true;
                break;
            } else {
                if (isRoadIsNextToRoad(s, roadEdge)) {
                    b = true;
                    break;
                } else {
                    b = false;
                }
            }
        }
        return b;
    }

    /**
     * check if the given road is next to existent setelment.
     * @param s is a game sprite.
     * @param roadEdge is the given road.
     * @return true if it does.
     */
    public boolean isRoadIsNextToSettelment(Sprite s, Line roadEdge) {
       return s instanceof Settlement
                && (int) roadEdge.ptSegDist(s.startPoint) == 0;
    }

    /**
     * check if the given road is next to existent road.
     * @param s is a game sprite.
     * @param roadEdge is the given road.
     * @return true if it does.
     */
    public boolean isRoadIsNextToRoad(Sprite s, Line roadEdge) {
        return s instanceof Road
                &&  (roadEdge.getP1().distance(s.startPoint) == 0
                || roadEdge.getP2().distance(s.startPoint) == 0
                || roadEdge.getP1().distance(s.getEndPoint()) == 0
                || roadEdge.getP2().distance(s.getEndPoint()) == 0);
    }

    public boolean checkIfDiceButtonIsPressed(List<Point> clicks) {
        boolean b = false;
        for ( Point p : clicks) {
            if (this.diceButton.getButtonRectengle().contains(p)) {
                b = true;
            }
        }
        return b;
    }

    public boolean checkIfSettlementAlreadyExist(Point newSettlementPoint) {
        boolean b = false;
        for (Sprite s: this.spriteList) {
            if (s.startPoint.equals(newSettlementPoint)) {
                b = true;
                break;
            }
        }
        return b;
    }

    /**
     * check if the desire road is already existed.
     * @param newRoadLine is the desire road.
     * @return true if it does.
     */
    public boolean checkIfRoadAlreadyExist(Line newRoadLine) {
        boolean b = false;
        Line l;
        for (Sprite s: this.spriteList) {

            //if s is not road continue to the sprite
            if (!(s instanceof Road)) {
                continue;
            }

            l = new Line(s.startPoint, s.getEndPoint());
            if (l.equals(newRoadLine)) {
                b = true;
                break;
            }
        }
        return b;
    }

    public void takeResourcesAccordingToDice(int diceVal) {
        Line[] edges;

            for (Resource r : this.board.boardTiles) {
                edges = hexagonRepresentByEdges(r.getHexagon());
                for (Settlement s : this.player.getSettlementsList()) {
                    for (int  i = 0; i < edges.length; i++) {
                        if (edges[i].ptSegDist(s.startPoint) < 0.1) {
                            if (r.getStat() == diceVal )
                            this.player.getResourcesList().add(r);
                            break;
                        }
                    }

            }
        }
    }

    public Line[] hexagonRepresentByEdges(Hexagon h) {
        Line[] edges = { new Line(h.getA(), h.getB()), new Line(h.getB(), h.getC()),
                new Line(h.getC(), h.getD()), new Line(h.getD(), h.getE()),
                new Line(h.getE(), h.getF()), new Line(h.getF(), h.getA())};
        return edges;
    }

    public boolean doesHaveEnoughResources(String s, Player p) {

        boolean b = false;

        if (s.equals(ROAD)) {
            b = isEnoughResourcesForRoad(p.getResourcesList());
            roadPurchase(p.getResourcesList());
        } else if (s.equals(SETTLEMENT)) {
            b = isEnoughResourcesForSettlement(p.getResourcesList());
            settlementPurchase(p.getResourcesList());
        } else if (s.equals(CITY)) {
            b = isEnoughResourcesForCity(p.getResourcesList());
            cityPurchase(p.getResourcesList());
        } else {
            b = isEnoughResourcesForDevelopmentCard(p.getResourcesList());
            cardPurchase(p.getResourcesList());
        }
        return b;

    }

    public int getPlayerNumberOfSpecificResource(List<Resource> playerResources, String s) {
        int i = 0;
        for (Resource r : playerResources) {
            if (r.getResourceType().equals(s)) {
                i++;
            }
        }
        return i;
    }

    public boolean isEnoughResourcesForRoad(List<Resource> playerResources) {
        return getPlayerNumberOfSpecificResource(playerResources, WOOD) >= 1
                && getPlayerNumberOfSpecificResource(playerResources, MUD) >= 1;
    }

    public boolean isEnoughResourcesForSettlement(List<Resource> playerResources) {
        if (playerResources.size() == 0) {
            return false;
        }
        return getPlayerNumberOfSpecificResource(playerResources, WOOD) >= 1
                && getPlayerNumberOfSpecificResource(playerResources, MUD) >= 1
                && getPlayerNumberOfSpecificResource(playerResources, HAY) >= 1
                && getPlayerNumberOfSpecificResource(playerResources, SHEEP) >= 1;
    }

    public boolean isEnoughResourcesForCity(List<Resource> playerResources) {
        return getPlayerNumberOfSpecificResource(playerResources, IRON) >= 3
                && getPlayerNumberOfSpecificResource(playerResources, HAY) >= 2;
    }

    public boolean isEnoughResourcesForDevelopmentCard(List<Resource> playerResources) {
        return getPlayerNumberOfSpecificResource(playerResources, IRON) >= 1
                && getPlayerNumberOfSpecificResource(playerResources, HAY) >= 1
                && getPlayerNumberOfSpecificResource(playerResources, SHEEP) >= 1;

    }


    public void roadPurchase(List<Resource> playerResources) {
        int numOfRemoveWoods = 0;
        int numOfRemoveMuds = 0;

        List<Resource> buffer = new ArrayList<>();
        buffer.addAll(playerResources);

        for (Resource r : buffer) {

            if (r.getResourceType() == WOOD
                    && numOfRemoveWoods < 1 ) {
                numOfRemoveWoods++;
                playerResources.remove(r);
            }
            if (r.getResourceType() == MUD
                    && numOfRemoveMuds < 1 ) {
                numOfRemoveMuds++;
                playerResources.remove(r);
            }
         }
    }


    public void settlementPurchase(List<Resource> playerResources) {
        int numOfRemoveWoods = 0;
        int numOfRemoveMuds = 0;
        int numOfRemoveSheeps = 0;
        int numOfRemoveHays = 0;

        List<Resource> buffer = new ArrayList<>();
        buffer.addAll(playerResources);

        for (Resource r : buffer) {

            if (r.getResourceType() == WOOD
                    && numOfRemoveWoods < 1 ) {
                numOfRemoveWoods++;
                playerResources.remove(r);
            }

            if (r.getResourceType() == MUD
                    && numOfRemoveMuds < 1 ) {
                numOfRemoveMuds++;
                playerResources.remove(r);
            }

            if (r.getResourceType() == HAY
                    && numOfRemoveHays < 1 ) {
                numOfRemoveHays++;
                playerResources.remove(r);
            }

            if (r.getResourceType() == SHEEP
                    && numOfRemoveSheeps < 1 ) {
                numOfRemoveSheeps++;
                playerResources.remove(r);
            }
        }
    }

    public void cityPurchase(List<Resource> playerResources) {
        int numOfRemoveIrons = 0;
        int numOfRemoveHays = 0;

        List<Resource> buffer = new ArrayList<>();
        buffer.addAll(playerResources);

        for (Resource r : buffer) {

            if (r.getResourceType() == IRON
                    && numOfRemoveIrons < 3 ) {
                numOfRemoveIrons++;
                playerResources.remove(r);
            }

            if (r.getResourceType() == HAY
                    && numOfRemoveHays < 2 ) {
                numOfRemoveHays++;
                playerResources.remove(r);
            }
        }
    }

    public void cardPurchase(List<Resource> playerResources) {
        int numOfRemoveIrons = 0;
        int numOfRemoveHays = 0;
        int numOfRemoveSheeps = 0;


        List<Resource> buffer = new ArrayList<>();
        buffer.addAll(playerResources);

        for (Resource r : buffer) {

            if (r.getResourceType() == IRON
                    && numOfRemoveIrons < 1 ) {
                numOfRemoveIrons++;
                playerResources.remove(r);
            }

            if (r.getResourceType() == HAY
                    && numOfRemoveHays < 1 ) {
                numOfRemoveHays++;
                playerResources.remove(r);
            }

            if (r.getResourceType() == SHEEP
                    && numOfRemoveSheeps < 1 ) {
                numOfRemoveSheeps++;
                playerResources.remove(r);
            }
        }
    }

    /*
    public void setNumOfPlayers(int numOfPlayers) {
        this.player = numOfPlayers;
    }

     */


 }
