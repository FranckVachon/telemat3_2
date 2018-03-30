package coms;

import java.util.ArrayList;
import java.util.List;

import observerPattern.Observer;
import observerPattern.Subject;
public class Salle implements Subject {
	/**/
	
	String salleNom;
	int id;
	int likeCount;
	int dislikeCount;
	private List<Observer> suscribersSalle = new ArrayList<>();
	private List<Messge> messages = new ArrayList<>();

	//Constructeur pour reconstituer une salle déjà en mémoire
	public Salle(String salleNom, int id, int likeCount, int dislikeCount, ArrayList<User> suscribersList, ArrayList<Messge> messagesList) {
		this.salleNom = salleNom;
		this.id = id;
		this.dislikeCount = dislikeCount;
		this.likeCount = likeCount;
		
	}
	
	//création d'une nouvelle salle
	public Salle(String salleNom, int id) {
		this.salleNom = salleNom;
		this.id = id;
		this.dislikeCount = 0;
		this.likeCount = 0;
	}

	
	public String toString() {
		return salleNom + " id:" + id + "list suscriber:" + getSuscriberSalle().toString();

	}

	private Object getSuscriberSalle() {
		return suscribersSalle;
	}
	
	public void addNewMessage(Messge msg) {
		//Nouveau message reçu par UDP destiné à cette salle-ci & notifier les observateurs
		messages.add(msg);
		notifyObs(msg);
	}

	public String getSalleNom() {
		return salleNom;
	}

	public void setSalleNom(String salleNom) {
		this.salleNom = salleNom;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	public int getDislikeCount() {
		return dislikeCount;
	}

	public void setDislikeCount(int dislikeCount) {
		this.dislikeCount = dislikeCount;
	}

	@Override
	public void attachObserver(Observer o) {
		suscribersSalle.add(o);	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return suscribersSalle.size();
	}

	@Override
	public List<Observer> getObserverList() {
		// TODO Auto-generated method stub
		return suscribersSalle;
	}

	@Override
	public void notifyObs(Messge msg) {
		for (Observer o : suscribersSalle) {
			o.update(msg);
		}
		
	}

}
	
	
	