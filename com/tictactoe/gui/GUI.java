package com.tictactoe.gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.border.*;

import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;



public class GUI extends JFrame {

    public static final int WIDTH = 600;
    public static final int HEIGHT = 400;

    private JLabel message;



    public GUI() {
        super("TicTacToe");

        this.setResizable(false);
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.LINE_AXIS));

        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                System.out.println("Game OVER");
                exitGame();
                System.exit(0);
            }
        });


        //Menu
        Box left_box = new Box(BoxLayout.Y_AXIS);
        left_box.setAlignmentY(CENTER_ALIGNMENT);

        left_box.setPreferredSize(new Dimension(WIDTH/3, HEIGHT));
        //left_box.setBorder(BorderFactory.createLineBorder(Color.black)); //pour debug
        
        JLabel title = new JLabel("<html><h1>TicTacToe</h1></html>", JLabel.CENTER);
        title.setAlignmentX(CENTER_ALIGNMENT);

        message = new JLabel("<html><p>En attente des joueurs...</p></html>", JLabel.CENTER);
        message.setAlignmentX(CENTER_ALIGNMENT);
        
        left_box.add(title);

        left_box.add(Box.createRigidArea(new Dimension(0, 80)));

        left_box.add(message);

        left_box.add(Box.createRigidArea(new Dimension(0, 120)));


        JButton button_X = new JButton("JOUER \"X\"");
        button_X.setAlignmentX(CENTER_ALIGNMENT);
        left_box.add(button_X);

        left_box.add(Box.createRigidArea(new Dimension(10, 10)));

        JButton button_O = new JButton("JOUER \"O\"");
        button_O.setAlignmentX(CENTER_ALIGNMENT);
        left_box.add(button_O);



        this.add(left_box);
        

        //Grille
        System.out.println("AAA");
        JPanel grid = new GridPannel();
        grid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.add(grid);
        
        

        this.pack();

        this.setVisible(true);
    }



    void DisplayErrorDialog(String Title, String body, int errorCode) {

        DialogBox dial = new DialogBox(this);
        
        dial.setDialogBox("Erreur", body, errorCode);
        dial.displayMessageDialogBox(dial.ERROR_MESSAGE);
    }

    void exitGame()
    {
        DisplayErrorDialog("TictacToe","Votre partie est toujours en cours, êtes vous sur de vouloirs quitter ?",200);
        //System.out.println(Client.ClientDisconect());
    }

    public void display_waitingForServer() {
        
    }

    public void display_waitingForClient() {
        
    }

    public void reset() {
        
    }
}