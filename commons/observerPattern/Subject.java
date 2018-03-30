package observerPattern;

import java.util.List;

import coms.Messge;


public interface Subject {

    public void attachObserver(Observer o);
    
    //Returns the number of observers
    public int getCount();
            
    public List<Observer> getObserverList();

	public void notifyObs(Messge o);
}

