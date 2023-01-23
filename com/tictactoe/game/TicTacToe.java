package com.tictactoe.game;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class TicTacToe extends UnicastRemoteObject implements TTT_Interface {
   
    private TTT_Data data;

    private int X_player_id;
    private int O_player_id;

    /**
     * Permet au joueur de l'identifiant donné de joueur son tour, et actualise l'état général du jeu.
     * Le coup est ignoré s'il n'est pas autorisé.
     */
    public boolean playTurn(int player_id, int cell_index) throws RemoteException {
        System.out.println(player_id + " a joué en " + cell_index);

        //Vérifier si l'index est correct
        if(cell_index < 0 || cell_index >= 9 || this.data.grid[cell_index] != ' ')
            return false;

        //Vérifier si le joueur est autorisé à jouer et jouer le tour
        if((this.X_player_id == player_id) && (this.data.whoseTurn == 'X'))
                this.data.grid[cell_index] = 'X';
        else if((this.O_player_id == player_id) && (this.data.whoseTurn == 'O'))
                this.data.grid[cell_index] = 'O';
        else
            return false; //Non autorisé

        //Calculer si victoire
        int[] winningCombo = this.winningCombo();

        //Passer au joueur suivant
        this.data.nextPlayer();

        //Si fin de la partie
        if(winningCombo != null) {
            this.disconnect(this.X_player_id);
            this.disconnect(this.O_player_id);
            if(winningCombo[0] == -1) //Egalité
                this.data.setDraw();
            else
                this.data.setWinner(this.data.grid[winningCombo[0]], winningCombo);
        }

        return true;
    }

    /**
     * Retourne les données de la partie.
     */
    public TTT_Data fetchData() throws RemoteException {
        return this.data;
    }


    /**
     * Permet au joueur de se connecter en tant que X à la partie.
     * La partie est lancée si deux joueurs sont connectés.
     * Retourne un identifiant compris dans l'interval [0, 499].
     * @return l'identifiant, -1 si la place est prise.
     * @throws RemoteException
     */
    public int connectAsX() throws RemoteException {
        if(this.X_player_id < 0) {
            this.X_player_id = (int) (Math.random() * 1000) % 500;

            if(this.O_player_id >= 0) //Lancer partie si O connecté.
                this.data.setStart();

            return this.X_player_id;
        }
        return -1;
    }

    /**
     * Permet au joueur de se connecter en tant que O à la partie.
     * La partie est lancée si deux joueurs sont connectés.
     * Retourne un identifiant compris dans l'interval [500, 999].
     * @return l'identifiant, -1 si la place est prise.
     * @throws RemoteException
     */
    public int connectAsO() throws RemoteException {
        if(this.O_player_id < 0) {
            this.O_player_id = 500 + (int) (Math.random() * 1000) % 500;

            if(this.X_player_id >= 0) //Lancer partie si X connecté.
                this.data.setStart();

            return this.O_player_id;
        }
        return -1;
    }

    /**
     * Déconnecte le joueur de la partie.
     * Si la partie est en cours, elle est arrêtée.
     * Si le joueur était en attente d'un autre joueur, il libère sa place.
     */
    public void disconnect(int player_id) throws RemoteException {
        if(this.X_player_id == player_id) {
            this.data.X_connected = false;
            this.X_player_id = -1;
        }
        else if(this.O_player_id == player_id) {
            this.data.O_connected = false;
            this.O_player_id = -1;
        }
        else //Ignorer la suite si le joueur déconnecté n'est pas dans la partie
            return;

        if(this.data.state == TTT_Data.State.PLAYING)
            this.data = new TTT_Data(); //Reset la partie
    }


    /**
     * Retourne l'index des cases formant le combo gagnant de la partie, l'array {-1} en cas d'égalité,
     * sinon null si la partie n'est pas terminée.
     * Exemple : {0,1,2} pour la première ligne de la grille.
     * @return le combo gagnant de la partie, l'array {-1} en cas d'égalité,
     * sinon null si la partie n'est pas terminée.
     */
    private int[] winningCombo() throws RemoteException {
   
        //Test lignes
        for(int i = 0; i < 3; i++)
            if(data.grid[i*3] != ' ' && (data.grid[i*3] == data.grid[(i*3)+1]) && (data.grid[(i*3)+1] == data.grid[(i*3)+2]))
                return new int[] {i*3, (i*3)+1, (i*3)+2};

        //Test colonnes
        for(int j = 0; j < 3; j++)
            if(data.grid[j] != ' ' && (data.grid[j] == data.grid[3+j]) && (data.grid[3+j] == data.grid[6+j]))
                return new int[] {j, 3+j, 6+j};

        //Test diagonales
        if(data.grid[0] != ' ' && (data.grid[0] == data.grid[4]) && (data.grid[4] == data.grid[8]))
            return new int[] {0, 4, 8};
        if(data.grid[2] != ' ' && (data.grid[2] == data.grid[4]) && (data.grid[4] == data.grid[6]))
            return new int[] {2, 4, 6};

        //Test d'égalité = grille pleine
        for(int i = 0; i < 9; i++)
            if(data.grid[i] == ' ')
                return null;

        //Atteint si la grille est pleine
        return new int[] {-1};
    }


    public TicTacToe() throws RemoteException {
        this.X_player_id = -1;
        this.O_player_id = -1;

        this.data = new TTT_Data();        
    }
}
