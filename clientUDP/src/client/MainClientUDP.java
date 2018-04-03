package client;
import coms.Utils;


public final class MainClientUDP {
	
    public static void main(String[] args) {	
    	int portServerUDP = Utils.udpPort;
    	if (args.length>0) {
    		portServerUDP = Integer.parseInt(args[0]);
		} 
    	
    	System.out.println("Démarrage client UDP. PortUDP (serveur):" + portServerUDP);
    	new ClientUDP(portServerUDP).start();

    }

    
}



