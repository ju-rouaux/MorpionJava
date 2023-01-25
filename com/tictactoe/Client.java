package com.tictactoe;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.plaf.*;

import com.tictactoe.game.TTT_Data;
import com.tictactoe.game.TTT_Interface;
import com.tictactoe.game.TTT_Data.State;
import com.tictactoe.gui.GUI;

public class Client {

    private Client() {
    }

    private static TTT_Interface game;
    private static TTT_Data game_data;
    private static int client_id = -1;

    private static GUI gui;

    private static boolean disconnected = true;
    private static boolean game_has_just_started = false;

    public static boolean gridButtonClicked(int index) {
        try {
            return game.playTurn(client_id, index);
        } catch (RemoteException e) {
            System.out.println(e);
        }

        return false;
    }

    public static boolean connectAsX() {
        if(client_id != -1) //Déjà connecté
            return false;

        try {
            client_id = game.connectAsX();
            if(client_id != -1)
                return true;
        } catch (RemoteException e) {
            System.out.println(e);
        }

        System.out.println("Un joueur joue déjà X");
        return false;
    }

    public static boolean connectAsO() {
        if(client_id != -1) //Déjà connecté
            return false;

        try {
            client_id = game.connectAsO();
            if(client_id != -1)
                return true;
        } catch (RemoteException e) {
            System.out.println(e);
        }

        System.out.println("Un joueur joue déjà O");
        return false;
    }

    public static boolean disconnect() {
        try {
            game.disconnect(client_id);
        }
        catch (RemoteException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    public static void updateData() {
        try {
            game_data = game.fetchData();
        } catch (Exception e) {
            System.out.println("Erreur de connection au serveur :");
            System.out.println(e);
            System.exit(-1);
        }
    }

    // private static void CallErrorDialog(String body, int errorCode) {
    //     gui.DisplayErrorDialog("TictacToe Erreur",body,errorCode);
    // }

    public static void main(String[] args) {
        gui = new GUI();

        try {
            game = (TTT_Interface) Naming.lookup("rmi:///TicTacToe");
        } catch (Exception e) {
            System.out.println("Erreur d'accès à l'objet distant :");
            System.out.println(e);
            System.exit(-1);
        }

        System.out.println("Identifiant  : "+client_id);
        
        while (true) {
            try {
                Thread.sleep(200);
            }
            catch (Exception e) {}

            updateData();
            gui.setMessage(game_data.message);
            gui.updateGridText(game_data.grid);
            
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
