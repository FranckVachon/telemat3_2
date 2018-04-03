package main;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import coms.Messge;
import coms.Salle;
import coms.User;
import observerPattern.Observer;
import observerPattern.Subject;

public class SocketTCP extends Thread {
	
	//###########################TODO: Throw away code - surtout pour la démo pas super pertinent comme structure
	private static SocketTCP instance;
	public static SocketTCP getInstance(String name, int portUDP, int portTCP) throws IOException {
		//Probablement destiné à partir, mais pour démo ça va
		if (instance==null) {
			instance = new SocketTCP(name, portTCP);
			return instance;
		}
		return instance;
	}
	
	public void addMsgSalle(Messge msg) {
		//Méthode qui pourrait être conservée - mais est-ce la job du socket TCPT d'ajouter un nouveau msg 
		//à une salle, ou bien est-ce que ce code va ailleurs... à voir.
		//Je voulais surtout pour l'ajouter rapidement et facilement pour avoir un prototype de départ...
		int idSalle = msg.getSalleId();
		for (Salle s: salles) {
			if (s.getId() == idSalle) {
				s.addNewMessage(msg);
			}
		}
	}
	//############################### FIN throw-away code
	

	// ####################### endpoint et params  ##########

	// Endpoint: creation salle
	private static String creationSalleURI = "/creationSalle";
	private static String salleNomParam = "salleNom";

	// Endpoint: creation usager
	private static String creationUsagerURI = "/creationUsager";
	private static String usagerNomParam = "username";
	private static String usagerPasswordParam = "password";

	// Endpoint: suscriber un usager a une salle
	private static String suscribeUsagerURI = "/suscribeUsagerSalle";
	private static String usagerIdParam = "userId";
	private static String salleIdParam = "salleId";
	// ####################### FIND endpoint et param ###########

	// ####################### Serveur addresse & port
	private HttpServer server = null;
	private static int serverIP = 0; // Pour l'instant - roule en localhost
	private static int inetSocketPort = 8000;

	// ####################### Listes pour gestion des objets/entités
	private List<Salle> salles = new ArrayList<>();
	private List<User> usagers = new ArrayList<>();

	private SocketTCP(String name, int portTCP) throws IOException {
		super(name);
		inetSocketPort = portTCP;
		server = HttpServer.create(new InetSocketAddress(inetSocketPort), serverIP);
		System.out.println("server:" + super.getName() + " on port:" + inetSocketPort);
	}
	
	public void run() {
		// CREATION des contextes
		server.createContext(creationSalleURI, new CreationSalleHandler());
		server.createContext(creationUsagerURI, new CreationUsagerHandler());
		server.createContext(suscribeUsagerURI, new SuscribeUsagerHandler());
		server.setExecutor(null); // default exec - ils le font dans la doc... sais pas si essentiel ou non
		server.start();

	}

	//###################### HANDLERS pour endpoints ##################################
	//Chaque handler a une section surtout destinée à printer à la console pas super utile long terme, surtout pour démo/test
	//la logique de réponse au browser est probablement pertinente par contre
	
	class CreationSalleHandler implements HttpHandler {
		/*
		 * Création d'une salle
		 */
		@Override
		public void handle(HttpExchange t) throws IOException {

			Map<String, String> params = parseQueryString(t.getRequestURI().getQuery());

			// Creer la nouvelle salle 
			attachSalle(new Salle(params.get(salleNomParam), getSalleCount()));

		
			// #######################BEGIN DEBUG PRINT
			String lineREturn = System.lineSeparator();
			String response = "Creation d'une nouvelle salle" + lineREturn + salleNomParam + ": "
					+ params.get(salleNomParam) + " nmb total salles: " + getSalleCount();
			response += serverStateResponse();
			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
			// ################### END DEBUG PRINT
		}
	}

	class CreationUsagerHandler implements HttpHandler {

		/* Gérer la création d'une nouvel usager */
		@Override
		public void handle(HttpExchange t) throws IOException {
			// localhost:inetSocketPort/creationUsager?username=billybob&password=secrepass

			Map<String, String> params = parseQueryString(t.getRequestURI().getQuery());

			// Creer nouvel usager & attacher au observer pattern
			attachUsager(new User(params.get(usagerNomParam), params.get(usagerPasswordParam), getUserCount()));

			// #######################BEGIN DEBUG PRINT
			String lineREturn = System.lineSeparator();
			String response = "Creation d'une nouvel usager" + lineREturn + usagerNomParam + ": "
					+ params.get(usagerNomParam) + " pass: " + params.get(usagerPasswordParam) + " nmb total usagers: "
					+ getUserCount() + lineREturn;
			String resp = response + serverStateResponse();
			t.sendResponseHeaders(200, resp.length());
			OutputStream os = t.getResponseBody();
			os.write(resp.getBytes());
			os.close();
			// ################### END DEBUG PRINT

		}
	}

	class SuscribeUsagerHandler implements HttpHandler {
		// localhost:inetSocketPort/suscribeUsagerSalle?userId=1&salleId=01
		public void handle(HttpExchange t) throws IOException {

			Map<String, String> params = parseQueryString(t.getRequestURI().getQuery());

			// Suscriber revient à enregistrer l'Utilisateur comme observateur de la salle
			// la salle est subject, et l'utilisateur observer

			// 1 - Trouver la salle correspondante au salleId
			Salle salle = null;
			User user = null;
			List<Object> l = findObjectsFromIds(params.get(salleIdParam), params.get(usagerIdParam));

			if (l.size() == 2) {
				// 2 - Attacher l'usager spécifié à la salle
				salle = (Salle) l.get(1);
				user = (User) l.get(0);
				salle.attachObserver(user);
			} else {
				System.out.println("Impossible de trouver objets pour id spécifié - essayer d'autres id");
			}

			// #######################BEGIN DEBUG PRINT
			String lineREturn = System.lineSeparator();
			String response = "Attache user: " + user.toString() + " a salle: " + salle.toString();
			String resp = response + serverStateResponse();
			t.sendResponseHeaders(200, resp.length());
			OutputStream os = t.getResponseBody();
			os.write(resp.getBytes());
			os.close();
			// ################### END DEBUG PRINT
		}
		
		//###################### HANDLERS pour endpoints ##################################


		private List<Object> findObjectsFromIds(String salleId, String userId) {
			/*
			 * Matches the id provided (strings) to corresponding obj. No check for
			 * existence
			 */
			// TODO: checker pour non-existence d'un obj correspondant au ID et gérer ce cas
			List<Object> obj = new ArrayList<>();
			for (Salle s : getSalles()) {
				if (Integer.toString(s.getId()).equals(salleId)) {
					for (User u : getUsers()) {
						if (Integer.toString(u.getId()).equals(userId)) {
							obj.add(u);
							obj.add(s);
						}
					}
				}
			}
			return obj;
		}
	}

	public String serverStateResponse() {
		/*
		 * Cette méthode format une réponse qui donne un apperçu complet de l'état du
		 * serveur (salle usagers etc...) pour fin de debug surtout
		 */
		String response = System.lineSeparator() + "Users - sommaire de l'etat:" + System.lineSeparator();
		for (User u : getUsers()) {
			response += u.toString();
			response += System.lineSeparator();
		}
		response += System.lineSeparator() + "Salles - sommaire de l'etat:" + System.lineSeparator();

		for (Salle s : getSalles()) {
			response += s.toString();
			response += System.lineSeparator();
		}
		return response;
	}

	public Map<String, String> parseQueryString(String qs) {
		/**
		 * Prends en entrée un string, qui represente l'ensemble de la query qui a été
		 * envoyé au serveur En extrait les params avec "&" et les fous dans un hashmap
		 * Thanks to "Oliv" from stack overflow question #11640025 for code snippet
		 */
		Map<String, String> result = new HashMap<>();
		if (qs == null)
			return result;

		int last = 0, next, l = qs.length();
		while (last < l) {
			next = qs.indexOf('&', last);
			if (next == -1)
				next = l;

			if (next > last) {
				int eqPos = qs.indexOf('=', last);
				try {
					if (eqPos < 0 || eqPos > next)
						result.put(URLDecoder.decode(qs.substring(last, next), "utf-8"), "");
					else
						result.put(URLDecoder.decode(qs.substring(last, eqPos), "utf-8"),
								URLDecoder.decode(qs.substring(eqPos + 1, next), "utf-8"));
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e); // will never happen, utf-8 support is mandatory for java
				}
			}
			last = next + 1;
		}
		return result;
	}

	public void attachSalle(Salle s) {
		salles.add(s);
	}

	public void attachUsager(User u) {
		usagers.add(u);
	}

	public int getSalleCount() {
		return salles.size();
	}

	public int getUserCount() {
		return usagers.size();
	}

	public List<Salle> getSalles() {
		return salles;
	}

	public List<User> getUsers() {
		return usagers;
	}
	
	

}
