import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<Resource>  resources = new ArrayList<>();
    private List<Settlement> settlements = new ArrayList<>();
    private List<Road> roads = new ArrayList<>();
    private List<City> cities = new ArrayList<>();
    private int Point;

    public List<Settlement> getSettlementsList() {
        return this.settlements;
    }

    public List<Resource> getResourcesList() {
        return this.resources;
    }

}
