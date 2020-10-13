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
}
