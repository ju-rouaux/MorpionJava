package com.tictactoe.game;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.tictactoe.game.TTT_Data.State;

/**
 * Jeu du TicTacToe.
 * Ce jeu actualise son état à chaque mouvement réalisé par un joueur.
 * Autrement dit, il ne boucle pas.
 * 
 * @author Julien Rouaux
 */
public class TicTacToe extends UnicastRemoteObject implements TTT_Interface {

    /** Etat de la partie */
    private TTT_Data data;

    /** Identifiant du joueur X (-5 si aucun) */
    private int X_player_id;
    
    /** Identifiant du joueur O (-5 si aucun) */
    private int O_player_id;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean playTurn(int player_id, int cell_index) throws RemoteException {
        // Vérifier si l'on est en cours de partie
        if (this.data.state != TTT_Data.State.PLAYING)
            return false;

        // Vérifier si l'index est correct
        if (cell_index < 0 || cell_index >= 9 || this.data.grid[cell_index] != ' ')
            return false;

        // Vérifier si le joueur est autorisé à jouer et jouer le tour
        if ((this.X_player_id == player_id) && (this.data.whoseTurn == 'X'))
            this.data.grid[cell_index] = 'X';
        else if ((this.O_player_id == player_id) && (this.data.whoseTurn == 'O'))
            this.data.grid[cell_index] = 'O';
        else
            return false; // Non

        System.out.println(player_id + " a joué en " + cell_index);

        // Calculer si victoire
        int[] winningCombo = this.winningCombo();

        // Si fin de la partie
        if (winningCombo != null) {
            this.disconnect(this.X_player_id);
            this.disconnect(this.O_player_id);
            if (winningCombo[0] == -1) // Egalité
                this.data.setDraw();
            else
                this.data.setWinner(this.data.whoseTurn, winningCombo);
            return true;
        }

        // Passer au joueur suivant
        this.data.nextPlayer();

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TTT_Data fetchData() throws RemoteException {
        return this.data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int connectAsX() throws RemoteException {
        if (this.X_player_id < 0) {
            this.X_player_id = (int) (Math.random() * 1000) % 500;

            this.playerConnected();

            this.data.X_connected = true;

            return this.X_player_id;
        }
        return -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int connectAsO() throws RemoteException {
        if (this.O_player_id < 0) {
            this.O_player_id = 500 + (int) (Math.random() * 1000) % 500;

            this.playerConnected();

            this.data.O_connected = true;

            return this.O_player_id;
        }
        return -1;
    }

    /**
     * Vérifie si la partie doit être lancée lorsqu'un joueur se connecte.
     * Lance la partie si deux joueurs sont connectés.
     */
    private void playerConnected() {
        if (this.data.state != State.PLAYING) {
            this.data.setWait();
        }

        if (this.X_player_id >= 0 && this.O_player_id >= 0) // Lancer partie si 2 joueurs connectés
            this.data.setStart();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnect(int player_id) throws RemoteException {
        System.out.println("Déconnexion de " + player_id);

        if (this.X_player_id == player_id || this.O_player_id == player_id) {
            this.data.X_connected = false;
            this.data.O_connected = false;
            this.X_player_id = -5;
            this.O_player_id = -5;
            this.data.setWait();
        }

        return;
    }

    /**
     * Retourne l'index des cases formant le combo gagnant de la partie, l'array
     * {-1} en cas d'égalité,
     * sinon null si la partie n'est pas terminée.
     * Exemple : {0,1,2} pour la première ligne de la grille.
     * 
     * @return le combo gagnant de la partie, l'array {-1} en cas d'égalité,
     *         sinon null si la partie n'est pas terminée.
     * @throws RemoteException
     */
    private int[] winningCombo() throws RemoteException {

        // Test lignes
        for (int i = 0; i < 3; i++)
            if (data.grid[i * 3] != ' ' && (data.grid[i * 3] == data.grid[(i * 3) + 1])
                    && (data.grid[(i * 3) + 1] == data.grid[(i * 3) + 2]))
                return new int[] { i * 3, (i * 3) + 1, (i * 3) + 2 };

        // Test colonnes
        for (int j = 0; j < 3; j++)
            if (data.grid[j] != ' ' && (data.grid[j] == data.grid[3 + j]) && (data.grid[3 + j] == data.grid[6 + j]))
                return new int[] { j, 3 + j, 6 + j };

        // Test diagonales
        if (data.grid[0] != ' ' && (data.grid[0] == data.grid[4]) && (data.grid[4] == data.grid[8]))
            return new int[] { 0, 4, 8 };
        if (data.grid[2] != ' ' && (data.grid[2] == data.grid[4]) && (data.grid[4] == data.grid[6]))
            return new int[] { 2, 4, 6 };

        // Test d'égalité = grille pleine
        for (int i = 0; i < 9; i++)
            if (data.grid[i] == ' ')
                return null;

        // Atteint si la grille est pleine
        return new int[] { -1 };
    }

    /**
     * Créé un TicTacToe, instancie une structure TTT_Data().
     * 
     * @throws RemoteException -
     */
    public TicTacToe() throws RemoteException {
        this.X_player_id = -5;
        this.O_player_id = -5;

        this.data = new TTT_Data();
    }
}
