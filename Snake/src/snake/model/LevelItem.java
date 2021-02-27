package snake.model;

public enum LevelItem {
    S_H('H'), 
    S_T('T'), 
    S_B('B'), 
    WALL('W'), 
    EMPTY(' '), 
    FOOD('F');
    LevelItem(char rep){ representation = rep; }
    public final char representation;
}
