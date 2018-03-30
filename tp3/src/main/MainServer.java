package main;    

public final class MainServer {
	
    public static void main(String[] args) throws Exception {	

    	System.out.println("Demarrage thread TCP...");
    	//Démarrage du thread UDP - Utils.mainPort, même host
    	SocketTCP.getInstance("tcpHost").start();
    	
    	//Démarrage du thread UDP - Utils.udpPort
    	System.out.println("Demarrage thread UDP...");
        new SocketUDP("udpHost").start();
        

    }

    
}



