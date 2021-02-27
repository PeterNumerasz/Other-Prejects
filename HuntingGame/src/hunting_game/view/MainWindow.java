/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hunting_game.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import hunting_game.model.Model;
import java.awt.Component;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author numip
 */
public class MainWindow extends JFrame {

    private JPanel mainPanel;   // playground for hunters and the prey
    private JLabel roundLabel;  // Label showing current player and who won
    private MyMenuBar menuBar;  // Menubar at the top
    private Model model;        // Model used 
    private JButton[][] grid;   // Buttons used to play
    private char winner = 'N';  // Variable for later use, N = None
    private int size;           // Size of playground
    private boolean selected = false;

    public MainWindow() {
        super();
        setSize(600, 600);
        setTitle("Hunting Game");
        URL url = MainWindow.class.getResource("hunter.png");
        setIconImage(Toolkit.getDefaultToolkit().getImage(url));
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                showExitConfirmation();
            }
        });
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        menuBar = new MyMenuBar(startNewGameAction, exitGameAction);

        setJMenuBar(menuBar);
        model = new Model(menuBar.getDifficulty());
        grid = new JButton[model.getTableSize()][model.getTableSize()];
        roundLabel = new JLabel();
        setTableLayout();
    }

    // Asking if they really want to quit from this awesome game
    private void showExitConfirmation() {

        int n = JOptionPane.showConfirmDialog(this,
                "Valóban ki akar lépni?",
                "Megerősítés", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private boolean ContinueConfirmation() {

        int n = JOptionPane.showConfirmDialog(this,
                "Mehet a következő ?",
                "Megerősítés", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            return true;
        } else {
            return false;
        }
    }

    // Adds one button, sets its action listener and we can also check the win
    // condition here
    private void addButton(JPanel mainPanel, int i, int j) {
        JButton button = new JButton();
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!selected) {
                    if (model.canSelect(i, j)) {
                        selected = true;
                        Component[] components = mainPanel.getComponents();
                        components[model.getSelected()].setEnabled(true);
                        model.setSelected(i, j);
                        button.setEnabled(false);
                    }
                } else {
                    if (model.buttonAction(i, j)) {
                        if (model.preyWon()) {
                            winner = 'P';
                        }
                        if (model.hunterWon()) {
                            winner = 'H';
                        }
                        selected = false;
                        (mainPanel.getComponent(model.getSelected())).setEnabled(true);
                        model.setSelected(-1, -1);

                    }
                }
                revalidateTable();
            }
        });
        grid[i][j] = button;
        mainPanel.add(grid[i][j]);
    }

    // used to keep the table up to date 
    private void revalidateTable() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                ((JButton) (mainPanel.getComponent(i * size + j))).setText("");
            }
        }
        ((JButton) (mainPanel.getComponent(model.getPosition(0)))).setText("P");
        for (int i = 1; i < 5; i++) {
            ((JButton) (mainPanel.getComponent(model.getPosition(i)))).setText("H");
        }
        if (model.getCurrentPlayer() == 'H') {
            roundLabel.setText("Hunter's turn");
        }
        if (model.getCurrentPlayer() == 'P') {
            roundLabel.setText("Prey's turn");
        }

        if (winner != 'N' && winner == 'H') {
            roundLabel.setText("Hunters won the game!");
            Component[] buttons = (mainPanel.getComponents());
            for (Component button : buttons) {
                button.setEnabled(false);
            }
            if (ContinueConfirmation()) {
                setTableLayout();
            }
            else showExitConfirmation();
        }
        if (winner != 'N' && winner == 'P') {
            roundLabel.setText("The prey won the game!");
            Component[] buttons = (mainPanel.getComponents());
            for (Component button : buttons) {
                button.setEnabled(false);
            }
            if (ContinueConfirmation()) {
                setTableLayout();
            }
            else showExitConfirmation();
        }
    }

    // Called from new game menu option, start a new game
    private final Action startNewGameAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            setTableLayout();
        }
    };

    // called from exit menu, closes the game when confirmed
    private final Action exitGameAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            showExitConfirmation();
        }
    };

    // Basically this generates the whole table based on the difficulty
    private void setTableLayout() {

        winner = 'N';
        model = new Model(menuBar.getDifficulty());
        size = model.getTableSize();
        grid = new JButton[size][size];

        mainPanel = new JPanel();
        this.getContentPane().removeAll();

        mainPanel.setLayout(new GridLayout(size, size));
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                addButton(mainPanel, i, j);
            }
        }
        add(mainPanel, BorderLayout.CENTER);

        this.validate();
        this.repaint();
        revalidateTable();

        add(roundLabel, BorderLayout.PAGE_START);
    }
}
