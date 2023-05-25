package tetris;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.beans.SimpleBeanInfo;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Board extends JPanel implements KeyListener {

    public boolean statePause = false;
    public boolean stateOver = false;

    private BufferedImage pause, refresh;
    private static int FPS = 60;
    private static int delay = FPS / 1000;
    private boolean gameStart = false;
    public static final int B_W = 10;
    public static final int B_H = 20;
    public static final int B_S = 30;
    private Timer looper;
    private Color[][] board = new Color[B_H][B_W];
    private Color[] colors = {Color.cyan, Color.green,Color.pink,Color.yellow,Color.blue,Color.red,Color.orange};
    private Random random;
    private Shapes[] shapes = new Shapes[7];
    private Shapes currentShape, nextShape;
    private int score = 0;
    private Timer buttonLapse = new Timer(300, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            buttonLapse.stop();
        }
    });
    public Board(){
        random = new Random();
        shapes[0] = new Shapes(new int[][]{
                {1,0},
                {1,0},
                {1,1}
        }, this, colors[0]);
        shapes[1] = new Shapes(new int[][]{
                {0,1},
                {0,1},
                {1,1}
        }, this, colors[1]);
        shapes[2] = new Shapes(new int[][]{
                {1,1,1,1}
        }, this, colors[2]);
        shapes[3] = new Shapes(new int[][]{
                {1,1,0},
                {0,1,1}
        }, this, colors[3]);
        shapes[4] = new Shapes(new int[][]{
                {0,1,1},
                {1,1,0}
        }, this, colors[4]);
        shapes[5] = new Shapes(new int[][]{
                {1,1},
                {1,1}
        }, this, colors[5]);
        shapes[6] = new Shapes(new int[][]{
                {1,1,1},
                {0,1,0}
        }, this, colors[6]);
        looper = new Timer(delay, new GameLooper());
    }

    private void changes(){
        if(!stateOver && !buttonLapse.isRunning() && gameStart){
            buttonLapse.start();
            //statePause = !statePause;
        }
        if(gameStart){
            startGame();
        }
        if(statePause || stateOver){
            return;
        }
        currentShape.changes();
    }
    public void setNextShape(){
        int i = random.nextInt(shapes.length);
        nextShape = new Shapes(shapes[i].getCoords(),this,colors[i]);
    }
    public void setCurrentShape(){
        currentShape = shapes[random.nextInt(shapes.length)];
        setNextShape();
        for (int row = 0; row < currentShape.getCoords().length; row++) {
            for (int col = 0; col < currentShape.getCoords()[0].length; col++) {
                if(currentShape.getCoords()[row][col] != 0){
                    if(board[row + currentShape.getY()][currentShape.getX()+col]!= null){
                        stateOver = true;
                    }
                }
            }
        }

    }


    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setFont(new Font("FiraMono-Medium",Font.PLAIN,50));
        g.setColor(Color.black);
        g.fillRect(0,0,getWidth(),getHeight());


        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if(board[row][col] != null){
                    g.setColor(board[row][col]);
                    g.fillRect(col * B_S, row * B_S, B_S,B_S);
                }
            }
        }
        g.setColor(nextShape.getColor());
        for (int row = 0; row < nextShape.getCoords().length; row++) {
            for (int col = 0; col < nextShape.getCoords()[0].length; col++) {
                if (nextShape.getCoords()[row][col] != 0) {
                    g.fillRect(col * 30 + 320, row * 30 + 50, Board.B_S, Board.B_S);
                }
            }
        }
        currentShape.render(g);

        g.setColor(Color.white);
        for(int row = 0; row < B_H; row++){
            g.drawLine(0,B_S * row,B_S*B_W, B_S* row);
        }
        for(int col=0; col < B_W+1; col++){
            g.drawLine(col*B_S,0, col *B_S, B_S*B_H);
        }
        if( stateOver){
            g.setColor(Color.white);
            g.drawString("Game Over", 30, 210);
            g.drawString("Press SPACE" , 30, 260);
            g.setFont(new Font("Time Roman", Font.PLAIN, 20));
            g.drawString("to start a new game", 60, 280);
        }
        if(statePause){
            g.setColor(Color.white);
            g.drawString("Pause", 30, 210);
        }
        g.setFont(new Font("Time Roman", Font.PLAIN, 20));
        g.drawString("Score", 450 -125, 636/2);
        g.drawString(score + "", 450 -125, 636/2 + 30);

    }
    public Color[][] getBoard(){
        return board;
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            gameStart = true;
        }
        if(e.getKeyChar() == KeyEvent.VK_S || e.getKeyChar() == Character.toLowerCase(KeyEvent.VK_S)){
            currentShape.speedUp();
        }
        else if(e.getKeyChar() == KeyEvent.VK_D || e.getKeyChar() == Character.toLowerCase(KeyEvent.VK_D)){
            currentShape.moveRight();
        }else if(e.getKeyChar() == KeyEvent.VK_A || e.getKeyChar() == Character.toLowerCase(KeyEvent.VK_A)){
            currentShape.moveLeft();
        }else if(e.getKeyChar() == KeyEvent.VK_W || e.getKeyChar() == Character.toLowerCase(KeyEvent.VK_W)){
            currentShape.rotate();
        }

        if(stateOver){
            if(e.getKeyCode() == KeyEvent.VK_SPACE){
                for (int row = 0; row < board.length; row++) {
                    for (int col = 0; col < board[row].length; col++) {
                        board[row][col] = null;

                    }
                }
                setCurrentShape();
                stateOver = false;
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            statePause = !statePause;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        if(e.getKeyChar() == KeyEvent.VK_S || e.getKeyChar() == Character.toLowerCase(KeyEvent.VK_S)){
            currentShape.setNormal();
        }
    }
    public void startGame(){
        stopGame();
        setNextShape();
        setCurrentShape();
        stateOver = false;
        looper.start();
    }
    public void stopGame(){
        score = 0;
        for(int row = 0; row < board.length; row++){
            for(int col = 0; col < board[row].length; col++){
                board[row][col] = null;
            }
        }
        looper.stop();
    }
    protected void addScore(){
        score ++;
    }



    class GameLooper implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            changes();
            repaint();
        }

    }
}
