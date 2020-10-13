public class VictoryPointCard extends DevelopmentCard {

    public VictoryPointCard(String t, Player owner, GameState g) {
        super(t, owner, g);
    }

    @Override
    public void useAbility() {
        {
            this.owner.setScores(this.owner.getScores() + 1);
        }
    }
}
