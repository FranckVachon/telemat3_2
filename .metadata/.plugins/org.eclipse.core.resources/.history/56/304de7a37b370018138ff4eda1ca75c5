package main;

import coms.Utils;

public final class MainServer {
	
    public static void main(String[] args) throws Exception {	
    	int portServerUDP = Utils.udpPort;
    	int portServerTCP = Utils.tcpPort;

    	//Run arguments - 2 args = port udp et TCP sont spécifiés autre que les valeurs par défaut
    	if (args.length==2) {
    		portServerUDP = Integer.parseInt(args[0]);
    		portServerTCP = Integer.parseInt(args[1]);
		} else if (args.length==1) {
			//seulement port UDP est spécifié
    		portServerUDP = Integer.parseInt(args[0]);
		}
    	
    	
    	
    	//Démarrage du thread UDP - Utils.mainPort, même host
    	System.out.println("Demarrage thread TCP...");
    	SocketTCP.getInstance("tcpHost",portServerUDP, portServerTCP).start();
    	
    	//Démarrage du thread UDP - Utils.udpPort
    	System.out.println("Demarrage thread UDP...");
        new SocketUDP("udpHost", portServerUDP, portServerTCP).start();
        

    }

    
}



