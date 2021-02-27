/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake.persistence;

/**
 *
 * @author numip
 */
public class HighScore {
    public String name;
    public int score;

    public HighScore(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "HighScore{" + "name=" + name + ", score=" + score + '}';
    }
    
}
