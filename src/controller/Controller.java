/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.concurrent.atomic.AtomicBoolean;
import model.Auxiliary;
import util.Quadrant;

/**
 *
 * @author Igor
 */
public class Controller {

    private static Controller controller;
    private Auxiliary aux;
    private ArrayList<String> ips;
    private ArrayList<CarController> cars;
    private int counter;
    private String myIp;
    private AtomicBoolean value;

    public Controller(String myIp) {
        aux = new Auxiliary(this);
        ips = new ArrayList<>();
        cars = new ArrayList<>();
        counter = 0;
        this.myIp = myIp;
        value = new AtomicBoolean();
    }

    public static Controller newController(String ip) {
        controller = new Controller(ip);
        return controller;
    }

    public static Controller getInstance() {
        return controller;
    }

    public void firstConection(String ip) {
        while (!value.get()) {
            value.set(true);
            if (checkIp(ip)) {
                ips.add(ip);
                aux.firstConection(ip);
            }
        }
        value.set(false);
    }

    public void startConection(String ip) {
        while (!value.get()) {
            value.set(true);
            if (checkIp(ip)) {
                ips.add(ip);
                aux.startConection(ip);
            }
        }
        value.set(false);
    }

    public void removeIp(String ip) {
        ips.remove(ip);
        aux.removeClient(ip);
    }

    private boolean checkIp(String ip) {
        if (ip.trim().equals(myIp.trim())) {
            return false;
        } else {
            for (String currentIp : ips) {
                if (currentIp.equals(ip)) {
                    return false;
                }
            }
        }
        return true;
    }

    public ArrayList<String> getIps() {
        return this.ips;
    }

    public void replicateMsg(String msg) {
        aux.replicateMsg(msg);
    }

    public void replicateMsg(ArrayList<Object> msg) {
        aux.replicateMsg(msg);
    }

    public void addCar(int id, String origin, String destiny) {
        CarController c = new CarController(id, origin, destiny);
        cars.add(c);
        counter++;
    }

    public void addCar(int id, float x, float y, int direction, ArrayList<Quadrant> route) {
        CarController c = new CarController(id, x, y, direction, route);
        cars.add(c);
        counter++;
    }

    public void removeCar(int id) {
        try {
            for (CarController c : cars) {
                if (c.getId() == id) {
                    cars.remove(c);
                    counter--;
                }
            }
        } catch (ConcurrentModificationException ex) {
            this.removeCar(id);
        }
    }

    public ArrayList<CarController> getCars() {
        ArrayList<CarController> auxCar = new ArrayList<>();
        for (int i = 0; i < this.counter; i++) {
            auxCar.add(this.cars.get(i));
        }
        return auxCar;
    }

    public ArrayList<CarController> getCars1() {
        ArrayList<CarController> auxCar = new ArrayList<>();
        for (int i = 1; i < this.counter; i++) {
            auxCar.add(this.cars.get(i));
        }
        return auxCar;
    }

    public CarController getCar(int id) {
        try {
            for (CarController car : cars) {
                if (car.getId() == id) {
                    return car;
                }
            }
            return null;
        } catch (Exception e) {
            this.getCar(id);
        }
        return null;
    }
}
