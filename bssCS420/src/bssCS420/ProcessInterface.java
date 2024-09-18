package bssCS420;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ProcessInterface extends Remote{
	void send(String message) throws RemoteException;
    void deliver(String message) throws RemoteException;
    void getToken() throws RemoteException;
}
