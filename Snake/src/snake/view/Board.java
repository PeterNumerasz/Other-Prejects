/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import javax.swing.JPanel;
import snake.model.GameLevel;
import snake.res.ResourceLoader;

/**
 *
 * @author numip
 */
public class Board extends JPanel {
    private GameLevel game;
    private final Image head, body, tail, food, wall, empty;
    private double scale;
    private int scaled_size;
    private final int tile_size = 33;
    
    public Board(GameLevel g) throws IOException{
        game = g;
        scale = 1.0;
        scaled_size = (int)(scale * tile_size);
        head = ResourceLoader.loadImage("snake/res/snake_head.png");
        body = ResourceLoader.loadImage("snake/res/snake_body.png");
        tail = ResourceLoader.loadImage("snake/res/snake_tail.png");
        food = ResourceLoader.loadImage("snake/res/food.png");
        wall = ResourceLoader.loadImage("snake/res/wall.png");
        empty = ResourceLoader.loadImage("snake/res/sand.jpg");
    }
    
    public boolean setScale(double scale){
        this.scale = scale;
        scaled_size = (int)(scale * tile_size);
        return refresh();
    }
    
    public boolean refresh(){
        //if (!game.isLevelLoaded()) return false;
        Dimension dim = new Dimension(game.getCol() * scaled_size, game.getRow() * scaled_size);
        setPreferredSize(dim);
        setMaximumSize(dim);
        setSize(dim);
        repaint();
        return true;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        //if (!game.isLevelLoaded()) return;
        Graphics2D gr = (Graphics2D)g;
        int w = game.getCol();
        int h = game.getRow();
        //String p = game.getPlayerPos();
        for (int y = 0; y < h; y++){
            for (int x = 0; x < w; x++){
                Image img = null;
                String li = game.getPosition(y, x);
                switch (li){
                    case "S_H": img = head; break;
                    case "S_B": img = body; break;
                    case "S_T": img = tail; break;
                    case "WALL": img = wall; break;
                    case "FOOD": img = food; break;
                    case "EMPTY": img = empty; break;
                }
                //if (p.x == x && p.y == y) img = player;
                //if (img == null) continue;
                gr.drawImage(img, x * scaled_size, y * scaled_size, scaled_size, scaled_size, null);
            }
        }
    }
    
}
