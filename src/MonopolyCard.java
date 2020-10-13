import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MonopolyCard extends DevelopmentCard {

    private String monopolyResource;

    public MonopolyCard(String t, Player owner, GameState g) {
        super(t, owner, g);
    }

    @Override
    public void useAbility() {
        List<Resource> buffer;
        for (int i = 0; i < this.gamePlayers.length; i++) {

            //if its the owner of the card, continue in the loop
            if (gamePlayers[i].getPlayerName().equals(this.owner.getPlayerName())) {
                continue;
            }
            //buffer = gamePlayers[i].getResourcesList();

            //if the monopoly resource appear remove it from the
            //this resource list and add it to the owner list
            //for (Resource r : buffer ) {
             //  if (r.getResourceType().equals(this.monopolyResource)) {
              //     this.owner.getResourcesList().add(r);
              //     gamePlayers[ i].getResourcesList().remove(r);
              // }
            //}
        }
    }

    /**
     * set the resource you want to monopoly.
     * @param s is the resource represent by string.
     */
    public void setMonopolyResource(String s) {
        this.monopolyResource = s;
    }
}
