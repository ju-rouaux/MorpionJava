package com.tictactoe;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;

import com.tictactoe.game.TTT_Data;
import com.tictactoe.game.TTT_Interface;
import com.tictactoe.game.TTT_Data.State;
import com.tictactoe.gui.GUI;

/**
 * Application principale.
 * Lance l'interface et réalise les appels au serveur.
 * @author Julien Rouaux
 */
public class Client {

    /**
     * Ne pas autoriser l'instanciation d'un client.
     */
    private Client() {}

    private static TTT_Interface game;
    private static TTT_Data game_data;
    private static int client_id = -1;

    private static GUI gui;

    /**
     * Indique au serveur qu'un bouton de la grille est cliqué.
     * @param index Index du bouton cliqué dans la grille.
     * @return Vrai si le clic a été autorisé et enregistré par le serveur, sinon faux.
     */
    public static boolean gridButtonClicked(int index) {
        try {
            return game.playTurn(client_id, index);
        } catch (RemoteException e) {
            System.out.println(e);
        }
        return false;
    }

    /**
     * Indique au serveur la volonté de jouer le joueur X.
     * Si le joueur peut se connecter, son identifiant est actualisé par celui donné par le serveur.
     * @return Vrai si le joueur s'est connecté en tant que joueur X.
     */
    public static boolean connectAsX() {
        if(client_id != -1) {
            System.out.println("Un identifiant est déjà attribué au joueur.");
            return false;
        }

        try {
            client_id = game.connectAsX();
            if(client_id != -1) {
                System.out.println("Identifiant attribué par le serveur : " + client_id);
                return true;
            }
        } catch (RemoteException e) {
            System.out.println(e);
        }

        System.out.println("Un joueur joue déjà X.");
        return false;
    }

    /**
     * Indique au serveur la volonté de jouer le joueur O.
     * Si le joueur peut se connecter, son identifiant est actualisé par celui donné par le serveur.
     * @return Vrai si le joueur s'est connecté en tant que joueur O.
     */
    public static boolean connectAsO() {
        if(client_id != -1) {
            System.out.println("Un identifiant est déjà attribué au joueur.");
            return false;
        }

        try {
            client_id = game.connectAsO();
            if(client_id != -1) {
                System.out.println("Identifiant attribué par le serveur : " + client_id);
                return true;
            }
        } catch (RemoteException e) {
            System.out.println(e);
        }

        System.out.println("Un joueur joue déjà O.");
        return false;
    }

    /**
     * Appelé lorsque le joueur doit être déconnecté du jeu.
     * Le serveur réinitialise la partie si un joueur (en jeu) quitte.
     * @return
     */
    public static boolean disconnect() {
        try {
            System.out.println("Demande de déconnexion...");
            game.disconnect(client_id);
        }
        catch (RemoteException e) {
            System.out.println(e);
            return false;
        }

        System.out.println("Déconnexion réussie.");
        return true;
    }

    /**
     * Main
     * @param args -
     */
    public static void main(String[] args) {
        boolean disconnected = true;
        boolean game_has_just_started = false;

        gui = new GUI();

        try {
            game = (TTT_Interface) Naming.lookup("rmi:///TicTacToe");
        } catch (Exception e) {
            System.out.println("Erreur d'accès à l'objet distant :");
            System.out.println(e);
            System.exit(-1);
        }

        
        
        while (true) {
            try {
                Thread.sleep(200);
            }
            catch (Exception e) {}

            //Récupérer les données du serveur, on quitte le programme s'il n'est pas en ligne.
            try {
                game_data = game.fetchData();
            } catch (Exception e) {
                System.out.println(e);
                System.exit(-1);
            }

            gui.setMessage(game_data.message);
            gui.updateGridText(game_data.grid);

            if(client_id != -1)
                gui.setSymbole("Vous êtes le joueur " + (client_id < 500 ? 'X' : 'O') + ".");
            else
                gui.setSymbole("");
            
            if(game_data.state == State.PLAYING) {
                if(game_has_just_started) {
                    gui.resetGrid();
                    game_has_just_started = false;
                }

                ArrayList<Integer> activated = new ArrayList<>();

                //Vérifier si le joueur n'est pas autorisé à jouer
                if( client_id == -1 ||
                    !((game_data.whoseTurn == 'O' && client_id >= 500) ||  // !(tour de O et joueur O)
                    (game_data.whoseTurn == 'X' && client_id < 500))) {    // !(tour de X et joueur X)
                    gui.deactivateAll();
                    continue;
                }

                disconnected = false;

                for(int i = 0; i < 9; i++)
                    if(game_data.grid[i] == ' ')
                        activated.add(i);

                if(activated.size() > 0)
                    gui.activateCells(activated.toArray(new Integer[0]));
            }
            else {
                game_has_just_started = true;

                if(game_data.state == State.VICTORY)
                    gui.highlight(game_data.winningCombo);

                if(disconnected == false) {
                    client_id = -1;
                    disconnected = true;
                }
            }
            if(game_data.X_connected)
                gui.enable_X(false);
            else
                gui.enable_X(true);
            if(game_data.O_connected)
                gui.enable_O(false);
            else
                gui.enable_O(true);
        }
    }
}
