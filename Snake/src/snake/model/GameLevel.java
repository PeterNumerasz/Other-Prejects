/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake.model;

import java.util.ArrayList;
import javax.swing.Timer;
import snake.persistence.Database;
import snake.persistence.HighScore;

/**
 *
 * @author numip
 */
public class GameLevel {
    private LevelItem[][] level;
    private int row;
    private int col;
    private int wallNum;
    private final Database database;
    private int[] snakeHeadCoord = new int[2];
    private int[] snakeTailCoord = new int[2];
    private ArrayList<ArrayList<Integer>> snakeBodyLenght = new ArrayList<ArrayList<Integer>>();
    //private int[] snakeTurnPoint = new int[2];
    private int[] foodCoord = new int[2];
    
    public GameLevel(String difficulty, int r, int c, int sd) {
        row = r;
        col = c;
        database = new Database();
        wallNum = getRandomNumber(2, 4);
        System.out.println("Falak száma : " + wallNum);
        level = new LevelItem[row][col];
        this.initLevel();
        for(int i = 0; i < wallNum; i++) {
            boolean horizontal = false, curved = false, right = false;
            
            int index = getRandomNumber(1, level.length - 1);
            System.out.println("Index : " + index);
            
            int start = getRandomNumber(1, level.length - 1 / 2);
            System.out.println("Start : " + start);
            
            int end = getRandomNumber(level.length - 1 / 2, level.length - 1);
            System.out.println("End : " + end);
            
            if(getRandomNumber(1, 3) > getRandomNumber(1, 3)) 
                horizontal = true;
            if(getRandomNumber(1, 3) > getRandomNumber(1, 3)) {
                curved = true;
                if(getRandomNumber(1, 3) > getRandomNumber(1, 3)) 
                    right = true;
            }
            placeWalls(index, start, end, horizontal, curved, right);
        }
        initSnake(sd);
        placeFood();
        System.out.print("    0\t   1\t   2\t   3\t   4\t   5\t   6\t   7\t   8\t   9\t   10\t   11\t   12\t   13\t   14\t   15\t   16\t   17\t   18\t   19\n");
        for(int i = 0; i < row; i++) {
            System.out.print(i);
            for(int j = 0; j < col; j++) {
                if(level[i][j].name() == "EMPTY") {
                    System.out.print("     \t");
                }
                else System.out.print(" " + level[i][j].name() + "\t");
            }
            System.out.println("\t");
        }
    }
    public ArrayList<HighScore> getHighScores() {
        return database.getHighScores();
    }
    private void initLevel() {
        for(int i = 0; i < row; i++) {
            for(int j = 0; j < col; j++) {
                this.level[i][j] = LevelItem.EMPTY;
            }
        }
    }
    /**
     * 
     * @param index tells the method which row or column we are at
     * @param from tells the method which index we start the wall at
     * @param to tell the method which index we stop the wall at
     * @param horizontal tells the method if it's a horizontal or vertical line
     * @param curved tells the method if it's curved or not, if not curved we call the other placeWalls
     * @param right tells the method which way is it curved, not used if curved is false
     */
    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }
    private void placeWalls(int index, int from, int to, boolean horizontal, boolean curved, boolean right) {
        int wallLength = to - from;
        if(curved) {
            int curvePoint = getRandomNumber(from, to);
            System.out.println("Hajlítási pont : " + curvePoint);
            if(horizontal) {
                if(right) {
                    System.out.println("-- Jobbra kanyarodó horizontális L --");
                    for(int i = from; i <= curvePoint; i++) {
                        //System.out.println("egyenes ciklus");
                        level[index][i] = LevelItem.WALL;
                    }
                    //System.out.println("Belépés a kanyarodós ciklusba, --- " + (index + (wallLength - curvePoint)));
                    for(int j = index + 1; j <= index + wallLength - curvePoint; j++) {
                        //System.out.println("kanyarodós ciklus");
                        level[j][curvePoint] = LevelItem.WALL;
                    }
                }
                else {
                    System.out.println("-- Balra kanyarodó horizontális L --");
                    for(int i = from; i <= curvePoint; i++) {
                        //System.out.println("egyenes ciklus");
                        level[index][i] = LevelItem.WALL;
                    }
                    for(int j = index - wallLength + curvePoint; j <= index; j++) {
                        //System.out.println("kanyarodós ciklus");
                        level[j][curvePoint] = LevelItem.WALL;
                    }
                }
            }
            else {
                if(right) {
                    System.out.println("-- Jobbra kanyarodó vertikális L --");
                    for(int i = from; i <= curvePoint; i++) {
                        //System.out.println("egyenes ciklus");
                        level[i][index] = LevelItem.WALL;
                    }
                    for(int j = 1; j <= index + wallLength - curvePoint && index + j < level.length; j++) {
                        level[curvePoint][index + j] = LevelItem.WALL;
                    }
                }
                else {
                    System.out.println("-- Balra kanyarodó vertikális L --");
                    for(int i = from; i <= curvePoint; i++) {
                        //System.out.println("egyenes ciklus");
                        level[i][index] = LevelItem.WALL;
                    }
                    for(int j = 1; j <= index + wallLength - curvePoint && index - j > 0; j++) {
                        level[curvePoint][index - j] = LevelItem.WALL;
                    }
                }
            }
            
        }
        else {
            placeWalls(index, from, to, horizontal);
        }
    }
    private void eat(int x, int y) {
        placeFood();
        snakeBodyLenght.add(new ArrayList<Integer>());
        snakeBodyLenght.get(snakeBodyLenght.size() - 1).add(x);
        snakeBodyLenght.get(snakeBodyLenght.size() - 1).add(y);
        level[x][y] = LevelItem.S_B;
    }
    private void placeWalls(int index, int from, int to, boolean horizontal) {
        if(horizontal) {
            System.out.println("-- Horizontális egyenes --");
            for(int i = from; i <= to; i++) {
                level[index][i] = LevelItem.WALL;
            }
        }
        else {
            System.out.println("-- Vertikális egyenes --");
            for(int i = from; i <= to; i++) {
                level[i][index] = LevelItem.WALL;
            }
        }
    }
    private void initSnake(int direction) {
        snakeTailCoord[0] = row / 2;
        snakeTailCoord[1] = col / 2;
        switch(direction) {
            case 1: // Head up
                level[row / 2][col / 2 + 1] = LevelItem.S_H;
                level[row / 2][col / 2] = LevelItem.S_T;
                snakeHeadCoord[0] = row / 2;
                snakeHeadCoord[1] = col / 2 + 1;
                break;
            case 2: // Head down
                level[row / 2][col / 2 - 1] = LevelItem.S_H;
                level[row / 2][col / 2] = LevelItem.S_T;
                snakeHeadCoord[0] = row / 2;
                snakeHeadCoord[1] = col / 2 - 1;
                break;
            case 3: // Head right
                level[row / 2 + 1][col / 2] = LevelItem.S_H;
                level[row / 2][col / 2] = LevelItem.S_T;
                snakeHeadCoord[0] = row / 2 + 1;
                snakeHeadCoord[1] = col / 2;
                break;
            case 4: // Head left
                level[row / 2 - 1][col / 2] = LevelItem.S_H;
                level[row / 2][col / 2] = LevelItem.S_T;
                snakeHeadCoord[0] = row / 2 - 1;
                snakeHeadCoord[1] = col / 2;
                break;
        }
        
    }
    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
    public String getPosition(int x, int y) {
        return level[x][y].name();
    }
    public void placeFood() {
        int x, y;
        do {
            x = getRandomNumber(1, row);
            y = getRandomNumber(1, col);
        }
        while(getPosition(x, y) != "EMPTY");
        level[x][y] = LevelItem.FOOD;
    }
    // TODO:    fel kell jegyezni melyik ponton fordultunk a kígyóval
    //          kígyót csak direction írányába mozgatjuk folyton
    //          kígyó feje és farka közé body kell kerüljön
    //          ne tudjon "magába fordulni", azaz ne lehessen azt, hogyha elindultunk felfelé akkor utána egyből lefele forduljon, ugyanez vízszintesen
    //          magyarul normálisan forduljon, legalább egy egységgel menjen el balra vagy jobbra, illetve fel vagy le és utána tudjon az ellenkező írányba fordulni
    public boolean moveSnake(int direction/*, int turnPoint*/) {
        // 1 - UP, 2 - DOWN, 3 - RIGHT, 4 - LEFT
        int tmpX = snakeHeadCoord[0];
        int tmpY = snakeHeadCoord[1];
        level[snakeTailCoord[0]][snakeTailCoord[1]] = LevelItem.EMPTY;
        switch(direction) {
            case 1: // moves up
                snakeHeadCoord[0]--;
                break;
            case 2: // moves down
                snakeHeadCoord[0]++;
                break;
            case 3: // moves right
                snakeHeadCoord[1]++;
                break;
            case 4: // moves left
                snakeHeadCoord[1]--;
                break;
        }
        if( snakeHeadCoord[0] < 0 || snakeHeadCoord[1] < 0 ||
            snakeHeadCoord[0] > col - 1 || snakeHeadCoord[1] > row - 1
            ) {
            
            return true;
        }
        else if( level[snakeHeadCoord[0]][snakeHeadCoord[1]] == LevelItem.S_B ||
                level[snakeHeadCoord[0]][snakeHeadCoord[1]] == LevelItem.S_T ||
                level[snakeHeadCoord[0]][snakeHeadCoord[1]] == LevelItem.WALL) {
                return true;
            }
        
        if(level[snakeHeadCoord[0]][snakeHeadCoord[1]] == LevelItem.FOOD) {
            eat(tmpX, tmpY);
            for(int i = snakeBodyLenght.size() - 1; i >= 0; i--) {
                    if(i == 0) {
                        //System.out.println("i = " + i);
                        snakeBodyLenght.get(i).set(0, tmpX);
                        snakeBodyLenght.get(i).set(1, tmpY);
                    }
                    else {
                        snakeBodyLenght.get(i).set(0, snakeBodyLenght.get(i - 1).get(0));
                        snakeBodyLenght.get(i).set(1, snakeBodyLenght.get(i - 1).get(1));
                    }
                    //System.out.println("i = " + i + ", y = " + snakeBodyLenght.get(i).get(0) + ", x = " + snakeBodyLenght.get(i).get(1));
                    level[snakeBodyLenght.get(i).get(0)][snakeBodyLenght.get(i).get(1)] = LevelItem.S_B;
                }
        }
        else {
            if(snakeBodyLenght.isEmpty()) {
                snakeTailCoord[0] = tmpX;
                snakeTailCoord[1] = tmpY;
                level[tmpX][tmpY] = LevelItem.S_T;
            }
            else {
                snakeTailCoord[0] = snakeBodyLenght.get(snakeBodyLenght.size() - 1).get(0);
                snakeTailCoord[1] = snakeBodyLenght.get(snakeBodyLenght.size() - 1).get(1);
                for(int i = snakeBodyLenght.size() - 1; i >= 0; i--) {
                    if(i == 0) {
                        //System.out.println("i = " + i);
                        snakeBodyLenght.get(i).set(0, tmpX);
                        snakeBodyLenght.get(i).set(1, tmpY);
                    }
                    else {
                        snakeBodyLenght.get(i).set(0, snakeBodyLenght.get(i - 1).get(0));
                        snakeBodyLenght.get(i).set(1, snakeBodyLenght.get(i - 1).get(1));
                    }
                    //System.out.println("i = " + i + ", y = " + snakeBodyLenght.get(i).get(0) + ", x = " + snakeBodyLenght.get(i).get(1));
                    level[snakeBodyLenght.get(i).get(0)][snakeBodyLenght.get(i).get(1)] = LevelItem.S_B;
                }
            }
        }
        
        level[snakeHeadCoord[0]][snakeHeadCoord[1]] = LevelItem.S_H;
        level[snakeTailCoord[0]][snakeTailCoord[1]] = LevelItem.S_T;
        return false;
    }
    
    @Override
    public String toString() {
        String printed = "    0\t   1\t   2\t   3\t   4\t   5\t   6\t   7\t   8\t   9\t   10\t   11\t   12\t   13\t   14\t   15\t   16\t   17\t   18\t   19\n";
        for(int i = 0; i < row; i++) {
            printed += i;
            for(int j = 0; j < col; j++) {
                if(level[i][j].name() == "EMPTY") {
                    printed += "     \t";
                }
                else {
                    printed += " " + level[i][j].name() + "\t";
                }
            }
            printed = printed.substring(0, printed.length()-1);
            printed += "\n";
        }
        return printed;
    }
    
}
