package tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Intro extends JPanel implements KeyListener {
    private static final long serialVersionUID = 1L;

    private tetris window;
    private Timer timer;


    public Intro(tetris window){
        timer = new Timer(1000/60, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }

        });
        timer.start();
        this.window = window;



    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        g.setColor(Color.BLACK);

        g.fillRect(0, 0, tetris.WIDTH, tetris.HEIGHT);

        g.setColor(Color.WHITE);
        g.drawString("Press enter to play!", 150, tetris.HEIGHT / 2 + 100);


    }

    @Override
    public void keyTyped(KeyEvent e) {
        if(e.getKeyChar() == KeyEvent.VK_ENTER) {
            window.startTetris();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
