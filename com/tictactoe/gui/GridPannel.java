package com.tictactoe.gui;

import javax.swing.JButton;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Dimension;

import com.tictactoe.Client;

/**
 * Bouton de grille de morpion.
 * Il s'agit d'un JButton mais se voyant attribué un index à son instanciation.
 * @author Julien Rouaux
 * @see javax.swing.JButton
 */
class GridButton extends JButton {
    private int index;

    /**
     * Créer un nouveau bouton de grille.
     * Lorsque ce bouton est cliqué, il l'indique au client en donnant son index.
     * @param index Index du bouton.
     */
    public GridButton(int index) {
        this.index = index;

        this.addActionListener((ActionEvent e) -> {
            this.setEnabled(false);
            if(!Client.gridButtonClicked(this.index))
                this.setEnabled(true);
        });
    }
}

/**
 * Grille de morpion, contenant des GridButton.
 * @author Julien Rouaux
 * @see com.tictactoe.gui.GridButton
 */
class GridPannel extends JPanel {
    
    private GridButton[] buttons;

    /**
     * Instancier une grille de 9 boutons indexés de 0 à 8.
     */
    GridPannel() {
        buttons = new GridButton[9];
        for (int i = 0; i < 9; i++)
            buttons[i] = new GridButton(i);

        this.setPreferredSize(new Dimension(GUI.HEIGHT, GUI.HEIGHT));
        
        this.setLayout(new GridLayout(3, 3));
        for (JButton button : buttons)
            this.add(button);

        this.resetGrid();
    }

    /**
     * Enlève toutes les modifications faites sur les cellules, et les désactive.
     */
    public void resetGrid() {
        for(GridButton b : buttons) {
            b.setEnabled(false);
            b.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        }
    }

    /**
     * Change le texte contenu dans les cellules par le caractère donné au même index que l'index de la cellule.
     * @param cells Les caractères que doivent contenir chaque cellule.
     */
    public void updateGridText(char[] cells) {
        for(int i = 0; i < 9; i++)
            buttons[i].setText(Character.toString(cells[i]));
    }

    /**
     * Met en surbrillance (encadré rouge) les cellules dont les index sont donnés en paramètre.
     * @param cells Les index des cellules à mettre en surbrillance.
     */
    public void highlight(int[] cells) {
        for(int index : cells)
            buttons[index].setBorder(BorderFactory.createLineBorder(Color.RED));
    }

    /**
     * Désactive toutes les cellules de la grille.
     * Le joueur ne peut plus cliquer sur ces dernières.
     */
    public void deactivateAll() {
        for(JButton b : buttons)
            b.setEnabled(false);
    }

    /**
     * Active les cellules de la grille dont les index sont donnés en paramètre.
     * @param cells Les index des cellules à activer.
     */
    public void activateCells(Integer cells[]) {
        for(Integer index : cells)
            buttons[index].setEnabled(true);
    }
}