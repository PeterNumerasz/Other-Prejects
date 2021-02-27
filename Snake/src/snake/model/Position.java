package snake.model;

public class Position {
    public int x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }    
    
    public Position turn(Direction d){
        return new Position(x + d.x, y + d.y);
    }
    public Position move(Direction d) {
        String direction = d.name();
        Position newPos;
        switch(direction) {
            case "DOWN":
                newPos = new Position(x, y - 1);
                break;
            case "UP":
                newPos = new Position(x, y + 1);
                break;
            case "LEFT":
                newPos = new Position(x + 1, y);
                break;
            case "RIGHT":
                newPos = new Position(x - 1, y);
                break;
            default:
                newPos = null;
                break;
        }
        return newPos;        
    }
}
