package com.tictactoe.gui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Classe pour afficher des boîtes de dialogue
 * 
 * @author Elias Okat
 */
public class DialogBox extends JOptionPane {

    /** Fenêtre contenant la boite */
    private JFrame parentFrame;
    /** Le code pour donner plus d'informations à l'utilisateur */
    private int code;
    /** Le titre de la boîte de dialogue */
    private String title;
    /** Le corps de la boîte de dialogue */
    private String body;

    /**
     * Créer une boite de dialogue.
     * @param parent fenêtre contenant la boite
     */
    public DialogBox(JFrame parent) {
        this.parentFrame = parent;
        this.code = 0;
        this.title = null;
        this.body = null;
    }

    /**
     * Définit tous les paramètres de la boîte de dialogue
     * 
     * @param title Le titre de la boîte de dialogue
     * @param body  Le corps de la boîte de dialogue
     * @param code  Le code pour donner plus d'informations à l'utilisateur
     */
    public void setDialogBox(String title, String body, int code) {
        this.code = code;
        this.title = title;
        this.body = body;
    }

    /**
     * Affiche une boîte de dialogue de type message
     * 
     * @param messageType le type de message à afficher
     */
    public void displayMessageDialogBox(int messageType) {
        if (this.code != 0)
            this.body += "\nError code : " + this.code;

        JOptionPane.showMessageDialog(this.parentFrame, this.body, this.title, messageType);
    }

    /**
     * Affiche une boîte de dialogue de type confirmation
     * 
     * @param optionType le type d'options à afficher (Oui/Non/Annuler)
     * @return le choix de l'utilisateur (0 = Oui, 1 = Non, 2 = Annuler)
     */
    public int displayConfirmDialogBox(int optionType) {
        if (this.code != 0)
            this.body += "\nError code : " + this.code;

        return JOptionPane.showConfirmDialog(this.parentFrame, this.body, this.title, optionType);
    }

    /**
     * Test
     * @param args -
     */
    public static void main(final String[] args) {
        final JFrame parent = new JFrame();

        DialogBox dial = new DialogBox(parent);

        dial.setDialogBox("Hello World !", "lorem ipsum", 0);
        System.out.print("Vous avez répondu ");
        switch (dial.displayConfirmDialogBox(YES_NO_CANCEL_OPTION)) {
            case 0:
            System.out.println("oui");
            break;

            case 1:
            System.out.println("non");
            break;

            case 2:
            System.out.println("cancel");
            break;
        }
    }
}
