import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.Dimension;

public class DialogBox {
    private JFrame parentFrame;
    private int code;
    private String Title;
    private String Body;

    public enum DialogBoxTypes {
        MESSAGE, QUESTION, INFORMATION, WARNING, ERROR
    }
    public  DialogBoxTypes Dialogtypes;
    private int Dialogtype;

    public DialogBox(JFrame parent) {
        this.parentFrame = parent;
        this.code = 0;
        this.Title = null;
        this.Body = null;
        this.Dialogtype = -2;
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

    public void setDialogBoxType(DialogBoxTypes type) {
        switch(type){
            case MESSAGE:
            Dialogtype = -1;
            break;

            case ERROR:
            Dialogtype = 0;
            break;

            case INFORMATION:
            Dialogtype = 1;
            break;

            case WARNING:
            Dialogtype = 2;
            break;

            case QUESTION:
            Dialogtype = 3;
            break;
        }
    }

    /**
     * Sets all the parameters of the DialogBox. You must specify one of the
     * following choices:
     *
     * @param Title       The title string for the dialog
     * @param Body        The body string for the dialog
     * @param code        A code to display, in order to give user further information
     */
    public void setDialogBox(String Title, String Body, int code) {
        this.code = code;
        this.Title = Title;
        this.Body = Body;
    }

    public void displayDialogBox() {
        JOptionPane d = new JOptionPane();
        //d.setDialogBoxType(d.DialogBoxTypes.MESSAGE);
        d.showMessageDialog(this.parentFrame, this.Body, this.Title, Dialogtype);
        d.setPreferredSize(new Dimension(1,2));
    }

    public static void main(final String[] args) {
        final JFrame parent = new JFrame();

        DialogBox dial = new DialogBox(parent);
        dial.setDialogBox("Hello World !", "lorem ipsum", 0);
        dial.displayDialogBox();
    }
}
