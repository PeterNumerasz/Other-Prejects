/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hunting_game.view;

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import hunting_game.model.Difficulty;

/**
 *
 * @author numip
 */
public class MyMenuBar extends JMenuBar {
    
    private Difficulty difficulty = Difficulty.EASY;
    
    public MyMenuBar(Action startNewGameAction, Action exitGameAction) {
        
        JMenu fileMenu = new JMenu("Fájl");
        fileMenu.setMnemonic('F');
        JMenuItem exitGame = new JMenuItem(exitGameAction);
        exitGame.setText("Kilépés");
        fileMenu.add(exitGame);
        fileMenu.addActionListener(exitGameAction);

        JMenu newGameMenu = new JMenu("Új játék");
        JMenuItem startNewGame = new JMenuItem(startNewGameAction);
        startNewGame.setText("Indítás");
        newGameMenu.add(startNewGame);
        
        newGameMenu.addSeparator();
        
        ButtonGroup group = new ButtonGroup();
        
        JRadioButtonMenuItem easy = new JRadioButtonMenuItem();
        easy.setText("Könnyű");
        easy.setSelected(true);
        easy.addActionListener(actionListener);
        easy.setActionCommand(Difficulty.EASY.name());
        group.add(easy);
        
        JRadioButtonMenuItem medium = new JRadioButtonMenuItem();
        medium.setText("Közepes");
        medium.addActionListener(actionListener);
        medium.setActionCommand(Difficulty.MEDIUM.name());
        group.add(medium);
        
        JRadioButtonMenuItem hard = new JRadioButtonMenuItem();
        hard.setText("Nehéz");
        hard.addActionListener(actionListener);
        hard.setActionCommand(Difficulty.HARD.name());
        group.add(hard);
        
        newGameMenu.add(easy);
        newGameMenu.add(medium);
        newGameMenu.add(hard);
        
        add(fileMenu);
        add(newGameMenu);
    }

    private ActionListener actionListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            String actionCommand = e.getActionCommand();
            difficulty = Difficulty.valueOf(actionCommand);
            //System.out.println(difficulty);
        }
        
    };
    private ActionListener exitGameAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String actionCommand = e.getActionCommand();
            System.out.println("QTTYA");
        }
    };

    public Difficulty getDifficulty() {
        return difficulty;
    }
}
