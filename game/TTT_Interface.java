package game;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TTT_Interface extends Remote {
    public void playTurn(int player_id, int cell_index) throws RemoteException;
    public TTT_Data fetchData() throws RemoteException;
    public int connectAsX() throws RemoteException;
    public int connectAsO() throws RemoteException;
    public void disconnect(int player_id) throws RemoteException;
}