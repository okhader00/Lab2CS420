package bssCS420;

import java.rmi.Naming;

public class ClientRMI {

	public static void main(String[] args) {
		try {
			
			ProcessInterface process0 = (ProcessInterface) Naming.lookup("//localhost/Process0");
            System.out.println("Process 0 is sending a message.");	//Client side message to ensure processes are bound correctly
            process0.send("Hello from Process 0");	//Server side message that it sent from the process
            
            ProcessInterface process1 = (ProcessInterface) Naming.lookup("//localhost/Process1");
            System.out.println("Process 1 is sending a message.");
            process1.send("Hello from Process 1");
            
            ProcessInterface process2 = (ProcessInterface) Naming.lookup("//localhost/Process2");
            System.out.println("Process 2 is sending a message.");
            process2.send("Hello from Process 2");	//Simulating events and message sending
            
            process1.send("This is process 1 for the 2nd time");
            
            process2.send("This is process 2 for the 2nd time");
            
            process0.send("This is process 0 for the 2nd time");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

}
