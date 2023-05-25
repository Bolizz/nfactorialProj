package tetris;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class tetris extends JPanel{
    public static final int WIDTH = 450, HEIGHT = 636;

    private JFrame window;
    private Board board;
    private Intro intro;
    public tetris() {
        window = new JFrame("Tetris");
        window.setSize(WIDTH, HEIGHT);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        board = new Board();
        intro = new Intro(this);


        window.addKeyListener(board);
        window.addKeyListener(intro);

        window.add(intro);

        window.setVisible(true);


    }
        public void startTetris() {
            window.remove(intro);
            window.add(board);
            board.startGame();
            window.revalidate();
        }
    public static void main(String[] args){
        new tetris();
    }
}
