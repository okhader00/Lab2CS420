package bssCS420;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

public class BSSManager extends UnicastRemoteObject implements BSSManagerInterface {

	private int tokenHolderId;
    private Queue<String> messageQueue;
    private int totalProcesses;
    private Vector<Integer> vectorClock;
    
    public BSSManager(int totalProcesses) throws RemoteException {
        this.tokenHolderId = 0;  // Process 0 starts with token
        this.messageQueue = new LinkedList<>();
        this.totalProcesses = totalProcesses;
        this.vectorClock = new Vector<>(totalProcesses);
        for (int i = 0; i < totalProcesses; i++) {
            vectorClock.add(0);
        }
    }
    
    @Override
    public synchronized void send(String message) throws RemoteException {
        //Split message to obtain vector clock and process ID
        //Ex: message|[1,0,0]
        String[] parts = message.split("\\|");
        String msgContent = parts[0];
        Vector<Integer> msgClock = parseClock(parts[1]);
        System.out.println("BSSManager received message: '" + msgContent + "' with vector clock: " + msgClock);

        if (tokenHolderId == getProcessIdFromClock(msgClock)) {	//Message delivered only if sent by token holder
        	System.out.println("Process " + tokenHolderId + " holds the token, delivering message: '" + msgContent + "'");
            //Dequeue and deliver messages in causal order
            while (!messageQueue.isEmpty()) {
                String nextMessage = messageQueue.poll();
                ProcessInterface recipient = getRecipient(nextMessage);
                recipient.deliver(nextMessage);
            }
            releaseToken();
        } else {	
        	//If message sent by process who is not token holder, it is placed in queue
        	System.out.println("Message added to queue: '" + msgContent + "'");
            messageQueue.add(message);
        }
    }
    
    @Override
    public void releaseToken() throws RemoteException {
        tokenHolderId = (tokenHolderId + 1) % totalProcesses;	//Token passed in round robin fashion
        System.out.println("Token passed to process " + tokenHolderId);
    }
    
    private Vector<Integer> parseClock(String clockStr) {
        //Obtaining vector clock from string
        String[] values = clockStr.replaceAll("[\\[\\]]", "").split(",");
        Vector<Integer> clock = new Vector<>();
        for (String value : values) {
            clock.add(Integer.parseInt(value.trim()));
        }
        return clock;
    }
    
    public int getProcessIdFromClock(Vector<Integer> vectorClock) {
        int max = -1; 
        int processId = -1; 
        
        //Iterate through the vector clock to find the process with the highest clock value
        for (int i = 0; i < vectorClock.size(); i++) {
            if (vectorClock.get(i) > max) {
                max = vectorClock.get(i);
                processId = i;  //Process ID should be index of the highest clock value
            }
        }

        return processId;
    }
    
    private ProcessInterface getRecipient(String message) throws RemoteException {
        //Finding which process to send to
        try { 
        	int processId = getProcessIdFromClock(vectorClock);
            return (ProcessInterface) Naming.lookup("//localhost/Process" + processId);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace();
			return null;
		}
    }
}
