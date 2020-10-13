public abstract class DevelopmentCard {
    protected String type;
    protected Player owner;
    protected Player[] gamePlayers;

    public DevelopmentCard(String t, Player owner, GameState g ) {
        this.type = t;
        this.owner = owner;
        this.gamePlayers = g.getGamePlayers();
    }

    public abstract void useAbility();
}
