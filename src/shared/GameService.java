package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface GameService extends Remote {
    String connect(String playerName) throws RemoteException;
    List<String> startRound(String playerName) throws RemoteException;
    List<String> hit(String playerName) throws RemoteException;
    String stand(String playerName) throws RemoteException;
    List<String> getDealerVisibleCard(String playerName) throws RemoteException;
}
