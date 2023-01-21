import java.rmi.*;

import game.TTT_Data;
import game.TTT_Interface;
import gui.GUI;

public class Client {

    private Client() {
    }

    private static TTT_Interface game;
    private static int clientId;
    private static GUI gui;

    public static boolean buttonClicked(int index) {
        boolean return_value = false;

        try {
            return_value = game.playTurn(clientId, index);
        } catch (Exception e) {
            System.out.println("Le joueur n'a pas pu jouer à l'index " + index);
        }

        return return_value;
    }

    public static boolean ClientDisconect() {
        boolean return_value = false;
        try {
            return_value = game.disconnect(clientId);
        } catch (Exception e) {
            System.out.println("Le serveur n'a pas pu deconnecter le joueur" + clientId);
        }

        return return_value;
    }

    private static void CallErrorDialog(String body, int errorCode) {
        gui.DisplayErrorDialog("TictacToe Erreur",body,errorCode);
    }
    public static void main(String[] args) {
        new GUI();

        try {
            game = (TTT_Interface) Naming.lookup("rmi:///TicTacToe");
            clientId = game.connect();
            //TODO : Ajouter la gestion de l'erreur lors de la connexion (trop de joueurs ou autres)
        } catch (Exception e) {
            System.out.println("Erreur d'accès à l'objet distant.");
            System.out.println(e);
            System.exit(-1);
        }

        if (clientId == -1) {
            System.out.println("Impossible de rejoindre la partie, car il y a déjà deux joueurs");
            System.exit(-1);
        }

        System.out.println("clientId : "+clientId);
        
        while (clientId != -1) {
            try {
                TTT_Data Data = game.fetchData(clientId);

                // if (!Data.opponent_connected) {
                //    CallErrorDialog("Votre adversaire a été déconnecté, la partie est donc interrompue.",0);
                // }
            } catch (Exception e) {
                System.out.println("Erreur fetchData");
                System.out.println(e);
                System.exit(-1);
            }
        }

    }
}
