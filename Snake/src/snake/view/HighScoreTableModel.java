package snake.view;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import snake.persistence.HighScore;

public class HighScoreTableModel extends AbstractTableModel{
    private final ArrayList<HighScore> highScores;
    private final String[] colName = new String[]{ "timestamp", "name", "score" };
    
    public HighScoreTableModel(ArrayList<HighScore> highScores){
        this.highScores = highScores;
    }

    @Override
    public int getRowCount() { return highScores.size(); }

    @Override
    public int getColumnCount() { return 3; }

    @Override
    public Object getValueAt(int r, int c) {
        HighScore h = highScores.get(r);
        if      (c == 0) return h.name;
        else if (c == 1) return h.score;
        return h.score;
    }

    @Override
    public String getColumnName(int i) { return colName[i]; }    
    
}
