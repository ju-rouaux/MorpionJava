import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.WindowConstants;
import java.awt.Container;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.event.ActionEvent;

class Button extends JButton {
    private int index;
    private boolean clicked; //Vrai si le bouton a déjà été cliqué
    private GUI gui;

    public Button(GUI gui, int index) {
        this.gui = gui;
        this.index = index;
        this.clicked = false;

        this.addActionListener(((ActionEvent e) -> {
            if(!this.clicked) {
                this.gui.buttonClicked(this.index);
                this.setText("X");
                this.clicked();
            }
        }));
    }

    public boolean isClicked() {
        return clicked;
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

    private Client client;

    private Button[] buttons;
    
    private void initButtons() {
        buttons = new Button[9];
        for(int i = 0; i < 9; i++)
            buttons[i] = new Button(this, i);
    }


    private void initGrid(Container c) {
        c.setLayout(new GridLayout(3,3));
        for(JButton button : buttons)
            c.add(button);
    }


    public GUI(Client client){
        super("Morpion");

        this.client = client;

        JMenuBar menu = new JMenuBar();
        menu.add(new JMenu("Restart"));


        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setJMenuBar(menu);
        this.setSize(400,400);
        this.initButtons();
        this.initGrid(this.getContentPane());; 

        this.setVisible(true);
    }


    public void serverButtonClicked(int index_button) {
        buttons[index_button].setText("O");
        buttons[index_button].clicked();
    }

    void buttonClicked(int index) {
        //TODO appeler le client
    }

    public void display_waitingForServer() {
        for(Button b : buttons) {
            b.setVisible(false);
        }
    }

    public void display_waitingForClient() {
        for(Button b : buttons) {
            if(!b.isClicked())
                b.setVisible(true);
        }
    }

    public void reset() {
        for(Button b : buttons) {
            b.reset();
        }
    }
}