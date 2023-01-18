
import java.rmi.Remote;
import java.rmi.RemoteException;

class TicTacToeData {
    public enum State {
        PLAYING, DRAW, VICTORY, DEFEAT, WAITING
    }

    public boolean shouldPlay;
    public boolean opponent_connected;
    public int last_move_index;
    public State state;
    public int[] winningCombo;
}


public interface ServerInterface extends Remote {
    public boolean playTurn(int player_id, int cell_index) throws RemoteException;
    public TicTacToeData fetchData(int player_id) throws RemoteException;
    public int connect() throws RemoteException;
    public boolean disconnect(int player_id) throws RemoteException;
}
