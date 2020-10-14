import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test1 extends State {

    private boolean doesAllPlayersPickThereLocations = false;
    private boolean doesDiceRolled = false;

    private int numOfPlayers;
    private Player[] players;
    private Board board;
    private List<Sprite> spriteList = new ArrayList<>();
    private List<Point> bufferList = new ArrayList<>();
    private List<Player> playersFirstTurnsByOrder = new ArrayList<>();
    private List<Point> clearVertexes;
    private List<Line> clearEdges;
    private DiceButton diceButton;
    private Button endTurnButton;

    private static String WOOD = "w";
    private static String SHEEP = "s";
    private static String HAY = "h";
    private static String IRON = "i";
    private static String MUD = "m";

    private static final String ROAD = "road";
    private static final String CITY = "city";
    private static final String SETTLEMENT = "settlement";
    private static final String CARD = "card";

    private int turnCounter = 0;


    private static int DICE_BUTTON_X = 100;
    private static int DICE_BUTTON_y = 300;


    public Test1(Game g) {
        super(g);
        clearVertexes = this.game.getBoard().BoardVertexList;
        clearEdges = this.game.getBoard().BoardEdgesList;
        diceButton = new DiceButton(new Point(DICE_BUTTON_X, DICE_BUTTON_y));
        endTurnButton = new Button(new Point(DICE_BUTTON_X, DICE_BUTTON_y + 200), "End Turn");
        spriteList.add(diceButton);
        spriteList.add(endTurnButton);
    }

    @Override
    public void tick() {
        //make a copy of the list of the mouse click points that represent setelments.
        bufferList.addAll(this.game.getMouseManager().DoubleClickScreenLocations);

        //roll dice and give every player resource accordingly.
        diceRolling();

        //every player pick his first locations
        firstStageOfGame();

        //every player play his turn according the rules
        secondStageOfGame();

        //check if player win
        if (isWin()) {
            System.exit(2);
        }

        //clear double click point list and buffer.
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
     *
     * @param b is the game board.
     */
    public void setBoard(Board b) {
        this.board = b;
    }

    /**
     * check if the point of the mouse click are
     * closest to any vertex.
     *
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
     *
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
     *
     * @param clearVertexes is the clear vertexes list.
     * @param p             is the point of the new setelment that cause the
     *                      changes.
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
     *
     * @param roadEdge is the given road
     * @return
     */
    public boolean isRoadLocationIsAccessible(Line roadEdge, Player p) {
        boolean b = false;

        for (Settlement s : p.getSettlementsList()) {
            if (isRoadIsNextToSettelment(s, roadEdge)) {
                b = true;
                break;
            }
        }

        for (Road r : p.getRoadsList()) {
            if (isRoadIsNextToRoad(r, roadEdge)) {
                b = true;
                break;
            }
        }
        return b;
    }

    /**
     * check if the given road is next to existent setelment.
     *
     * @param s        is a game sprite.
     * @param roadEdge is the given road.
     * @return true if it does.
     */
    public boolean isRoadIsNextToSettelment(Sprite s, Line roadEdge) {
        return s instanceof Settlement
                && (int) roadEdge.ptSegDist(s.startPoint) == 0;
    }

    /**
     * check if the given road is next to existent road.
     *
     * @param s        is a game sprite.
     * @param roadEdge is the given road.
     * @return true if it does.
     */
    public boolean isRoadIsNextToRoad(Sprite s, Line roadEdge) {
        return s instanceof Road
                && (roadEdge.getP1().distance(s.startPoint) == 0
                || roadEdge.getP2().distance(s.startPoint) == 0
                || roadEdge.getP1().distance(s.getEndPoint()) == 0
                || roadEdge.getP2().distance(s.getEndPoint()) == 0);
    }

    public boolean checkIfDiceButtonIsPressed(List<Point> clicks) {
        boolean b = false;
        for (Point p : clicks) {
            if (this.diceButton.getButtonRectengle().contains(p)) {
                b = true;
            }
        }
        return b;
    }

    public boolean checkIfSettlementAlreadyExist(Point newSettlementPoint) {
        boolean b = false;
        for (Sprite s : this.spriteList) {
            if (s.startPoint.equals(newSettlementPoint)) {
                b = true;
                break;
            }
        }
        return b;
    }

    /**
     * check if the desire road is already existed.
     *
     * @param newRoadLine is the desire road.
     * @return true if it does.
     */
    public boolean checkIfRoadAlreadyExist(Line newRoadLine) {
        boolean b = false;
        Line l;
        for (Sprite s : this.spriteList) {

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

    /**
     * check which player's settlements intersect with resources that has
     * stat that equal to dice value and add them to player resources list.
     *
     * @param p       is the player.
     * @param diceVal is the dice value.
     */
    public void takeResourcesAccordingToDice(int diceVal, Player p) {
        Line[] edges;

        for (Resource r : this.board.boardTiles) {
            edges = hexagonRepresentByEdges(r.getHexagon());
            for (Settlement s : p.getSettlementsList()) {
                for (int i = 0; i < edges.length; i++) {
                    if (edges[i].ptSegDist(s.startPoint) < 0.1) {
                        if (r.getStat() == diceVal)
                            p.getResourcesList().add(r.getResourceType());
                        break;
                    }
                }

            }
        }
    }

    /**
     * divide hexagon into edges and store them in array.
     *
     * @param h is the hexagon.
     * @return array of hexagon edges.
     */
    public Line[] hexagonRepresentByEdges(Hexagon h) {
        Line[] edges = {new Line(h.getA(), h.getB()), new Line(h.getB(), h.getC()),
                new Line(h.getC(), h.getD()), new Line(h.getD(), h.getE()),
                new Line(h.getE(), h.getF()), new Line(h.getF(), h.getA())};
        return edges;
    }

    /**
     * check if the player has enough resources for purchase specific structure or card.
     *
     * @param s is the desire structure.
     * @param p is the player.
     * @return true if it does.
     */
    public boolean doesHaveEnoughResources(String s, Player p) {

        boolean b = false;
/*
        switch (s) {
            case ROAD:
                b = isEnoughResourcesForRoad(p.getResourcesList());
                roadPurchase(p.getResourcesList());
            case SETTLEMENT:
                b = isEnoughResourcesForSettlement(p.getResourcesList());
                settlementPurchase(p.getResourcesList());
            case CITY:
                b = isEnoughResourcesForCity(p.getResourcesList());
                cityPurchase(p.getResourcesList());
            case CARD:
                b = isEnoughResourcesForDevelopmentCard(p.getResourcesList());
                cardPurchase(p.getResourcesList());
            default:
                break;
        }


 */
        if (s.equals(ROAD)) {
            b = isEnoughResourcesForRoad(p.getResourcesList());
            if (b) {
                roadPurchase(p.getResourcesList());
            }
        } else if (s.equals(SETTLEMENT)) {
            b = isEnoughResourcesForSettlement(p.getResourcesList());
            if (b) {
                settlementPurchase(p.getResourcesList());
            }
        } else if (s.equals(CITY)) {
            b = isEnoughResourcesForCity(p.getResourcesList());
            if (b) {
                cityPurchase(p.getResourcesList());
            }
        } else {
            b = isEnoughResourcesForDevelopmentCard(p.getResourcesList());
            if (b) {
                cardPurchase(p.getResourcesList());
            }
        }

        return b;


    }

    /**
     * check how many resources of specific type the player has.
     *
     * @param playerResources is player's resources list.
     * @param s               is the specific resource represent by string.
     * @return the quantity of this resource.
     */
    public static int getPlayerNumberOfSpecificResource(List<String> playerResources, String s) {
        int i = 0;
        for (String resourceType : playerResources) {
            if (resourceType.equals(s)) {
                i++;
            }
        }
        return i;
    }

    /**
     * check if the player has enough resources for purchase road.
     *
     * @param playerResources is player resources list.
     * @return true if it does.
     */
    public boolean isEnoughResourcesForRoad(List<String> playerResources) {
        return getPlayerNumberOfSpecificResource(playerResources, WOOD) >= 1
                && getPlayerNumberOfSpecificResource(playerResources, MUD) >= 1;
    }

    /**
     * check if the player has enough resources for purchase settlement.
     *
     * @param playerResources is player resources list.
     * @return true if it does.
     */
    public boolean isEnoughResourcesForSettlement(List<String> playerResources) {
        if (playerResources.size() == 0) {
            return false;
        }
        return getPlayerNumberOfSpecificResource(playerResources, WOOD) >= 1
                && getPlayerNumberOfSpecificResource(playerResources, MUD) >= 1
                && getPlayerNumberOfSpecificResource(playerResources, HAY) >= 1
                && getPlayerNumberOfSpecificResource(playerResources, SHEEP) >= 1;
    }

    /**
     * check if the player has enough resources for purchase city.
     *
     * @param playerResources is player resources list.
     * @return true if it does.
     */
    public boolean isEnoughResourcesForCity(List<String> playerResources) {
        return getPlayerNumberOfSpecificResource(playerResources, IRON) >= 3
                && getPlayerNumberOfSpecificResource(playerResources, HAY) >= 2;
    }

    /**
     * check if the player has enough resources for purchase development card.
     *
     * @param playerResources is player resources list.
     * @return true if it does.
     */
    public boolean isEnoughResourcesForDevelopmentCard(List<String> playerResources) {
        return getPlayerNumberOfSpecificResource(playerResources, IRON) >= 1
                && getPlayerNumberOfSpecificResource(playerResources, HAY) >= 1
                && getPlayerNumberOfSpecificResource(playerResources, SHEEP) >= 1;

    }


    /**
     * remove the resources value of road (according to game rules)
     * from player resource list.
     *
     * @param playerResources is player resource list.
     */
    public void roadPurchase(List<String> playerResources) {
        int numOfRemoveWoods = 0;
        int numOfRemoveMuds = 0;

        List<String> buffer = new ArrayList<>();
        buffer.addAll(playerResources);

        for (String resource : buffer) {

            if (resource.equals(WOOD)
                    && numOfRemoveWoods < 1) {
                numOfRemoveWoods++;
                playerResources.remove(resource);
            }
            if (resource.equals(MUD)
                    && numOfRemoveMuds < 1) {
                numOfRemoveMuds++;
                playerResources.remove(resource);
            }
        }
    }

    /**
     * remove the resources value of settlement (according to game rules)
     * from player resource list.
     *
     * @param playerResources is player resource list.
     */
    public void settlementPurchase(List<String> playerResources) {
        int numOfRemoveWoods = 0;
        int numOfRemoveMuds = 0;
        int numOfRemoveSheeps = 0;
        int numOfRemoveHays = 0;

        List<String> buffer = new ArrayList<>();
        buffer.addAll(playerResources);

        for (String resource : buffer) {

            if (resource.equals(WOOD)
                    && numOfRemoveWoods < 1) {
                numOfRemoveWoods++;
                playerResources.remove(resource);
            }

            if (resource.equals(MUD)
                    && numOfRemoveMuds < 1) {
                numOfRemoveMuds++;
                playerResources.remove(resource);
            }

            if (resource.equals(HAY)
                    && numOfRemoveHays < 1) {
                numOfRemoveHays++;
                playerResources.remove(resource);
            }

            if (resource.equals(SHEEP)
                    && numOfRemoveSheeps < 1) {
                numOfRemoveSheeps++;
                playerResources.remove(resource);
            }
        }
    }

    /**
     * remove the resources value of city (according to game rules)
     * from player resource list.
     *
     * @param playerResources is player resource list.
     */
    public void cityPurchase(List<String> playerResources) {
        int numOfRemoveIrons = 0;
        int numOfRemoveHays = 0;

        List<String> buffer = new ArrayList<>();
        buffer.addAll(playerResources);

        for (String resource : buffer) {

            if (resource.equals(IRON)
                    && numOfRemoveIrons < 3) {
                numOfRemoveIrons++;
                playerResources.remove(resource);
            }

            if (resource.equals(HAY)
                    && numOfRemoveHays < 2) {
                numOfRemoveHays++;
                playerResources.remove(resource);
            }
        }
    }

    /**
     * remove the resources value of development card (according to game rules)
     * from player resource list.
     *
     * @param playerResources is player resource list.
     */
    public void cardPurchase(List<String> playerResources) {
        int numOfRemoveIrons = 0;
        int numOfRemoveHays = 0;
        int numOfRemoveSheeps = 0;


        List<String> buffer = new ArrayList<>();
        buffer.addAll(playerResources);

        for (String resource : buffer) {

            if (resource.equals(IRON)
                    && numOfRemoveIrons < 1) {
                numOfRemoveIrons++;
                playerResources.remove(resource);
            }

            if (resource.equals(HAY)
                    && numOfRemoveHays < 1) {
                numOfRemoveHays++;
                playerResources.remove(resource);
            }

            if (resource.equals(SHEEP)
                    && numOfRemoveSheeps < 1) {
                numOfRemoveSheeps++;
                playerResources.remove(resource);
            }
        }
    }

    /**
     * set number of players to the game.
     *
     * @param numOfPlayers is the player's number.
     */
    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    /**
     * initial players array and set unique color for each.
     */
    public void initPlayersArr() {
        Color[] playersColors = {Color.RED, Color.MAGENTA, Color.LIGHT_GRAY, Color.DARK_GRAY};
        players = new Player[this.numOfPlayers];
        String[] resourceArr = {"w", "w", "w", "w", "w", "w", "w", "w",
                "m", "m", "m", "m", "m", "m", "m", "m",
                "s", "s", "s", "s", "s", "s", "s", "s",
                "h", "h", "h", "h", "h", "h", "h", "h",
                "w", "w", "w", "w", "w", "w", "w", "w",
                "m", "m", "m", "m", "m", "m", "m", "m",
                "s", "s", "s", "s", "s", "s", "s", "s",
                "h", "h", "h", "h", "h", "h", "h", "h",
                "w", "w", "w", "w", "w", "w", "w", "w",
                "m", "m", "m", "m", "m", "m", "m", "m",
                "s", "s", "s", "s", "s", "s", "s", "s",
                "h", "h", "h", "h", "h", "h", "h", "h","w",
                "w", "w", "w", "w", "w", "w", "w",
                "m", "m", "m", "m", "m", "m", "m", "m",
                "s", "s", "s", "s", "s", "s", "s", "s",
                "h", "h", "h", "h", "h", "h", "h", "h",
                "w", "w", "w", "w", "w", "w", "w", "w",
                "m", "m", "m", "m", "m", "m", "m", "m",
                "s", "s", "s", "s", "s", "s", "s", "s",
                "h", "h", "h", "h", "h", "h", "h", "h",
                "w", "w", "w", "w", "w", "w", "w", "w",
                "m", "m", "m", "m", "m", "m", "m", "m",
                "s", "s", "s", "s", "s", "s", "s", "s",
                "h", "h", "h", "h", "h", "h", "h", "h",
                "w", "w", "w", "w", "w", "w", "w", "w",
                "m", "m", "m", "m", "m", "m", "m", "m",
                "s", "s", "s", "s", "s", "s", "s", "s",
                "h", "h", "h", "h", "h", "h", "h", "h",
                "w", "w", "w", "w", "w", "w", "w", "w",
                "m", "m", "m", "m", "m", "m", "m", "m",
                "s", "s", "s", "s", "s", "s", "s", "s",
                "h", "h", "h", "h", "h", "h", "h", "h",
                "w", "w", "w", "w", "w", "w", "w", "w",
                "m", "m", "m", "m", "m", "m", "m", "m",
                "s", "s", "s", "s", "s", "s", "s", "s",
                "h", "h", "h", "h", "h", "h", "h", "h"};
        List<String> resourcelist = Arrays.asList(resourceArr);
        String[] playersNames = {"Red", "Pink", "Light Gray", "Dark Gray"};

        for (int i = 0; i < numOfPlayers; i++) {
            players[i] = new Player(playersColors[i]);
            players[i].getResourcesList().addAll(resourcelist);
            players[i].setPlayerName(playersNames[i]);
        }
    }


    /**
     * play turn in the game.
     *
     * @param player     is the player that it his turn.
     * @param bufferList is the clicks list.
     */

    public void playTurn(Player player, List<Point> bufferList) {
        /* for every point in the list find the closest board location.
           if we get null - remove the point from the point list "doubleClicks" of the
           mouse manager.
           if we get actual point, check if it closer to a vertex or to an edge.
           in case it closer to vertex - build settelment.
           in case it closer to edge - build road.
           if it already represent setelment or road do nothing. (check it in the
           sprite list).
         */
        System.out.println(player.getPlayerName() + " turn");
        Point p = bufferList.get(0);
        Point closestSettelmentPoint = findClosestVertex(p);
        Line closestRoadEdge = findClosestEdge(p);

        if (closestRoadEdge == null && closestSettelmentPoint == null) {
            //remove point from the list of mouse click points
            this.game.getMouseManager().DoubleClickScreenLocations.remove(p);
        } else if (closestSettelmentPoint != null) {
            if (doesHaveEnoughResources(SETTLEMENT, player)) {
                System.out.println("enough resources for settlement");
                buildSettlement(closestSettelmentPoint, player);
            }
        } else {
            if (doesHaveEnoughResources(ROAD, player)) {
                System.out.println("enough resources for road");
                buildRoad(closestRoadEdge, player);
            }
        }
    }

    /**
     * @return game's players arr.
     */
    public Player[] getGamePlayers() {
        return this.players;
    }

    /**
     * choose player initial locations.
     *
     * @param player is the player that its turn to choose location.
     */
    public void chooseFirstLocations(Player player) {
        Point p = bufferList.get(0);
        Point closestSettelmentPoint = findClosestVertex(p);
        Line closestRoadEdge = findClosestEdge(p);

        int playerSettlementsCounter = player.getSettlementsList().size();
        int playerRoadsCounter = player.getRoadsList().size();

        if (closestRoadEdge == null && closestSettelmentPoint == null) {
            //remove point from the list of mouse click points
            this.game.getMouseManager().DoubleClickScreenLocations.remove(p);
        } else if (closestSettelmentPoint != null) {
            if (playerSettlementsCounter == 0
                    || (playerSettlementsCounter == 1
                    && playerRoadsCounter == 1)) {
                buildSettlement(closestSettelmentPoint, player);
                playersFirstTurnsByOrder.remove(playersFirstTurnsByOrder.get(0));
            }
        } else {
            if (playerRoadsCounter == 0
                    || (playerRoadsCounter == 1
                    && playerSettlementsCounter == 2)) {
                buildRoad(closestRoadEdge, player);
                playersFirstTurnsByOrder.remove(playersFirstTurnsByOrder.get(0));
            }
        }
        turnCounter++;
    }

    /**
     * build road according to given point for specific player.
     *
     * @param RoadEdge is the road location.
     * @param p        is the player who own the new road.
     */
    public void buildRoad(Line RoadEdge, Player p) {
        System.out.println(!checkIfRoadAlreadyExist(RoadEdge));
        System.out.println(this.clearEdges.contains(RoadEdge));
        System.out.println(isRoadLocationIsAccessible(RoadEdge, p));
        if (!checkIfRoadAlreadyExist(RoadEdge)
                && this.clearEdges.contains(RoadEdge)
                && isRoadLocationIsAccessible(RoadEdge, p)) {
            Road newRoad = new Road(RoadEdge.getX1(),
                    RoadEdge.getY1(),
                    RoadEdge.getX2(),
                    RoadEdge.getY2());
            newRoad.setColor(p.getColor());
            //add point to game sprite list as setelment.
            this.spriteList.add(newRoad);
            p.getRoadsList().add(newRoad);
            System.out.println(p.getRoadsList());
        }
    }

    /**
     * build settlement according to given point for specific player.
     *
     * @param settlementPoint is the settlement location.
     * @param p               is the player who own the new settlement.
     */
    public void buildSettlement(Point settlementPoint, Player p) {
        if (this.clearVertexes.contains(settlementPoint)) {
            //add point to game sprite list as setelment.
            Settlement newSettlement = new Settlement(settlementPoint, this.game);
            newSettlement.setColor(p.getColor());
            this.spriteList.add(newSettlement);
            p.getSettlementsList().add(newSettlement);
            System.out.println(p.getSettlementsList());
            removeUnReachableSettelmentesLocations(this.clearVertexes, settlementPoint);
        }
    }


    /**
     * initial player's turns order.
     */
    public void initListOfFirstTurnsByOrder() {

        for (int i = 0; i < players.length; i++) {
            this.playersFirstTurnsByOrder.add(players[i]);
            this.playersFirstTurnsByOrder.add(players[i]);
        }

        for (int i = players.length - 1; i >= 0; i--) {
            playersFirstTurnsByOrder.add(players[i]);
            playersFirstTurnsByOrder.add(players[i]);
        }
    }

    /**
     * rolling the dice and give every player the appropriate resources.
     */
    public void diceRolling() {
        //check if dice button is pressed
        if (checkIfDiceButtonIsPressed(bufferList) && !doesDiceRolled) {
            //roll the dice and print their value
            this.diceButton.rollTheDice();
            System.out.println("Dice show " + this.diceButton.getDiceVal());

            //for every player in the game
            for (int i = 0; i < players.length; i++) {
                //take resources according to dice
                takeResourcesAccordingToDice(this.diceButton.getDiceVal(), players[i]);
                System.out.println(this.players[i].getPlayerName() + " " + this.players[i].getResourcesList());
            }
            this.doesDiceRolled = true;
            //clear buffer and click list
            bufferList.clear();
            this.game.getMouseManager().DoubleClickScreenLocations.clear();
        }
    }

    /**
     * every player choose is first locations.
     */
    public void firstStageOfGame() {
        if (this.turnCounter < numOfPlayers * 4) {
            if (!bufferList.isEmpty()) {
                chooseFirstLocations(playersFirstTurnsByOrder.get(0));
            }
            if (turnCounter == numOfPlayers * 4) {
                doesAllPlayersPickThereLocations = true;
            }
        }
    }

    /**
     * every player play in his turn.
     */
    public void secondStageOfGame() {
        if (doesAllPlayersPickThereLocations) {
            if (endTurnButton.IsPressed(bufferList) && this.doesDiceRolled) {
                turnCounter++;
                this.doesDiceRolled = false;
            }
            //check if there was a double click
            if (!bufferList.isEmpty()) {
                playTurn(players[turnCounter % numOfPlayers], bufferList);
            }
        }
    }

    public boolean isWin() {
        boolean b = false;
        for (int i = 0; i < players.length; i++) {
            players[i].updateScores();
            if (players[i].getScores() == 10) {
                b = true;
                System.out.println("congratulations " + players[i].getPlayerName() + ", You Just Reach 10 Scores, You Win!");
            }
        }
        return b;
    }
}
