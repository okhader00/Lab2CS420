package bssCS420;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

public class Process extends UnicastRemoteObject implements ProcessInterface {
	
	private Vector<Integer> vectorClock;
    private Queue<String> messageQueue;
    private BSSManagerInterface bssManager;
    private int processId;
    
    public Process(int processId, int totalProcesses, BSSManagerInterface manager) throws RemoteException {
        this.processId = processId;
        this.vectorClock = new Vector<>(totalProcesses);
        for (int i = 0; i < totalProcesses; i++) {
            vectorClock.add(0);
        }
        this.messageQueue = new LinkedList<>();
        this.bssManager = manager;
    }

    @Override
    public synchronized void send(String message) throws RemoteException {
        vectorClock.set(processId, vectorClock.get(processId) + 1); //Updating vector clock on message send
        String messageWithClock = message + "|" + vectorClock.toString();
        System.out.println("Process " + processId + " sent message: '" + message + "' with vector clock: " + vectorClock);
        bssManager.send(messageWithClock);
    }

    @Override
    public synchronized void deliver(String message) throws RemoteException {
        System.out.println("Delivered message to process " + processId + ": " + message);
        //Delivered message would be further processed for specific application needs
    }

    @Override
    public void getToken() throws RemoteException {
    }
	

}
