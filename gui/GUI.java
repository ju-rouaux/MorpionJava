package gui;

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

class GridButton extends JButton {
    private int index;
    private boolean clicked; // Vrai si le bouton a déjà été cliqué
    private GUI gui;

    public GridButton(GUI gui, int index) {
        this.gui = gui;
        this.index = index;
        this.clicked = false;

        this.addActionListener(((ActionEvent e) -> {
            if (!this.clicked) {
                this.clicked = true;
                this.setEnabled(false);
                this.gui.buttonClicked(this);
            }
        }));
    }

    public boolean isClicked() {
        return this.clicked;
    }

    public int getIndex() {
        return this.index;
    }

    public void clicked() {
        this.clicked = true;
        this.setEnabled(false);
    }

    public void reset() {
        clicked = false;
        this.setVisible(true);
        this.setText("");
    }
}

public class GUI extends JFrame {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;

    private JLabel message;

    private GridButton[] buttons;



    private JPanel buildGrid() {
        JPanel grid = new JPanel();

        buttons = new GridButton[9];
        for (int i = 0; i < 9; i++)
            buttons[i] = new GridButton(this, i);

        grid.setPreferredSize(new Dimension(HEIGHT, HEIGHT));
        
        grid.setLayout(new GridLayout(3, 3));
        for (JButton button : buttons)
            grid.add(button);

        return grid;
    }

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

        message = new JLabel("<html><p>En attente des joueurs</p></html>", JLabel.CENTER);
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
        JPanel grid = buildGrid();
        grid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.add(grid);
        
        

        this.pack();

        this.setVisible(true);
    }

    public void serverButtonClicked(int index_button) {
        buttons[index_button].setText("O");
        buttons[index_button].clicked();

    }

    void buttonClicked(GridButton button) {
        button.setText("X");
        // if(!Client.buttonClicked(button.getIndex()))
        //     button.reset();

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
        for (GridButton b : buttons) {
            b.setVisible(false);
        }
    }

    public void display_waitingForClient() {
        for (GridButton b : buttons) {
            if (!b.isClicked())
                b.setVisible(true);
        }
    }

    public void reset() {
        for (GridButton b : buttons) {
            b.reset();
        }
    }
}