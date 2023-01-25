package com.tictactoe.game;

import java.io.Serializable;

public class TTT_Data implements Serializable {

    /**
     * État de la partie.
     */
    public enum State {
        /** La partie est en cours */
        PLAYING, 
        /** La partie résulte à une égalité */
        DRAW, 
        /** Un joueur a gagné la partie */
        VICTORY, 
        /** En attente de connexion des joueurs */
        WAITING
    }

    /** Vrai si un joueur X est connecté. */
    public boolean X_connected;

    /** Vrai si un joueur O est connecté. */
    public boolean O_connected;

    /**
     * Contient le marqueur (X ou O) du joueur devant jouer.
     * Vide sinon.
     */
    public char whoseTurn;

    /**
     * Etat de la partie.
     * @see TTT_Data.State
     */
    public State state;
    
    /**
     * Grille du jeu contenant "X" ou "O".
     * Si la case est vide, contient le caractère ' '.
     */
    public char[] grid;

    /**
     * Suite des trois identifiants des cases formant
     * la combinaison gagnante de la partie.
     */
    public int[] winningCombo;

    /**
     * Message affiché sur l'écran du client :
     *  - En attente des joueurs...
     *  - Au joueur X (ou O) de jouer.
     *  - Égalité.
     *  - Le joueur X (ou O) a gagné !
     */
    public String message;

    /**
     * Passe le tour du joueur au joueur suivant.
     * Si aucun joueur n'est définit, X commence.
     */
    void nextPlayer() {
        if(this.whoseTurn == 'X')
            this.whoseTurn = 'O';
        else
            this.whoseTurn = 'X';
    }

    void setWinner(char winner, int[] winningCombo) {
        this.state = State.VICTORY;
        this.message = winner + " a gagné !";
        this.winningCombo = winningCombo;
    }

    void setDraw() {
        this.state = State.DRAW;
        this.message = "Égalité.";
        this.winningCombo = null;
    }

    void setStart() {
        System.out.println("Commencer.");
        this.state = State.PLAYING;
        this.grid = new char[] {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
        this.message = "Au joueur " + this.whoseTurn + " de jouer.";
    }

    void setWait() {
        this.state = State.WAITING;
        this.message = "En attente des joueurs...";
        this.winningCombo = null;
    }

    TTT_Data() {
        X_connected = false;
        O_connected = false;
        this.whoseTurn = 'X';
        this.grid = new char[] {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
        this.setWait();
    }
}



