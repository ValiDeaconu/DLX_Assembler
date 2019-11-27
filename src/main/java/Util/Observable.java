package Util;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable {
    private List<Observer> observers;

    protected Observable() {
        observers = new ArrayList<>();
    }

    public boolean subscribe(Observer observer) {
        return observers.add(observer);
    }

    public boolean unsubscribe(Observer observer) {
        return observers.remove(observer);
    }

    public void notify(String notification) {
        for (Observer observer : observers) {
            observer.update(this, notification);
        }
    }
}
