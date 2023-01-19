import java.rmi.*;
import java.rmi.server.*;
import java.util.ArrayList;
import java.util.Random;

class TicTacToe extends UnicastRemoteObject implements ServerInterface {

    private int[] matrix;
    
    private TicTacToeData data;
    
    private ArrayList<Integer> players;
    private int next_player_id;

    /**
     * Permet au joueur de l'index donné de joeur son tour, et actualise l'état général du jeu.
     * @return true si le joueur a bien pu jouer, false dans le cas contraire.
     */
    public boolean playTurn(int player_id, int cell_index) throws RemoteException {
        if(next_player_id != player_id)
            return false;

        if(matrix[cell_index] != 0)
            return false;
        
        if(cell_index >= 9)
            return false;
        
        matrix[cell_index] = player_id;
        data.last_move_index = cell_index;
    
        data.winningCombo = winningCombo();

        if(data.winningCombo == null)
            data.state = TicTacToeData.State.PLAYING;
        else if(data.winningCombo[0] == -1)
            data.state = TicTacToeData.State.DRAW;

        next_player_id = players.get(players.indexOf(player_id) ^ 1); //Inverser le joueur

        return true;
    }

    /**
     * 
     */
    public TicTacToeData fetchData(int player_id) throws RemoteException {
        if(players.contains(player_id)) {
            data.shouldPlay = (player_id == next_player_id);
            data.opponent_connected = (players.size() == 2);
            if((data.winningCombo != null) && (data.winningCombo[0] != -1))
                data.state = (data.winningCombo[0] == player_id ? TicTacToeData.State.VICTORY : TicTacToeData.State.DEFEAT);
            return data;
        }

        return null;
    }

    
    /**
     * Connecte le client au jeu et retourne son identifiant.
     * Retourne -1 s'il y a déjà 2 clients de connecté (donc impossible d'ajouter un nouveau client), sinon retourne l'id du client.
     * @return
     */
    public int connect() throws RemoteException {
        if(players.size() >= 2)
            return -1;

        players.add((int)Math.floor(Math.random()*1000));

        return players.get(players.size()-1);  
    }  


    /**
     * Retire le client de la liste des joueurs.
     * @return true si le client a réussi a se deconnecter, sinon retourne false.
     */
    public boolean disconnect(int player_id) throws RemoteException {
        if (players.contains(player_id)) {
            players.remove(players.indexOf(player_id));
            return true;
        }
        return false;
    }
    

    /**
     * Retourne l'index des cases formant le combo gagnant de la partie, l'array {-1} en cas d'égalité,
     * sinon null si la partie n'est pas terminée.
     * Exemple : {0,1,2} pour la première ligne de la grille.
     * @return le combo gagnant de la partie, l'array {-1} en cas d'égalité,
     * sinon null si la partie n'est pas terminée.
     */
    private final int[] winningCombo() throws RemoteException {

        //Test lignes
        for(int i = 0; i < 3; i++)
            if((matrix[i] == matrix[i+1]) && (matrix[i+1] == matrix[i+2]))
                return new int[] {i+1, i+2, i+3};

        //Test colonnes
        for(int j = 0; j < 3; j++)
            if((matrix[j] == matrix[3+j]) && (matrix[3+j] == matrix[6+j]))
                return new int[] {j, 3+j, 6+j};


        //Test diagonales
        if((matrix[0] == matrix[4]) && (matrix[4] == matrix[8]))
            return new int[] {0, 4, 8};
        if((matrix[2] == matrix[4]) && (matrix[4] == matrix[6]))
            return new int[] {2, 4, 6};
                
        return null;
    }


    public TicTacToe() throws RemoteException {
        next_player_id = 0;
        matrix = new int[9];
        players = new ArrayList<Integer>();
        data = new TicTacToeData();
        
        data.shouldPlay = false;
        data.opponent_connected = false;
        data.last_move_index = -1;
        data.state = TicTacToeData.State.WAITING;
        data.winningCombo = null;
        
    }
}


public class Server {

    public static void main(String[] args) {
        try {
            TicTacToe game = new TicTacToe();
            Naming.rebind("rmi:///TicTacToe", game); //Lier l'objet dans le registry
        } 
        catch (Exception e) {
            System.out.println("Erreur de liaison de l'objet TicTacToe");
            System.out.println(e.toString());
        }
    }
}
