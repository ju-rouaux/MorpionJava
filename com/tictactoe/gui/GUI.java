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



public class GUI extends JFrame {

    public static final int WIDTH = 600;
    public static final int HEIGHT = 400;

    private JLabel message_label;
    private GridPannel grid;
    private JButton button_X;
    private JButton button_O;

    private JButton state_button;
    private boolean state;
    private JButton O_Play_State_Button;
    private JButton X_Play_State_Button;

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


        JLabel title = new JLabel("<html><h1>TicTacToe</h1></html>", JLabel.CENTER);
        title.setAlignmentX(CENTER_ALIGNMENT);
        left_box.add(title);

        left_box.add(Box.createRigidArea(new Dimension(0, 80)));

        message_label = new JLabel("<html><p>En attente des joueurs...</p></html>", JLabel.CENTER);
        message_label.setAlignmentX(CENTER_ALIGNMENT);
        left_box.add(message_label);
        
        left_box.add(Box.createRigidArea(new Dimension(0, 120)));

        this.button_X = new JButton("JOUER \"X\"");
        this.enable_X(true);
        this.button_X.setAlignmentX(CENTER_ALIGNMENT);
        left_box.add(this.button_X);


        this.X_Play_State_Button = new JButton("X player state");
        this.X_Play_State_Button.setAlignmentX(CENTER_ALIGNMENT);
        this.X_Play_State_Button.addActionListener((ActionEvent e) ->  this.enable_X(this.state) );

        left_box.add(this.X_Play_State_Button);
        
        left_box.add(Box.createRigidArea(new Dimension(10, 10)));

        this.button_O = new JButton("JOUER \"O\"");
        this.enable_O(true);
        this.button_O.setAlignmentX(CENTER_ALIGNMENT);
        left_box.add(this.button_O);



        this.O_Play_State_Button = new JButton("O player state");
        this.O_Play_State_Button.setAlignmentX(CENTER_ALIGNMENT);
        this.O_Play_State_Button.addActionListener((ActionEvent e) ->  this.enable_O(this.state) );
        left_box.add(this.O_Play_State_Button);

        this.state_button = new JButton("False");
        this.state_button.setAlignmentX(BOTTOM_ALIGNMENT);

        this.state_button.addActionListener((ActionEvent e) ->  this.switchState() );
        
        left_box.add(this.state_button);

        //Ajout menu
        this.add(left_box);
        

        //Grille
        System.out.println("AAA");
        this.grid = new GridPannel();
        this.grid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        



        //Ajout grille
        this.add(this.grid);
        
        this.pack();
        this.setVisible(true);
    }

    void switchState() {
        this.state = !this.state;

        if (this.state)
        this.state_button.setText("True");
        else
        this.state_button.setText("False");
    }

    void DisplayErrorDialog(String Title, String body, int errorCode) {

        DialogBox dial = new DialogBox(this);
        
        dial.setDialogBox("Erreur", body, errorCode);
        dial.displayMessageDialogBox(JOptionPane.ERROR_MESSAGE);
    }

    void displayMessageDialogBox(String Title, String body) {
        DialogBox dial = new DialogBox(this);
        
        dial.setDialogBox(Title, body, 0);
        dial.displayMessageDialogBox(JOptionPane.INFORMATION_MESSAGE);
    }

    void displayConfirmDialogBox(String Title, String body) {
        DialogBox dial = new DialogBox(this);
        
        dial.setDialogBox(Title, body, 0);
        dial.displayConfirmDialogBox(JOptionPane.YES_NO_CANCEL_OPTION);
    }

    void exitGame()
    {
        DisplayErrorDialog("TictacToe","Votre partie est toujours en cours, êtes vous sur de vouloirs quitter ?",200);
        if (Client.disconnect())
        {
            this.displayMessageDialogBox("Information","Vous avez bien été déconnecté");
        } else
        {
            this.displayMessageDialogBox("Information","Vous n'avez pas pu être correctement déconnecté");
        }
    }

    public void deactivateAll() {
        this.grid.deactivateAll();
    }

    public void activateCells(Integer[] cells) {
        this.grid.activateCells(cells);
    }

    public void highlight(int[] cells) {
        this.grid.highlight(cells);
    }
    
    public void updateGridText(char[] cells) {
        this.grid.updateGridText(cells);
    }
    
    public void setMessage(String message) {
        this.message_label.setText(message);
    }

    public void resetGrid() {
        this.grid.resetGrid();
    }

    public void enable_X(boolean b) {
        this.button_X.setEnabled(b);
        
        if(b == true) {
            this.button_X.addActionListener((ActionEvent e) -> Client.connectAsX());
            this.button_X.setText("Jouer \"Xa\"");
        }
        else {
            this.button_X.removeActionListener(null);
            this.button_X.setText("\"X\" est connecté");
        }
    }

    public void enable_O(boolean b) {
        this.button_O.setEnabled(b);
        
        if(b == true) {
            this.button_O.addActionListener((ActionEvent e) -> Client.connectAsO());
            this.button_O.setText("Jouer \"O\"");
        }
        else {
            this.button_O.removeActionListener(null);
            this.button_O.setText("\"O\" est connecté");
        }
    }

}