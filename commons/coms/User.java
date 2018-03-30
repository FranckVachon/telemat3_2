package coms;

import java.util.ArrayList;

import observerPattern.Observer;

public class User implements Observer {

	int id;
	String username;
	String password;
	ArrayList<Salle> sallesSuscribed ;   
	ArrayList<Messge> msgUsagers ;   
	
	public User(String username, String password, int id) {
		this.password = password;
		this.username = username;
		this.id = id;
	}
	public User(String username, String password, ArrayList<Salle> sallesSuscribed, ArrayList<Messge> msgUsagers) {
		this.password = password;
		this.username = username;
		this.sallesSuscribed= sallesSuscribed;
		this.msgUsagers = msgUsagers;
	}

	
	public String toString() {
		return "username: " + username +" pass: " + password +" id:" + id;
	}
	@Override
	public void update(Messge msg) {
		//Ceci est applé lorsqu'une salle à laquelle l'utilisateur est suscribé reçoit un nouveau message
		//Pour l'instant on se contente d'imprimer sur console qu'on a été averti de l'ajout 
		System.out.println("");
		System.out.println("Ajout d'un nouveau message dans une salle d'intérêt pour cet usager");
		System.out.println("(UDP client a l'Usager par défaut 999 et envoie à salleId: 1)");

		System.out.println("Usagé abonné id: " + id + " message ajouté: " + msg.getContent() + " par utilisateur id:" +msg.getuserId());
	}
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ArrayList<Salle> getSallesSuscribed() {
		return sallesSuscribed;
	}

	public void setSallesSuscribed(ArrayList<Salle> sallesSuscribed) {
		this.sallesSuscribed = sallesSuscribed;
	}

	public ArrayList<Messge> getMsgUsagers() {
		return msgUsagers;
	}

	public void setMsgUsagers(ArrayList<Messge> msgUsagers) {
		this.msgUsagers = msgUsagers;
	}

}
