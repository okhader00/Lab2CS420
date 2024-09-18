package bssCS420;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BSSManagerInterface extends Remote {
	void send(String message) throws RemoteException;
    void releaseToken() throws RemoteException;
}
