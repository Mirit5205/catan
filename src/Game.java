import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game implements Runnable {
    private Display display;
    private String title;
    private int width, height;
    private Board board;

    private Thread thread;
    private boolean running = false;

    private BufferStrategy bs;
    private Graphics g;

    //states
    private State gameState;

    private MouseManager mouseManager;

    public Game(String s, int w, int h) {
        this.title = s;
        this.width = w;
        this.height = h;
        this.board = new Board();
        this.mouseManager = new MouseManager();

    }

    private void init() {
        this.display = new Display(this.title, this.width, this.height);
        //this.display.getFrame().addMouseListener(this.mouseManager);
        this.display.getCanvas().addMouseListener(this.mouseManager);
        this.display.getCanvas().addMouseMotionListener(this.mouseManager);
        this.board.initBoardTiles();
        this.gameState = new Menu(this);
        State.setState(gameState);
        //this.gameState = new GameState(this);
        //State.setState(gameState);
    }

    private void tick() {

        //this.mouseManager.tick();
        if (State.getState() != null) {
            State.getState().tick();
        }

    }

    private void render() {
        this.bs = this.display.getCanvas().getBufferStrategy();
        if (this.bs == null) {
            this.display.getCanvas().createBufferStrategy(3);
            return;
        }
        //this.g = this.display.getCanvas().getGraphics();
        this.g = this.bs.getDrawGraphics();

        if (State.getState() != null) {
            State.getState().setBoard(this.board);
            State.getState().render(this.g);
        }

        this.bs.show();
        this.g.dispose();
    }

    @Override
    public void run() {
        init();

        int fps = 60;
        double timePerTick = 1000000000 / fps;
        double delta = 0;
        long now;
        long lastTime = System.nanoTime();
        long timer = 0;
        int ticks = 0;

        while (running) {
            now = System.nanoTime();
            delta += (now - lastTime) / timePerTick;
            timer += now -lastTime;
            lastTime = now;
            if (delta >= 1) {
                tick();
                render();
                ticks++;
                delta--;
            }

            if (timer >= 1000000000) {
                ticks = 0;
                timer = 0;
            }
        }

        stop();
    }

    public synchronized void start(){
        if (this.running == true) {
            return;
        }
        this.running = true;
        this.thread = new Thread(this);
        this.thread.start();

    }

    public synchronized void stop(){
        if (!this.running) {
            return;
        }
        running = false;
        try {
            this.thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public Board getBoard() {
        return this.board;
    }

    public MouseManager getMouseManager() {
        return this.mouseManager;
    }

    public void setGameState(State gameState) {
        this.gameState = gameState;
    }

    public State getGameState() {
        return this.gameState;
    }

    public void clearGameCanvas() {
        this.g.clearRect(0, 0, 2000, 1200);

    }
}
