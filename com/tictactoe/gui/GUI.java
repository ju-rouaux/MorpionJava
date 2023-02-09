package com.tictactoe.gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;

import com.tictactoe.Client;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Interface graphique du jeu.
 * @author Julien Rouaux - Elias Okat
 */
public class GUI extends JFrame {

    public static final int WIDTH = 600;
    public static final int HEIGHT = 400;

    private JLabel message_label;
    private JLabel symbole_label;
    private GridPannel grid;
    private JButton button_X;
    private JButton button_O;

    /**
     * Instanciation de la fenêtre de jeu.
     */
    public GUI() {
        super("TicTacToe");

        this.setResizable(false);
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.LINE_AXIS));


        // Fenêtre de dialogue à la fermeture du programme.
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                if (exitGame()) {
                    System.exit(0);
                }
            }
        });

        // Menu
        Box left_box = new Box(BoxLayout.Y_AXIS);
        left_box.setAlignmentY(CENTER_ALIGNMENT);
        left_box.setPreferredSize(new Dimension(WIDTH / 3, HEIGHT));

        JLabel title = new JLabel("<html><h1>TicTacToe</h1></html>", JLabel.CENTER);
        title.setAlignmentX(CENTER_ALIGNMENT);
        left_box.add(title);

        left_box.add(Box.createRigidArea(new Dimension(0, 80)));

        message_label = new JLabel("<html><p>En attente des joueurs...</p></html>", JLabel.CENTER);
        message_label.setAlignmentX(CENTER_ALIGNMENT);
        left_box.add(message_label);

        symbole_label = new JLabel("<html><p></p></html>", JLabel.CENTER);
        symbole_label.setAlignmentX(CENTER_ALIGNMENT);
        left_box.add(symbole_label);

        left_box.add(Box.createRigidArea(new Dimension(0, 120)));

        this.button_X = new JButton("JOUER \"X\"");
        this.enable_X(true);
        this.button_X.setAlignmentX(CENTER_ALIGNMENT);
        left_box.add(this.button_X);
        
        left_box.add(Box.createRigidArea(new Dimension(10, 10)));

        this.button_O = new JButton("JOUER \"O\"");
        this.enable_O(true);
        this.button_O.setAlignmentX(CENTER_ALIGNMENT);
        left_box.add(this.button_O);

        // Ajout menu
        this.add(left_box);

        // Grille
        this.grid = new GridPannel();
        this.grid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Ajout grille
        this.add(this.grid);

        this.pack();
        this.setVisible(true);
    }

    /**
     * TODO
     * @param Title
     * @param body
     * @param errorCode
     */
    void DisplayErrorDialog(String Title, String body, int errorCode) {
        DialogBox dial = new DialogBox(this);
        dial.setDialogBox("Erreur", body, errorCode);
        dial.displayMessageDialogBox(JOptionPane.ERROR_MESSAGE);
    }

    /**
     * TODO
     * @param Title
     * @param body
     */
    void displayMessageDialogBox(String Title, String body) {
        DialogBox dial = new DialogBox(this);
        
        dial.setDialogBox(Title, body, 0);
        dial.displayMessageDialogBox(JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * TODO
     * @param Title
     * @param body
     * @param option
     * @return
     */
    int displayConfirmDialogBox(String Title, String body, int option) {
        DialogBox dial = new DialogBox(this);
        
        dial.setDialogBox(Title, body, 0);
        return dial.displayConfirmDialogBox(option);
    }

    /**
     * TODO
     * @return
     */
    boolean exitGame()
    {
        int reponse = displayConfirmDialogBox("TictacToe","Votre partie est toujours en cours, êtes vous sur de vouloirs quitter ?",JOptionPane.YES_NO_OPTION);
        
        if (reponse == 0) {
            if (Client.disconnect()) {
                this.displayMessageDialogBox("Information","Vous avez bien été déconnecté");
            } 
            else {
                this.displayMessageDialogBox("Information","Vous n'avez pas pu être correctement déconnecté");
            }
        }

        return reponse == 0;
    }

    /**
     * Désactive toutes les cellules de la grille.
     * Le joueur ne peut plus cliquer sur ces dernières.
     */
    public void deactivateAll() {
        this.grid.deactivateAll();
    }

    /**
     * Active les cellules de la grille dont les index sont donnés en paramètre.
     * @param cells Les index des cellules à activer.
     */
    public void activateCells(Integer[] cells) {
        this.grid.activateCells(cells);
    }

    /**
     * Met en surbrillance (encadré rouge) les cellules dont les index sont donnés en paramètre.
     * @param cells Les index des cellules à mettre en surbrillance.
     */
    public void highlight(int[] cells) {
        this.grid.highlight(cells);
    }

    /**
     * Change le texte contenu dans les cellules par le caractère donné au même index que l'index de la cellule.
     * @param cells Les caractères que doivent contenir chaque cellule.
     */
    public void updateGridText(char[] cells) {
        this.grid.updateGridText(cells);
    }

    /**
     * Change le message affiché à gauche de l'interface.
     * @param message Le nouveau message.
     */
    public void setMessage(String message) {
        this.message_label.setText(message);
    }

    /**
     * Change le symbole correspondant au joueur.
     * @param message Le symbole attribué au joueur.
     */
    public void setSymbole(String symbole) {
        this.symbole_label.setText(symbole);
    }

    /**
     * Enlève toutes les modifications faites sur les cellules, et les désactive.
     */
    public void resetGrid() {
        this.grid.resetGrid();
    }

    /**
     * Active ou désactive le bouton de connexion en tant que joueur X.
     * @param b Vrai pour activer le bouton, faux pour le désactiver.
     */
    public void enable_X(boolean b) {
        this.button_X.setEnabled(b);

        if (b == true) {
            //Prévenir l'ajout de multiple actionListener inutilement.
            if(this.button_X.getActionListeners().length == 0)
                this.button_X.addActionListener((ActionEvent e) -> Client.connectAsX());
            this.button_X.setText("Jouer \"X\"");
        } else {
            this.button_X.removeActionListener(null);
            this.button_X.setText("\"X\" est connecté");
        }
    }

    /**
     * Active ou désactive le bouton de connexion en tant que joueur O.
     * @param b Vrai pour activer le bouton, faux pour le désactiver.
     */
    public void enable_O(boolean b) {
        this.button_O.setEnabled(b);

        if (b == true) {
            //Prévenir l'ajout de multiple actionListener inutilement.
            if(this.button_O.getActionListeners().length == 0)
                this.button_O.addActionListener((ActionEvent e) -> Client.connectAsO());
            this.button_O.setText("Jouer \"O\"");
        } else {
            this.button_O.removeActionListener(null);
            this.button_O.setText("\"O\" est connecté");
        }
    }
}