package view;


import java.util.Observable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


public class ClientModel extends Observable {

    private String [] name;
    private int [] age;
    private int index;

    public void setName(String name) {
        if (index < 3){
            this.name[index] = name;
        }
        index++;
    }

    public void setAge(int age) {
        if (index < 3) {
            this.age[index] = age;
        }
        index++;
    }
}
