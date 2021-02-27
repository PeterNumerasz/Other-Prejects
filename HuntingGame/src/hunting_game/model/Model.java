/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hunting_game.model;

/**
 *
 * @author numip
 */
public class Model {

    private char currentPlayer = 'P';
    private int currentPlayerIndex = 0;
    private int currentGameRound = 1;
    private int maxRoundNumber;
    private int[] currentTile = {-1, -1}; 
    private Difficulty difficulty;
    private static int tableSize;
    private int[][] table;

    public Model(Difficulty difficulty){
        difficulty.name().toUpperCase();
        this.difficulty = difficulty;
        switch (difficulty.name()) {
            case "EASY":
                tableSize = 3;
                break;
            case "MEDIUM":
                tableSize = 5;
                break;
            case "HARD":
                tableSize = 7;
                break;
            default:
                tableSize = -1;
                break;
        }
        if(tableSize > 0)
            maxRoundNumber = 4 * tableSize;
            generateTable(tableSize);
    }
    public boolean buttonAction(int i, int j) {
        if(currentPlayer == 'P') {
            currentGameRound++;
            return preyAction(i, j);
        }
        else {
            currentGameRound++;
            return hunterAction(i, j);
        }
    }
    
    private boolean isAdjacent(int i, int j) {
        int currentPosition = currentTile[0] * tableSize + currentTile[1];
        if(     (i - 1) * tableSize + j == currentPosition ||
                (i + 1) * tableSize + j == currentPosition ||
                i * tableSize + (j - 1) == currentPosition ||
                i * tableSize + (j + 1) == currentPosition 
                
            ) {
            if(currentTile[1] == 0 && i * tableSize + (j + 1) == currentPosition) {
                return false;
            }
            if(currentTile[1] == tableSize - 1 && i * tableSize + (j - 1) == currentPosition) {
                return false;
            }
            for(int k = 0; k < 5; k++) {
                int tmpPos1 = table[k][0] * tableSize + table[k][1];
                int tmpPos2 = i * tableSize + j;
                if(tmpPos1 == tmpPos2)
                    return false;
            }
            return true;
        }
        return false;
    }
    
    private boolean preyAction(int i, int j) {
        if(isAdjacent(i, j)) {
            currentPlayer = 'H';
            table[0][0] = i;
            table[0][1] = j;
            return true;
        }
        return false;
    }
    
    private boolean hunterAction(int i, int j) {
        if(isAdjacent(i, j)) {
            currentPlayer = 'P';
            table[currentPlayerIndex][0] = i;
            table[currentPlayerIndex][1] = j;
            return true;
        }
        return false;
    }
    
    public boolean canSelect(int i, int j) {
        if(currentPlayer == 'P') {
            return table[0][0] == i && table[0][1] == j;
        }
        else {
            for(int k = 1; k < 5; k++) {
                if(table[k][0] == i && table[k][1] == j) {
                    return true;
                }
            }
            return false;
        }
    }
    
    private void generateTable(int size) {
        table = new int[5][2];
        table[0][0] = size/2;   // Prey Row Position
        table[0][1] = size/2;   // Prey Col Position
        table[1][0] = 0;        // First Hunter Row Position
        table[1][1] = 0;        // First Hunter Col Position
        table[2][0] = size-1;   // Second Hunter Row Position
        table[2][1] = 0;        // Second Hunter Col Position
        table[3][0] = size-1;   // Third Hunter Row Position
        table[3][1] = size-1;   // Third Hunter Col Position
        table[4][0] = 0;        // Fourth Hunter Row Position
        table[4][1] = size-1;   // Fourth Hunter Col Position
    }
    
    // Returns the size of the map
    public int getTableSize() {
        return tableSize;
    }
    
    // sets the selected tile with either hunter or prey
    public void setSelected(int i, int j) {
        currentTile[0] = i;
        currentTile[1] = j;
        for(int k = 0; k < 5; k++) {
            if(table[k][0] == i && table[k][1] == j) {
                currentPlayerIndex = k;
                return;
            }
        }
    }
    // returns the currently selected tile, used for later calculations
    public int getSelected() {
        if(currentTile[0] == -1)
            return 0;
        return currentTile[0] * tableSize + currentTile[1];
    }
    
    // returns the position of the choosen player
    public int getPosition(int player) {
        return table[player][0] * tableSize + table[player][1];
    }
    
    // condition, that when returns true indicates that the prey won
    public boolean preyWon() {
        return currentGameRound > maxRoundNumber;
    }
    
    // condition, that when returns true indicates that the hunters won 
    public boolean hunterWon() {
        int[][] preyPosition = new int[1][2];
        int huntersRequiredForWin = 0;
        preyPosition[0][0] = table[0][0];
        preyPosition[0][1] = table[0][1];
        
            // <editor-fold defaultstate="collapsed" desc="Bal felső sarok">
            if(preyPosition[0][0] == 0 && preyPosition[0][1] == 0) {
                huntersRequiredForWin = 2;
                //System.out.println("Bal felső sarok");
                for(int i=1; i<5; ++i){
                    if(table[i][0] == 0 && table[i][1] == 1) {
                        huntersRequiredForWin--;
                    }
                    if(table[i][0] == 1 && table[i][1] == 0) {
                        huntersRequiredForWin--;
                    }
                    if(huntersRequiredForWin == 0)
                        return true;
                    System.out.println(huntersRequiredForWin);
                }
            }
            // </editor-fold>
            
            // <editor-fold defaultstate="collapsed" desc="Bal alsó sarok">
            if(preyPosition[0][0] == tableSize-1 && preyPosition[0][1] == 0) {
                huntersRequiredForWin = 2;
                for(int i=1; i<5; ++i){
                    if(table[i][0] == tableSize-2 && table[i][1] == 0) {
                        huntersRequiredForWin--;
                    }
                    if(table[i][0] == tableSize-1 && table[i][1] == 1) {
                        huntersRequiredForWin--;
                    }
                    if(huntersRequiredForWin == 0)
                        return true;
                }
            }
            // </editor-fold>
            
            // <editor-fold defaultstate="collapsed" desc="Jobb alsó sarok">
            if(preyPosition[0][0] == tableSize-1 && preyPosition[0][1] == tableSize-1) {
                huntersRequiredForWin = 2;
                for(int i=1;i<5;++i ){
                    if(table[i][0] == tableSize-2 && table[i][1] == tableSize-1) {
                        huntersRequiredForWin--;
                    }
                    if(table[i][0] == tableSize-1 && table[i][1] == tableSize-2) {
                        huntersRequiredForWin--;
                    }
                    if(huntersRequiredForWin == 0)
                        return true;
                }
            }
            // </editor-fold>
            
            // <editor-fold defaultstate="collapsed" desc="Jobb felső sarok">
            if(preyPosition[0][0] == 0 && preyPosition[0][1] == tableSize-1) {
                huntersRequiredForWin = 2;
                for (int i=1; i<5; ++i){
                    if(table[i][0] == 0 && table[i][1] == tableSize-2) {
                        huntersRequiredForWin--;
                    }
                    if(table[i][0] == 1 && table[i][1] == tableSize-1) {
                        huntersRequiredForWin--;
                    }
                    if(huntersRequiredForWin == 0)
                        return true;
                }
            }
            // </editor-fold>
            
            // <editor-fold defaultstate="collapsed" desc="Bal szélén bárhol">
            if((preyPosition[0][0] > 0 && preyPosition[0][0] < tableSize-1) && 
                    preyPosition[0][1] == 0) {
                huntersRequiredForWin = 3;
                for(int i=1; i<5; ++i){
                    if(table[i][0] == preyPosition[0][0] - 1 && table[i][1] == 0) {
                        huntersRequiredForWin--;
                    }
                    if(table[i][0] == preyPosition[0][0] + 1 && table[i][1] == 0) {
                        huntersRequiredForWin--;
                    }
                    if(table[i][0] == preyPosition[0][0] && table[i][1] == 1) {
                        huntersRequiredForWin--;
                    }
                    if(huntersRequiredForWin == 0)
                        return true;
                }
            }
            // </editor-fold>
            
            // <editor-fold defaultstate="collapsed" desc="Jobb szélén bárhol">
            if((preyPosition[0][0] > 0 && preyPosition[0][0] < tableSize-1) && 
                    preyPosition[0][1] == tableSize-1) {
                huntersRequiredForWin = 3;
                for(int i=1; i<5; ++i){
                    if(table[i][0] == preyPosition[0][0] - 1 && table[i][1] == tableSize-1) {
                        huntersRequiredForWin--;
                    }
                    if(table[i][0] == preyPosition[0][0] + 1 && table[i][1] == tableSize-1) {
                        huntersRequiredForWin--;
                    }
                    if(table[i][0] == preyPosition[0][0] && table[i][1] == tableSize-2) {
                        huntersRequiredForWin--;
                    }
                    if(huntersRequiredForWin == 0)
                        return true;
                }
            }
            // </editor-fold>
            
            // <editor-fold defaultstate="collapsed" desc="Felül bárhol">
            if(preyPosition[0][0] == 0 && 
                (preyPosition[0][1] > 0 && preyPosition[0][1] < tableSize-1)) {
                huntersRequiredForWin = 3;
                for(int i=1; i<5; ++i) {
                    if(table[i][0] == 0 && table[i][1] == preyPosition[0][1] - 1) {
                        huntersRequiredForWin--;
                    }
                    if(table[i][0] == 0 && table[i][1] == preyPosition[0][1] + 1) {
                        huntersRequiredForWin--;
                    }
                    if(table[i][0] == 1 && table[i][1] == preyPosition[0][1]) {
                        huntersRequiredForWin--;
                    }
                    if(huntersRequiredForWin == 0)
                        return true;
                }
            }
            // </editor-fold>
            
            // <editor-fold defaultstate="collapsed" desc="Alul bárhol">
            if(preyPosition[0][0] == tableSize-1 && 
                (preyPosition[0][1] > 0 && preyPosition[0][1] < tableSize-1)) {
                huntersRequiredForWin = 3;
                for (int i=1; i<5; ++i){
                    if(table[i][0] == tableSize-1 && table[i][1] == preyPosition[0][1] - 1) {
                        huntersRequiredForWin--;
                    }
                    if(table[i][0] == tableSize-1 && table[i][1] == preyPosition[0][1] + 1) {
                        huntersRequiredForWin--;
                    }
                    if(table[i][0] == tableSize-2 && table[i][1] == preyPosition[0][1]) {
                        huntersRequiredForWin--;
                    }
                    if(huntersRequiredForWin == 0)
                        return true;
                    }
                }
            
            // </editor-fold>
            
            // <editor-fold defaultstate="collapsed" desc="Középen bárhol">
            if((preyPosition[0][0] > 0 && preyPosition[0][0] < tableSize-1) &&
               (preyPosition[0][1] > 0 && preyPosition[0][1] < tableSize-1)) {
                huntersRequiredForWin = 4;
                //felette
                for (int i = 1; i<5; ++i){
                    if(table[i][0] == preyPosition[0][0]-1 && 
                       table[i][1] == preyPosition[0][1]) {
                    huntersRequiredForWin--;
                    }
                    //alatta
                    if(table[i][0] == preyPosition[0][0]+1 && 
                       table[i][1] == preyPosition[0][1]) {
                    huntersRequiredForWin--;
                    }
                    //balra
                    if(table[i][0] == preyPosition[0][0] && 
                       table[i][1] == preyPosition[0][1]-1) {
                    huntersRequiredForWin--;
                    }
                    //jobbra
                    if(table[i][0] == preyPosition[0][0] && 
                       table[i][1] == preyPosition[0][1]+1) {
                    huntersRequiredForWin--;
                    }
                    if(huntersRequiredForWin == 0)
                        return true;
                }
            }
            // </editor-fold>
        return false;
    }
    
    // return the player that's turn it is currently
    public char getCurrentPlayer() {
        return currentPlayer;
    }
}
