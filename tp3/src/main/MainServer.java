package main;

import coms.Utils;

public final class MainServer {
	
    public static void main(String[] args) throws Exception {	
    	//Ports (défaut dans Utils, sinon spécifiés par ligne de command
    	int portServerUDP = Utils.udpPort;
    	int portServerTCP = Utils.tcpPort;
    	if (args.length==2) {
    		portServerUDP = Integer.parseInt(args[0]);
    		portServerTCP = Integer.parseInt(args[1]);
		} else if (args.length==1) {
    		portServerUDP = Integer.parseInt(args[0]);
		}
    	
    
    	//Démarrage du thread UDP -
    	System.out.println("Demarrage thread TCP...");
    	//TCP est un singleton, d'ou le getInstance()
    	SocketTCP.getInstance("tcpHost",portServerUDP, portServerTCP).start();
    	
    	//Démarrage du thread UDP 
    	System.out.println("Demarrage thread UDP...");
        new SocketUDP("udpHost", portServerUDP, portServerTCP).start();
        

    }

    
}



