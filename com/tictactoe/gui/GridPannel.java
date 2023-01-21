package com.tictactoe.gui;

import javax.swing.JButton;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.BorderFactory;

import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Dimension;

import com.tictactoe.Client;

class GridPannel extends JPanel {
    
    private GridButton[] buttons;

    GridPannel() {
        System.out.println("BUUU");
        buttons = new GridButton[9];
        for (int i = 0; i < 9; i++)
            buttons[i] = new GridButton(this, i);

        this.setPreferredSize(new Dimension(GUI.HEIGHT, GUI.HEIGHT));
        
        this.setLayout(new GridLayout(3, 3));
        for (JButton button : buttons)
            this.add(button);

        this.resetGrid();
    }

    public void resetGrid() {
        for(GridButton b : buttons) {
            b.setEnabled(false);
            b.setText("");
            b.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        }
    }

    void buttonClicked(int index) {
        buttons[index].setEnabled(false);
        if(!Client.gridButtonClicked(index))
            buttons[index].setEnabled(true);
    }

    /**
     * Actualise les textes des boutons.
     * @param cells
     */
    public void updateGridText(char[] cells) {
        for(int i = 0; i < 9; i++)
            buttons[i].setText(Character.toString(cells[i]));
    }

    public void highlight(int[] cells) {
        for(int index : cells)
            buttons[index].setBorder(BorderFactory.createLineBorder(Color.RED));
    }

    public void deactivateAll() {
        for(JButton b : buttons)
            b.setVisible(false);
    }

    public void activateCells(int cells[]) {
        for(int index : cells)
            buttons[index].setVisible(true);
    }

}

class GridButton extends JButton {
    private int index;
    private GridPannel grid;

    public GridButton(GridPannel grid, int index) {
        this.grid = grid;
        this.index = index;

        this.addActionListener((ActionEvent e) -> {
            this.setEnabled(false);
            this.grid.buttonClicked(this.index);
        });
    }
}