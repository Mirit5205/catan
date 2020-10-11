import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameState extends State {

    private Board board;
    private List<Sprite> spriteList = new ArrayList<>();
    private List<Point> bufferList = new ArrayList<>();
    private List<Point> clearVertexes;
    private List<Line> clearEdges;


    public GameState(Game g) {
        super(g);
        clearVertexes = this.game.getBoard().BoardVertexList;
        clearEdges = this.game.getBoard().BoardEdgesList;
    }

    @Override
    public void tick() {
        //make a copy of the list of the mouse click points that represent setelments.
        bufferList.addAll(this.game.getMouseManager().DoubleClickScreenLocations);

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
                if (!this.spriteList.contains(closestSettelmentPoint)
                        && this.clearVertexes.contains(closestSettelmentPoint)) {
                    //add point to game sprite list as setelment.
                    this.spriteList.add(new Settlement(closestSettelmentPoint, this.game));
                    removeUnReachableSettelmentesLocations(this.clearVertexes, closestSettelmentPoint);
                    //System.out.println(spriteList);
                }
            } else {
                if (!this.spriteList.contains(closestRoadEdge)
                        && this.clearEdges.contains(closestRoadEdge)
                        && isRoadLocationIsAccessible(closestRoadEdge)) {
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

    public boolean isRoadLocationIsAccessible(Line roadEdge) {
        boolean b = false;
        for (Sprite s : this.spriteList) {
            if (s instanceof Settlement
                    && (int) roadEdge.ptSegDist(s.x, s.y) == 0) {
                b = true;
                return b;
            } else {
                if (s instanceof Road
                        &&  (roadEdge.getP1().distance(s.x, s.y) == 0
                        || roadEdge.getP2().distance(s.x, s.y) == 0)) {
                    //System.out.println("d from closest road in true " + roadEdge.ptSegDist(s.x, s.y));
                    b = true;
                    return b;
                } else {
                    System.out.println("d from closest road in false " + roadEdge.getP1().distance(s.x, s.y));
                    System.out.println("d from closest road in false " + roadEdge.getP2().distance(s.x, s.y));
                    b = false;
                }
            }
        }
        return b;
    }

}
