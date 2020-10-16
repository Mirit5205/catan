import java.awt.*;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class Player {
    private Color c;
    private String playerName;
    private List<String>  resources = new ArrayList<>();
    private List<Settlement> settlements = new ArrayList<>();
    private List<Road> roads = new ArrayList<>();
    private List<City> cities = new ArrayList<>();
    private List<String> developmentCardsList = new ArrayList<>();
    private int scores;

    public Player(Color c) {
        this.c = c;
    }

    public List<Settlement> getSettlementsList() {
        return this.settlements;
    }

    public List<String> getResourcesList() {
        return this.resources;
    }

    public List<City> getCitiesList() {
        return this.cities;
    }

    public List<String> getDevelopmentCardsList() {
        return this.developmentCardsList;
    }


    public Color getColor() {
        return this.c;
    }

    public int getScores() {
        return this.scores;
    }

    public void setScores(int newScores) {
        this.scores = newScores;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public List<Road> getRoadsList() {
        return roads;
    }

    public void setPlayerName(String name) {
        this.playerName = name;
    }

    public void updateScores() {
        this.scores = this.settlements.size()
                + this.getCitiesList().size() * 2;

    }

    public Settlement getSettlementByPoint(Point settlementPoint) {
        Settlement settlement = null;
        for (Settlement s : settlements) {
            if (s.getStartPoint().equals(settlementPoint)) {
                settlement = s;
            }
        }
        return settlement;
    }

    public Settlement getClosestSettlementByPoint(Point settlementPoint) {
        Settlement settlement = null;
        for (Settlement s : settlements) {
            if (s.getStartPoint().distance(settlementPoint.x, settlementPoint.y) < 15) {
                settlement = s;
            }
        }
        return settlement;
    }

}
