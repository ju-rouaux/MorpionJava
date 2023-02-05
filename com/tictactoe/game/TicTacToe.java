package com.tictactoe.game;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.tictactoe.game.TTT_Data.State;

/**
 * Jeu du TicTacToe.
 * Ce jeu actualise son état à chaque mouvement réalisé par un joueur.
 * Autrement dit, il ne boucle pas.
 * @author Julien Rouaux
 */
public class TicTacToe extends UnicastRemoteObject implements TTT_Interface {
   
    private TTT_Data data;

    private int X_player_id;
    private int O_player_id;


    public boolean playTurn(int player_id, int cell_index) throws RemoteException {
        
        //Vérifier si l'index est correct
        if(cell_index < 0 || cell_index >= 9 || this.data.grid[cell_index] != ' ')
        return false;
        
        //Vérifier si le joueur est autorisé à jouer et jouer le tour
        if((this.X_player_id == player_id) && (this.data.whoseTurn == 'X'))
        this.data.grid[cell_index] = 'X';
        else if((this.O_player_id == player_id) && (this.data.whoseTurn == 'O'))
        this.data.grid[cell_index] = 'O';
        else
        return false; //Non 
        
        System.out.println(player_id + " a joué en " + cell_index);

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
                this.data.setWinner(this.data.grid[winningCombo[0]] == X_player_id ? 'X' : 'O', winningCombo);
        }

        return true;
    }

    /**
     * Retourne les données de la partie.
     * @throws RemoteException
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

            this.playerConnected();
            
            this.data.X_connected = true;

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

            this.playerConnected();

            this.data.O_connected = true;

            return this.O_player_id;
        }
        return -1;
    }

    /** 
     * Vérifie si la partie doit être lancée lorsqu'un joueur se connecte.
     */
    private void playerConnected() {
        if(this.data.state != State.PLAYING) {
            this.data.setWait();
        }

        if(this.X_player_id >= 0 && this.O_player_id >= 0) //Lancer partie si 2 joueurs connectés
            this.data.setStart();
    }

    /**
     * Déconnecte le joueur de la partie.
     * Si la partie est en cours, elle est arrêtée.
     * Si le joueur était en attente d'un autre joueur, il libère sa place.
     * @throws RemoteException
     */
    public void disconnect(int player_id) throws RemoteException {
        System.out.println("Déconnexion de " + player_id);

        if(this.X_player_id == player_id || this.O_player_id == player_id) {
            this.data.X_connected = false;
            this.data.O_connected = false;
            this.X_player_id = -5;
            this.O_player_id = -5;
            this.data.setWait();
        }
        
        return;            
    }


    /**
     * Retourne l'index des cases formant le combo gagnant de la partie, l'array {-1} en cas d'égalité,
     * sinon null si la partie n'est pas terminée.
     * Exemple : {0,1,2} pour la première ligne de la grille.
     * @return le combo gagnant de la partie, l'array {-1} en cas d'égalité,
     * sinon null si la partie n'est pas terminée.
     * @throws RemoteException
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

    /**
     * Créé un TicTacToe, instancie une structure TTT_Data().
     * @throws RemoteException
     */
    public TicTacToe() throws RemoteException {
        this.X_player_id = -5;
        this.O_player_id = -5;

        this.data = new TTT_Data();        
    }
}
