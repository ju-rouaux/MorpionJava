import game.TicTacToe;
import java.rmi.Naming;

public class Server {

    public static void main(String[] args) {
        try {
            TicTacToe game = new TicTacToe();
            Naming.rebind("rmi:///TicTacToe", game); //Lier l'objet dans le registry
        } 
        catch (Exception e) {
            System.out.println("Erreur de liaison de l'objet TicTacToe. Penser à lancer le rmiregistry.");
            System.out.println(e);
            System.exit(-1);
        }
    }
}
