package bssCS420;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class ServerRMI {

	public static void main(String[] args) {
		try {
            LocateRegistry.createRegistry(1099);	//Initializing and binding BSSManager
            BSSManagerInterface manager = new BSSManager(3);  
            Naming.rebind("//localhost/BSSManager", manager);

            for (int i = 0; i < 3; i++) {		//Initializing processes and assigning them to BSSManager
                ProcessInterface process = new Process(i, 3, manager);
                Naming.rebind("//localhost/Process" + i, process);
            }

            System.out.println("BSS Manager and Processes are ready.");
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

}
