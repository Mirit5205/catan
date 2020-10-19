import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Menu extends State {

    private boolean isStageEnd = false;
    private int numOfPlayers;
    private List<Point> bufferList = new ArrayList<>();

    private Button exitButton;
    private Button startButton;
    private Button singlePlayerButton;
    private Button twoPlayersButton;




    private static int START_BUTTON_X = 800;
    private static int START_BUTTON_Y = 200;

    private static final int height = 100;

    public Menu(Game g) {
        super(g);
        /*
        startButton = new Button(new Point(START_BUTTON_X, START_BUTTON_Y), "Start");
        singlePlayerButton = new Button(new Point(START_BUTTON_X, START_BUTTON_Y + 2 * height), "1 Player");
        twoPlayersButton = new Button(new Point(START_BUTTON_X, START_BUTTON_Y + 4 * height), "2 Player");
        exitButton = new Button(new Point(START_BUTTON_X, START_BUTTON_Y + 6 * height  ), "Exit");
         */

    }

    @Override
    public void tick() {
        bufferList.addAll(this.game.getMouseManager().DoubleClickScreenLocations);

        if (this.exitButton.isPressed(bufferList)) {
            System.exit(0);
        }

        if (this.startButton.isPressed(bufferList)) {
            if (numOfPlayers != 0) {
               /* isStageEnd = true;
                if (isStageEnd) {
                    this.game.clearGameCanvas();
                }

                */
                this.game.getMouseManager().DoubleClickScreenLocations.clear();
                GameState game = new GameState(this.game);
                game.setNumOfPlayers(this.numOfPlayers);
                game.initPlayersArr();
                game.initListOfFirstTurnsByOrder();
                this.game.setGameState(game);
                State.setState(this.game.getGameState());
            }
        }

        if (this.singlePlayerButton.isPressed(bufferList)) {
            this.numOfPlayers = 1;
        }

        if (this.twoPlayersButton.isPressed(bufferList)) {
            this.numOfPlayers = 2;
        }


    }

    @Override
    public void render(Graphics g) {

        this.startButton.render(g);
        this.singlePlayerButton.render(g);
        this.twoPlayersButton.render(g);
        this.exitButton.render(g);


    }

    @Override
    public void setBoard(Board b) {

    }

}
