package com.tictactoe.gui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class DialogBox extends JOptionPane{
    private JFrame parentFrame;
    private int code;
    private String title;
    private String body;
    private int DialogBoxType;

    int MESSAGE = -1;
    int QUESTION = 3;
    int INFORMATION = 1;
    int WARNING = 2;
    int ERROR = 0;

    public DialogBox(JFrame parent) {
        this.parentFrame = parent;
        this.code = 0;
        this.title = null;
        this.body = null;
        this.DialogBoxType = -2;
    }

    /**
     * Sets the type of the DialogBox. You must specify one of the following
     * choices:
     * @param type
     * <code>MESSAGE</code>,
     * <code>QUESTION</code>,
     * <code>INFORMATION</code>,
     * <code>WARNING</code>,
     * or <code>ERROR</code>
     * 
     */

    public void setDialogBoxType(int type) {
        this.DialogBoxType = type;
    }

    /**
     * Sets all the parameters of the DialogBox. You must specify one of the
     * following choices:
     *
     * @param title       The title string for the dialog
     * @param body        The body string for the dialog
     * @param code        A code to display, in order to give user further information
     */
    public void setDialogBox(String title, String body, int code) {
        this.code = code;
        this.title = title;
        this.body = body;
    }

    public void displayMessageDialogBox(int messageType) {
        if (this.code != 0)
            this.body += "\nError code : "+this.code;

        JOptionPane.showMessageDialog(this.parentFrame, this.body, this.title, messageType);
    }

    public int displayConfirmDialogBox(int optionType) {
        if (this.code != 0)
            this.body += "\nError code : "+this.code;

        return JOptionPane.showConfirmDialog(this.parentFrame, this.body, this.title, optionType);
    }

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
        //System.out.println(dial.displayConfirmDialogBox(YES_NO_CANCEL_OPTION));
    }
}
