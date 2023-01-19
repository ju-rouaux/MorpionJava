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
                
                System.exit(0);
            }
        });
        this.setJMenuBar(menu);
        this.setSize(400, 400);
        this.initButtons();
        this.initGrid(this.getContentPane());

        JPanel main_pannel = new JPanel();
        JPanel left_pannel = new JPanel();
        JPanel right_pannel = new JPanel();

        GridBagLayout main_layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        main_pannel.setLayout(main_layout);

        c.gridx = 0;
        c.gridy = 0;
        main_pannel.add(left_pannel, c);

        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 2;
        main_pannel.add(right_pannel, c);

        this.add(main_pannel);

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

    void exitGame()
    {
        DialogBox dial = new DialogBox(this);
        dial.setDialogBox("TictacToe","Votre partie est toujours en cours, êtes vous sur de vouloirs quitter ?",0);
        dial.displayDialogBox();
        //Syste Client.ClientDisconect();
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