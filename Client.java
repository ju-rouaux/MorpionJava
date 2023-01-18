import java.rmi.*;

public class Client {

    private Client() {}

    private static ServerInterface game;
    private static int clientId;

    public static boolean buttonClicked(int index) {
        boolean return_value = false;
        try {
            return_value = game.playTurn(clientId, index);
        }
        catch (Exception e) {
            System.out.println("Le joueur n'a pas pu jouer à l'index " + index);
        }

        return return_value;
    }

    public static void main(String[] args) {
        new GUI();

        try {
            game = (ServerInterface) Naming.lookup("rmi:///Server");
            //clientId = game.connect();
        } catch (Exception e) {
            System.out.println("Erreur d'accès à l'objet distant.");
            System.out.println(e);
            System.exit(-1);
        }

        if (clientId == -1)
        {
            System.out.println("Impossible de rejoindre la partie, car il y a déjà deux joueurs");
            System.exit(-1);
        }


        

    }
}
