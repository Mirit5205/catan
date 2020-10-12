import java.awt.*;

public abstract class State {

    private static State currentState = null;
    protected Game game;

    public State(Game g) {
        this.game = g;
    }
    public static void setState(State state) {
        currentState = state;
    }

    public static State getState() {
        return currentState;
    }

    public abstract void tick();

    public abstract void render(Graphics g);

    public abstract void setBoard(Board b);
}
