import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Menu extends State {

    private int numOfPlayers;
    private List<Point> bufferList = new ArrayList<>();

    private Button exitButton;
    private Button startButton;
    private Button singlePlayerButton;



    private static int START_BUTTON_X = 800;
    private static int START_BUTTON_Y = 200;

    private static final int height = 100;

    public Menu(Game g) {
        super(g);
        startButton = new Button(new Point(START_BUTTON_X, START_BUTTON_Y), "Start");
        singlePlayerButton = new Button(new Point(START_BUTTON_X, START_BUTTON_Y + 2 * height), "1 Player");

        exitButton = new Button(new Point(START_BUTTON_X, START_BUTTON_Y + 4 * height  ), "Exit");


    }

    @Override
    public void tick() {
        bufferList.addAll(this.game.getMouseManager().DoubleClickScreenLocations);

        if (this.exitButton.IsPressed(bufferList)) {
            System.exit(0);
        }

        if (this.startButton.IsPressed(bufferList)) {
            this.game.setGameState(new GameState(this.game));
            State.setState(this.game.getGameState());
        }

        if (this.singlePlayerButton.IsPressed(bufferList)) {
            this.numOfPlayers = 1;
        }


    }

    @Override
    public void render(Graphics g) {
        /*g.setColor(Color.BLUE);
        g.fillRect(this.startPoint.x, this.startPoint.y, width, height);
        g.setColor(Color.white);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
        g.drawString("Start Game", this.startPoint.x + 28,
                this.startPoint.y + height / 2);


         */
        this.startButton.render(g);
        this.singlePlayerButton.render(g);
        this.exitButton.render(g);
    }

    @Override
    public void setBoard(Board b) {

    }


}
