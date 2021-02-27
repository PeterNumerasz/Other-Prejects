/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake.view;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import javax.swing.AbstractAction;
import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import snake.model.GameLevel;
import java.util.Random;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author numip
 */
public class MainWindow extends JFrame implements KeyListener {

    private Timer timer;
    private Board board;
    static int direction = new Random().nextInt(4) + 1;
    int row, col;
    int score = 0;
    GameLevel gamelevel;
    boolean stepped = false;
    public MainWindow(int r, int c) throws IOException {
        super();
        row = r; col = c;
        gamelevel = new GameLevel("sajt", row, col, direction);
        addKeyListener(this);
        setTitle("Snake");
        setSize((col) * 32 + col * 2, (int) ((row + 1.4) * 32 + row * 2));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        URL url = MainWindow.class.getClassLoader().getResource("snake/res/snake.png");
        setIconImage(Toolkit.getDefaultToolkit().getImage(url));

        JMenuBar menuBar = new JMenuBar();

        JMenu menuGame = new JMenu("Game");
        JMenuItem newGameLevel = new JMenuItem(new AbstractAction("New Game") {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                String[] asd = {""};
                main(asd);
            }
        });
        //JMenu menuGameScale = new JMenu("Highscores");

        JMenuItem menuHighScores = new JMenuItem(new AbstractAction("Highscores") {
            @Override
            public void actionPerformed(ActionEvent e) {
                new HighScoreWindow(gamelevel.getHighScores(), MainWindow.this);
            }
        });
        
        JMenuItem menuGameExit = new JMenuItem(new AbstractAction("Exit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        //menuGame.add(menuGameLevel);
        //menuGame.addSeparator();
        menuGame.add(newGameLevel);
        menuGame.add(menuHighScores);
        menuGame.addSeparator();
        menuGame.add(menuGameExit);
        menuBar.add(menuGame);
        setJMenuBar(menuBar);
        try { add(board = new Board(gamelevel), BorderLayout.CENTER); } catch (IOException ex) {}
        setLayout(new BorderLayout(0, 10));
        //setResizable(false);
        setLocationRelativeTo(null);
        //pack();
        //refreshGameStatLabel();
        setVisible(true);
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_RIGHT:
                if(direction != 4 && !stepped) {
                    direction = 3;
                    stepped = true;
                }
                break;
            case KeyEvent.VK_LEFT:
                if(direction != 3 && !stepped) {
                    direction = 4;
                    stepped = true;
                }
                break;
            case KeyEvent.VK_DOWN:
                if(direction != 1 && !stepped) {
                    direction = 2;
                    stepped = true;
                }
                break;
            case KeyEvent.VK_UP:
                if(direction != 2 && !stepped) {
                    direction = 1;
                    stepped = true;
                }
                break;
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        
    }
    public static void setDirection(int dir) {
        direction = dir;
    }
    public static void main(String[] args) {
        int row = 25;
        int col = 25;
        
        
        try {
            MainWindow window = new MainWindow(row, col);
            window.timer = new Timer(666, new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    window.stepped = false;
                    if(window.gamelevel.moveSnake(direction)) {
                        window.timer.stop();
                    }
                    else {
                        window.score++;
                        window.board.refresh();
                        System.out.println(window.gamelevel);
                    }
                }

            });
            window.timer.start();
        } 
        catch (IOException ex) {
        }
        
    }
}
