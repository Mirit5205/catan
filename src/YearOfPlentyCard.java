public class YearOfPlentyCard extends DevelopmentCard {

    private String firstResource;
    private String secondResource;

    public YearOfPlentyCard(String t, Player owner, GameState g) {
        super(t, owner, g);
    }

    @Override
    public void useAbility() {

    }

    public void setDesireResources(String firstResource, String secondResource) {
        this.firstResource = firstResource;
        this.secondResource = secondResource;
    }
}
