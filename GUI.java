// import javax.swing.JFrame;
// import javax.swing.JMenu;
// import javax.swing.JMenuBar;
// import javax.swing.WindowConstants;
// import java.awt.Container;
// import java.awt.GridLayout;
// import javax.swing.JButton;
// import java.awt.event.ActionEvent;
// import java.awt.event.WindowAdapter;
// import java.awt.event.WindowEvent;
// import javax.swing.BoxLayout;
import javax.swing.*; //Pour faciliter le développement
import java.awt.*;
import java.awt.event.*;

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

class GUI extends JFrame {

    private GridButton[] buttons;

    private void initButtons() {
        buttons = new GridButton[9];
        for (int i = 0; i < 9; i++)
            buttons[i] = new GridButton(this, i);
    }

    private void initGrid(Container c) {
        c.setLayout(new GridLayout(3, 3));
        for (JButton button : buttons)
            c.add(button);
    }

    public GUI() {
        super("Morpion");

        JMenuBar menu = new JMenuBar();
        menu.add(new JMenu("Restart"));

        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                System.out.println("Game OVER");
                exitGame();
                System.exit(0);
            }
        });
        this.setJMenuBar(menu);
        this.setSize(400, 400);

        this.initButtons();
        this.initGrid(this.getContentPane());


        this.setVisible(true);
    }

    public void serverButtonClicked(int index_button) {
        buttons[index_button].setText("O");
        buttons[index_button].clicked();

    }

    void buttonClicked(GridButton button) {
        button.setText("X");
        if(!Client.buttonClicked(button.getIndex()))
            button.reset();

    }

    void DisplayErrorDialog(String Title, String body, int errorCode) {

        DialogBox dial = new DialogBox(this);
        
        dial.setDialogBox("Erreur", body, errorCode);
        dial.displayMessageDialogBox(dial.ERROR_MESSAGE);
    }

    void exitGame()
    {
        DisplayErrorDialog("TictacToe","Votre partie est toujours en cours, êtes vous sur de vouloirs quitter ?",200);
        System.out.println(Client.ClientDisconect());
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