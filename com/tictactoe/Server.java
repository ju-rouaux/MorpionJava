package com.tictactoe;

import com.tictactoe.game.TicTacToe;
import java.rmi.Naming;

/**
 * Ajout d'un objet TicTacToe au remote object registry.
 * @author Julien Rouaux
 * @see com.tictactoe.game.TicTacToe
 */
public class Server {

    /**
     * Main
     * @param args -
     */
    public static void main(String[] args) {
        try {
            TicTacToe game = new TicTacToe();
            Naming.rebind("rmi:///TicTacToe", game); //Lier l'objet dans le registry
        } 
        catch (Exception e) {
            System.out.println("Erreur de liaison de l'objet TicTacToe. Pensez à lancer le rmiregistry.");
            System.out.println(e);
            System.exit(-1);
        }
    }
}
