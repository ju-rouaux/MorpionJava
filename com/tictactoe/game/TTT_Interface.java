package com.tictactoe.game;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface pour appeler l'objet TicTacToe via protocole RMI.
 * 
 * @author Julien Rouaux
 */
public interface TTT_Interface extends Remote {
    /**
     * Place le symbole du joueur sur la grille, seulement si ce joueur a le droit
     * de jouer.
     * Si le mouvement du joueur est gagnant, la partie est arrêtée.
     * 
     * @param player_id  l'identifiant de joueur voulant jouer.
     * @param cell_index index de la case sur laquelle jouer.
     * @return vrai si le symbole a été placé, faux sinon.
     * @throws RemoteException { }
     */
    public boolean playTurn(int player_id, int cell_index) throws RemoteException;

    /**
     * Retourne les données de la partie.
     * 
     * @return les données de la partie.
     * @throws RemoteException { }
     */
    public TTT_Data fetchData() throws RemoteException;

    /**
     * Permet au joueur de se connecter en tant que X à la partie.
     * La partie est lancée si deux joueurs sont connectés.
     * Retourne un identifiant compris dans l'interval [0, 499].
     * 
     * @return l'identifiant, -1 si la place est prise.
     * @throws RemoteException { }
     */
    public int connectAsX() throws RemoteException;

    /**
     * Permet au joueur de se connecter en tant que O à la partie.
     * La partie est lancée si deux joueurs sont connectés.
     * Retourne un identifiant compris dans l'interval [500, 999].
     * 
     * @return l'identifiant, -1 si la place est prise.
     * @throws RemoteException { }
     */
    public int connectAsO() throws RemoteException;

    /**
     * Déconnecte le joueur de la partie.
     * Si la partie est en cours, elle est arrêtée.
     * Si le joueur était en attente d'un autre joueur, il libère sa place.
     * 
     * @param player_id l'identifiant du joueur souhaitant se déconnecter.
     * @throws RemoteException { }
     */
    public void disconnect(int player_id) throws RemoteException;
}