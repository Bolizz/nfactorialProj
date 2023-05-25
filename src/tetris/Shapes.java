package tetris;

import java.awt.*;

import static tetris.Board.B_H;
import static tetris.Board.B_S;

public class Shapes {
    private int x = 4, y = 0;
    private int normal = 600;
    private int fast = 50;
    private int delayTime = normal;
    private long beginTime;

    private int deltaX = 0;
    private boolean coll = false;
    private boolean collX = false;
    long deltaTime;
    long lastTime = 0;
    long time = 0;

    private int[][] coords;
    private Board board;
    private Color color;
    private int timePassedFromCollision = -1;
    public Shapes(int [][] coords, Board board, Color color){
        this.coords = coords;
        this.board = board;
        this.color = color;
        deltaX = 0;
        x = 4;
        y = 0;
        delayTime = normal;
        time = 0;
        lastTime = System.currentTimeMillis();
    }
    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }
    public void changes(){
        collX = false;
        deltaTime = System.currentTimeMillis() - lastTime;
        time += deltaTime;
        lastTime = System.currentTimeMillis();
        if(coll && timePassedFromCollision > 500){
            for (int i = 0; i <coords.length; i++) {
                for(int j = 0; j < coords[0].length; j++){
                    if(coords[i][j] != 0){
                        board.getBoard()[y + i][x+j] = color;
                    }
                }
            }
            checkLine();
            board.addScore();
            board.setCurrentShape();
            timePassedFromCollision =-1;
            return;
        }

        if(!(x+deltaX+coords[0].length > 10) && !(x+deltaX < 0)){
            for(int row =0; row < coords.length; row ++){
                for (int col = 0; col < coords[row].length; col++) {
                    if(coords[row][col] != 0){
                        if(board.getBoard()[y + row][x+deltaX+col] != null){
                            collX = true;
                        }
                    }
                }
            }
            if(!collX){
                x+=deltaX;
            }
        }
        if (timePassedFromCollision == -1) {
            if (!(y + 1 + coords.length > 20)) {

                for (int row = 0; row < coords.length; row++) {
                    for (int col = 0; col < coords[row].length; col++) {
                        if (coords[row][col] != 0) {

                            if (board.getBoard()[y + 1 + row][x + col] != null) {
                                coll();
                            }
                        }
                    }
                }
                if (time > delayTime) {
                    y++;
                    time = 0;
                }
            } else {
                coll();
            }
        } else {
            timePassedFromCollision += deltaTime;
        }

        deltaX = 0;
    }
    private void coll(){
        coll = true;
        timePassedFromCollision = 0;
    }
    private void checkLine(){
        int line = board.getBoard().length - 1;
        for (int topLine = line; topLine >0 ; topLine--) {
            int count = 0;
            for(int col = 0; col < board.getBoard()[0].length; col++){
                if(board.getBoard()[topLine][col]!=null){
                    count++;
                }
                board.getBoard()[line][col] = board.getBoard()[topLine][col];
            }
            if(count < board.getBoard()[0].length){
                line--;
            }
        }
    }
    public void rotate(){
        int[][] finalShape = transpose(coords);
        finalShape = reverseRows(finalShape);
        if((x + finalShape[0].length > board.B_W) || y + finalShape.length > 20){
            return;
        }
        for(int row= 0; row<finalShape.length; row++){
            for (int col = 0; col < finalShape[0].length; col++) {
                if(finalShape[row][col] != 0){
                    if(board.getBoard()[row + y][x + col] !=null){
                        return;
                    }
                }
            }
        }
        coords = finalShape;
    }
    private int[][] transpose(int[][] mat){
        int[][] temp = new int[mat[0].length][mat.length];
        for (int row = 0; row < mat.length; row++) {
            for (int col = 0; col < mat[0].length; col++) {
                temp[col][row] = mat[row][col];
            }
        }
        return temp;
    }
    private int[][] reverseRows(int [][] mat){
        int mid = mat.length/2;
        for (int i = 0; i < mid; i++) {
            int[] temp = mat[i];
            mat[i] = mat[mat.length - i - 1];
            mat[mat.length - i - 1] = temp;
        }
        return mat;
    }
    public void render(Graphics g){
        g.setColor(color);
        for (int i = 0; i <coords.length ; i++) {
            for (int j = 0; j < coords[0].length; j++) {
                if(coords[i][j] != 0){
                    g.fillRect(j*B_S + x * B_S, i* B_S+y*B_S,B_S,B_S);
                }

            }
        }
    }
    public void speedUp(){
        delayTime = fast;
    }
    public void setNormal(){
        delayTime = normal;
    }
    public void moveLeft(){
        deltaX = -1;
    }
    public void moveRight(){
        deltaX = 1;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public int[][] getCoords(){
        return coords;
    }
    public Color getColor(){
        return color;
    }
}
