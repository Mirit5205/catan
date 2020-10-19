import com.sun.javaws.util.JfxHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.lang.management.PlatformLoggingMXBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    private List<String> gameCards = new ArrayList<>();
    private List<Point> clearVertexes;
    private List<Line> clearEdges;

    private DiceButton diceButton;
    private EndTurnButton endTurnButton;
    private CardButton cardButton;
    private UseCardButton useCardButton;
    private InstructionButton instructionButton;

    private Button showResourceButton;
    private Button showDevCardsButton;

    public static String WOOD = "w";
    public static String SHEEP = "s";
    public static String HAY = "h";
    public static String IRON = "i";
    public static String MUD = "m";

    private static final String ROAD = "road";
    private static final String CITY = "city";
    private static final String SETTLEMENT = "settlement";
    private static final String CARD = "card";

    public static final String KNIGHT = "knight";
    public static final String MONOPOLY = "monopoly";
    public static final String PLENTY = "year of plenty";
    public static final String SCORE = "victory point";
    public static final String ROADS_BUILDER = "roads builder";

    public static final int CARDS_FRAME_WIDTH = 600;
    public static final int CARDS_FRAME_HEIGHT = 500;
    public static final int RESOURCE_FRAME_WIDTH = 400;
    public static final int RESOURCE_FRAME_HEIGHT = 400;
    public static final int RESOURCE_FRAME_X = 650;
    public static final int RESOURCE_FRAME_Y = 300;



    private int turnCounter = 0;


    private static int DICE_BUTTON_X = 100;
    private static int DICE_BUTTON_y = 300;

    private static int INSTRUCTION_BUTTON_X = 1850;
    private static int INSTRUCTION_BUTTON_Y = 30;


    public Test1(Game g) {
        super(g);
        clearVertexes = this.game.getBoard().BoardVertexList;
        clearEdges = this.game.getBoard().BoardEdgesList;

        diceButton = new DiceButton(new Point(DICE_BUTTON_X, DICE_BUTTON_y), 200, 100);
        endTurnButton = new EndTurnButton(new Point(DICE_BUTTON_X, DICE_BUTTON_y + 200), 200, 100);
        cardButton = new CardButton(new Point(DICE_BUTTON_X, DICE_BUTTON_y + 400), 200, 100);
        useCardButton = new UseCardButton(new Point(DICE_BUTTON_X, DICE_BUTTON_y + 600), 200, 100);

        instructionButton = new InstructionButton(new Point(INSTRUCTION_BUTTON_X, INSTRUCTION_BUTTON_Y), 30, 30);
        showResourceButton = new ShowResourceButton(new Point(INSTRUCTION_BUTTON_X - 60, INSTRUCTION_BUTTON_Y), 30, 30);
        showDevCardsButton = new ShowDevelopmentCardsButton(new Point(INSTRUCTION_BUTTON_X - 120, INSTRUCTION_BUTTON_Y), 30, 30);


        //add buttons
        spriteList.add(diceButton);
        spriteList.add(endTurnButton);
        spriteList.add(cardButton);
        spriteList.add(instructionButton);
        spriteList.add(showResourceButton);
        spriteList.add(showDevCardsButton);
        spriteList.add(useCardButton);

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
                return b;
                //break;
            }
        }

        for (Road r : p.getRoadsList()) {
            if (isRoadIsNextToRoad(r, roadEdge)) {
                b = true;
                return b;
                //break;
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

    public boolean isRoadIsNextToSettelment(Point settlementPoint, Line roadEdge) {
        return (int) roadEdge.ptSegDist(settlementPoint) == 0;
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
        /*boolean b = false;
        for (Point p : clicks) {
            if (this.diceButton.getButtonRectengle().contains(p)) {
                b = true;
            }
        }
        return b;

         */
        return this.diceButton.isPressed(clicks);
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

            //take 1 resources according to every settlement
            for (Settlement s : p.getSettlementsList()) {
                for (int i = 0; i < edges.length; i++) {
                    if (edges[i].ptSegDist(s.startPoint) < 0.1) {
                        if (r.getStat() == diceVal) {
                            p.getResourcesList().add(r.getResourceType());
                            break;
                        }
                    }
                }
            }

            //take 2 according resources for every city
            for (City c : p.getCitiesList()) {
                for (int i = 0; i < edges.length; i++) {
                    if (edges[i].ptSegDist(c.startPoint) < 0.1) {
                        if (r.getStat() == diceVal) {
                            p.getResourcesList().add(r.getResourceType());
                            p.getResourcesList().add(r.getResourceType());
                            break;
                        }
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
            System.out.println("check resources for city");
            b = isEnoughResourcesForCity(p.getResourcesList());
            if (b) {
                System.out.println("enough resources for city");
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
        System.out.println("irons " + getPlayerNumberOfSpecificResource(playerResources, IRON));
        System.out.println("hay " + getPlayerNumberOfSpecificResource(playerResources, HAY));
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
        /*String[] resourceArr = {"w", "w", "w", "w", "w", "w", "w", "w",
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
                "h", "h", "h", "h", "h", "h", "h", "h",
                "i", "i", "i", "i", "i", "i", "i", "i",
                "i","i", "i","i","i", "i", "i","i","i",
                "i","i", "i","i","i", "i", "i","i","i",
                "i","i", "i","i","i", "i", "i","i","i",
                "i","i", "i","i","i", "i", "i","i","i",
                "i","i", "i","i","i", "i", "i","i","i" };

         */
        String[] resourceArr = {"w", "w", "i", "i", "i", "s", "h", "h", "h", "m", "m"};
        List<String> resourcelist = Arrays.asList(resourceArr);
        String[] playersNames = {"Red", "Pink", "Light Gray", "Dark Gray"};

        for (int i = 0; i < numOfPlayers; i++) {
            players[i] = new Player(playersColors[i]);
            players[i].getResourcesList().addAll(resourcelist);
            players[i].setPlayerName(playersNames[i]);
        }
    }

    public void initGameCardsList() {
        String[] devCards = {KNIGHT, KNIGHT, KNIGHT, KNIGHT, KNIGHT,
                KNIGHT, KNIGHT, KNIGHT, KNIGHT, KNIGHT,
                KNIGHT, KNIGHT, KNIGHT, KNIGHT, KNIGHT,
                KNIGHT, KNIGHT, KNIGHT, KNIGHT, KNIGHT,
                MONOPOLY, MONOPOLY, PLENTY, PLENTY, SCORE, SCORE,
                SCORE, SCORE};
        this.gameCards = Arrays.asList(devCards);
        Collections.shuffle(this.gameCards);

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

        //try to purchase development card
        if (cardButton.isPressed(bufferList) && !this.gameCards.isEmpty()) {
            if (doesHaveEnoughResources(CARD, player)) {
                cardPurchase(player.getResourcesList());
                player.getDevelopmentCardsList().add(this.gameCards.get(0));
            }
        }

        if (showResourceButton.isPressed(bufferList)) {
            drawResourceFrame(player);

        }

        if (showDevCardsButton.isPressed(bufferList)) {
            drawCardsFrame(player);
        }

        if (useCardButton.isPressed(bufferList)) {
            useDevelopMentCard(player);
        }

        Point closestSettelmentPoint = findClosestVertex(p);
        Line closestRoadEdge = findClosestEdge(p);
        Settlement existedSettlement = player.getClosestSettlementByPoint(bufferList.get(0));

        if (closestRoadEdge == null && closestSettelmentPoint == null) {
            //remove point from the list of mouse click points
            this.game.getMouseManager().DoubleClickScreenLocations.remove(p);
        } else if (existedSettlement != null) {
            if (isThereAlreadySettlement(existedSettlement.getStartPoint(), player)) {
                if (doesHaveEnoughResources(CITY, player)) {
                    buildCity(existedSettlement, player);
                }
            }
        } else if (closestSettelmentPoint != null) {
            if (doesHaveEnoughResources(SETTLEMENT, player)) {
                buildSettlement(closestSettelmentPoint, player);
            }
        } else {
            if (doesHaveEnoughResources(ROAD, player)) {
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

                //if the settlement built successfully
                if (playerSettlementsCounter < player.getSettlementsList().size()) {
                    System.out.println("Settlement built! "
                            + player.getSettlementsList().size());
                    playersFirstTurnsByOrder.remove(playersFirstTurnsByOrder.get(0));
                    turnCounter++;
                }
            }
        } else {
            if (playerRoadsCounter == 0
                    || (playerRoadsCounter == 1
                    && playerSettlementsCounter == 2
                    && !isRoadIsNextToSettelment(player.getSettlementsList().get(0), closestRoadEdge)
                    && !isRoadIsNextToRoad(player.getRoadsList().get(0), closestRoadEdge))) {
                buildRoad(closestRoadEdge, player);


                //if the road built successfully
                if (playerRoadsCounter < player.getRoadsList().size()) {
                    System.out.println("Road built! "
                            + player.getRoadsList().size());
                    playersFirstTurnsByOrder.remove(playersFirstTurnsByOrder.get(0));
                    turnCounter++;
                }
            }
        }
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
        if (doesSettlementCanBeBuild(settlementPoint, p)) {
            //create new settlement according to point
            Settlement newSettlement = new Settlement(settlementPoint, this.game);

            //set settlement color according to player color
            newSettlement.setColor(p.getColor());

            //add settlement to sprite list and to player settlement list
            this.spriteList.add(newSettlement);
            p.getSettlementsList().add(newSettlement);

            System.out.println(p.getSettlementsList());

            //remove unreachable settlements locations according to game rules
            removeUnReachableSettelmentesLocations(this.clearVertexes, settlementPoint);
        }
    }

    /**
     * build settlement according to given point for specific player.
     *
     * @param s is the settlement location.
     * @param p               is the player who own the new settlement.
     */
    public void buildCity(Settlement s, Player p) {
        Point cityPoint = s.getStartPoint();
        System.out.println(cityPoint);
        City newCity = new City(cityPoint, this.game);

        //set settlement color according to player color
        newCity.setColor(p.getColor());

        //add settlement to sprite list and to player settlement list
        this.spriteList.add(newCity);
        p.getCitiesList().add(newCity);
        this.spriteList.remove(s);
        p.getSettlementsList().remove(s);


    }

    /**
     * initial player's turns order.
     */
    public void initListOfFirstTurnsByOrder() {

        //initial players turns: first - > last, last - > first
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
            if (endTurnButton.isPressed(bufferList) && this.doesDiceRolled) {
                turnCounter++;
                this.doesDiceRolled = false;
            }
            //check if there was a double click
            if (!bufferList.isEmpty()) {
                playTurn(players[turnCounter % numOfPlayers], bufferList);
            }
        }
    }

    /**
     * check if one of the players reach 10 scores and win.
     * @return true if it does.
     */
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

    /**
     * check if the settlement the player want to build is near to
     * one of his roads.
     * @param settlementPoint is the settlement point.
     * @param p is the player that want to build.
     * @return true if it does.
     */
    public boolean isNewSettlementCloseToPlayerRoad(Point settlementPoint, Player p) {
        boolean b = false;
        for (Road r : p.getRoadsList()) {
            if (isRoadIsNextToSettelment(settlementPoint, r.getRoadLine())) {
                b = true;
            }
        }
        return b;
    }

    /**
     * check if the settlement can be build by three parameters:
     * one is binding:
     * the settlement can build only in clear vertex.
     * and only one of the two others must exist:
     * - not all of the players pick locations in the first game stage.
     * - the settlement close to player's road.
     * @param settlementPoint is the settlement's point the player want to build
     * @param p is the player.
     * @return true if settlement can be build.
     */
    public boolean doesSettlementCanBeBuild(Point settlementPoint, Player p) {
        return this.clearVertexes.contains(settlementPoint)
                && (!doesAllPlayersPickThereLocations || isNewSettlementCloseToPlayerRoad(settlementPoint, p));
    }


    /**
     * check if the settlement the player want to build is
     * already exist in the player's settlements list.
     * @param desireSettlement is the settlement the player want to build.
     * @param p is the player.
     * @return true if it does.
     */
    public boolean isThereAlreadySettlement(Point desireSettlement, Player p) {
        boolean b = false;
        for (Settlement s : p.getSettlementsList()) {
            if (s.getStartPoint().equals(desireSettlement)) {
                b = true;
                break;
            }
        }
        return b;
    }

    /**
     * when the appropriate button is pressed, show player's resources.
     * @param p is the resource's owner.
     */
    public void drawResourceFrame(Player p) {
        JFrame frame = new JFrame();
        frame.setBounds(RESOURCE_FRAME_X, RESOURCE_FRAME_Y, RESOURCE_FRAME_WIDTH, RESOURCE_FRAME_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new ResourcesPanel(p));
        frame.setVisible(true);

    }

    /**
     * when the appropriate button is pressed, show player's dev cards.
     * @param p is the card's owner.
     */
    public void drawCardsFrame(Player p) {
        JFrame frame = new JFrame();
        frame.setBounds(RESOURCE_FRAME_X, RESOURCE_FRAME_Y, CARDS_FRAME_WIDTH, CARDS_FRAME_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new CardsPanel(p));
        frame.setVisible(true);
    }

    /**
     * player choose which of his dev cards to use.
     * @param p is the card's owner.
     */
    public void useDevelopMentCard(Player p) {
        UserInputFrame inputFrame = new UserInputFrame();

        while (!inputFrame.getIsActionPreformed()) {
            switch (inputFrame.getUserInput()) {
                case PLENTY:
                    System.out.println("use year of plenty");
                    break;
                case KNIGHT:
                    System.out.println("use knight");
                    break;

                case ROADS_BUILDER:
                    System.out.println("use road builder");
                    break;

                case MONOPOLY:
                    //System.out.println("use monopoly");
                    //check if the player has this type of card
                    if (p.getDevelopmentCardsList().contains(MONOPOLY)) {

                        //remove this card from player cards list (in order to prevent re-using)
                        p.getDevelopmentCardsList().remove(MONOPOLY);
                        UserInputFrame chooseResource = new UserInputFrame();
                        //MonopolyCard card = new MonopolyCard(chooseResource.getUserInput(), p, this);
                    }
                    break;

                default:
                    break;
            }
        }
    }
/*
    public int getSizeOfLongerRoad(Player p) {
        int i = 0;
        List<Road> interSectionRoads = new ArrayList<>();
        interSectionRoads.add(p.getRoadsList().get(i));

        for (Road r1 : interSectionRoads ) {
            for (Road r2 : p.getRoadsList()) {
                if (r1.getRoadLine().intersectsLine(r2.getRoadLine())
                        && !isRoadIntersectWithListOfRoads(r2 ,interSectionRoads)) {
                    interSectionRoads.add(r2);
                }
            }
        }
    }

    public boolean isRoadIntersectWithListOfRoads(Road road, List<Road> roadsList) {
        boolean b = false;
        for (Road r : roadsList) {
            if (road.getRoadLine().intersectsLine(r.getRoadLine())) {
                b = true;
            }
        }
        return b;
    }
 */



}

