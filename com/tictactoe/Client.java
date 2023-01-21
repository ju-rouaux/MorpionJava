package com.tictactoe;

import java.rmi.Naming;

import com.tictactoe.game.TTT_Data;
import com.tictactoe.game.TTT_Interface;
import static com.tictactoe.game.TTT_Data.State;
import com.tictactoe.gui.GUI;

public class Client {

    private Client() {
    }

    private static TTT_Interface game;
    private static TTT_Data game_data;
    private static int client_id = -1;

    private static GUI gui;

    public static boolean gridButtonClicked(int index) {
        try {
            return game.playTurn(client_id, index);
        } catch (Exception e) {
            System.out.println(e);
        }

        return false;
    }

    public static void connectAsX() {

    }

    public static void connectAsO() {

    }

    // public static boolean ClientDisconect() {
    //     boolean return_value = false;
    //     try {
    //         return_value = game.disconnect(clientId);
    //     } catch (Exception e) {
    //         System.out.println("Le serveur n'a pas pu deconnecter le joueur" + clientId);
    //     }

    //     return return_value;
    // }

    // private static void CallErrorDialog(String body, int errorCode) {
    //     gui.DisplayErrorDialog("TictacToe Erreur",body,errorCode);
    // }
    public static void main(String[] args) {
        new GUI();

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
                game_data = game.fetchData();
            } catch (Exception e) {
                System.out.println("Erreur de connection au serveur :");
                System.out.println(e);
                System.exit(-1);
            }
        }

    }
}
