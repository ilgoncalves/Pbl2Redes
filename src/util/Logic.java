/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import controller.CarController;
import controller.Controller;
import java.util.ArrayList;

/**
 * Classe responsavel por tratar as logicas relacionadas a imagem, em que traça
 * o trajeto do carro e verifica se tem conflito
 *
 * @author Igor
 */
public class Logic {

    private Controller controller;
    private CarController myCar;
    private boolean conflict = false;

    public Logic(CarController myCar) {
        this.myCar = myCar;
        this.controller = Controller.getInstance();
    }

    /**
     * Classe que verifica se tem ou não conflito
     *
     * @return
     */
    public boolean conflict() {
        if (controller.getCars().size() > 1) {//verifica se tem carro conectado
            for (int l = 0; l < controller.getCars().size(); l++) {//caso tenha,
                CarController currentCar = Controller.getInstance().getCars().get(l);
                int j = 0;
                //verifica qual o maior trajeto, para basear se vai ter conflito
                if (myCar.getRoute().size() > currentCar.getRoute().size()) {
                    j = currentCar.getRoute().size();
                } else {
                    j = myCar.getRoute().size();
                }
                //busca direta, para verificar se vai ter conflito
                if (currentCar.getId() != myCar.getId()) {//verifica se o carro não é o meu
                    conflict = false;
                    for (int i = 0; i < j; i++) {//percorre carro por carro, analisando se o carro ja esta no cruzamento ou se ta na via principal
                        if (currentCar.getRoute().get(i).getName().equals(myCar.getRoute().get(i).getName())) {
                            if (currentCar.getRoute().get(0).getName().equals("A") || currentCar.getRoute().get(0).getName().equals("B") || currentCar.getRoute().get(0).getName().equals("C") || currentCar.getRoute().get(0).getName().equals("D")) {
                            } else if (currentCar.getAtCrossroad() == true) {
                                conflict = true;
                            }
                        }
                    }
                    return conflict;
                }
            }
        }
        return false;
    }

    /**
     * Calcula apartir da origem e do destino, qual o trajeto a ser feito
     *
     * @param origem
     * @param destino
     * @return
     */
    public ArrayList<Quadrant> calculateRoute(String origem, String destino) {
        ArrayList<Quadrant> route = new ArrayList<>();
        switch (origem) {
            case "A":
                route.add(new Quadrant("A"));
                route.add(new Quadrant("d"));
                switch (destino) {
                    case "B":
                        route.add(new Quadrant("B"));
                        return route;
                    case "C":
                        route.add(new Quadrant("b"));
                        route.add(new Quadrant("C"));
                        return route;
                    default:
                        route.add(new Quadrant("b"));
                        route.add(new Quadrant("a"));
                        route.add(new Quadrant("D"));
                        return route;
                }
            case "B":
                route.add(new Quadrant("B"));
                route.add(new Quadrant("b"));
                switch (destino) {
                    case "C":
                        route.add(new Quadrant("C"));
                        return route;
                    case "D":
                        route.add(new Quadrant("a"));
                        route.add(new Quadrant("D"));
                        return route;
                    default:
                        route.add(new Quadrant("a"));
                        route.add(new Quadrant("c"));
                        route.add(new Quadrant("A"));
                        return route;
                }
            case "C":
                route.add(new Quadrant("C"));
                route.add(new Quadrant("a"));
                switch (destino) {
                    case "D":
                        route.add(new Quadrant("D"));
                        return route;
                    case "A":
                        route.add(new Quadrant("c"));
                        route.add(new Quadrant("A"));
                        return route;
                    default:
                        route.add(new Quadrant("c"));
                        route.add(new Quadrant("d"));
                        route.add(new Quadrant("B"));
                        return route;
                }
            default:
                route.add(new Quadrant("D"));
                route.add(new Quadrant("c"));
                switch (destino) {
                    case "A":
                        route.add(new Quadrant("A"));
                        return route;
                    case "B":
                        route.add(new Quadrant("d"));
                        route.add(new Quadrant("B"));
                        return route;
                    default:
                        route.add(new Quadrant("d"));
                        route.add(new Quadrant("b"));
                        route.add(new Quadrant("C"));
                        return route;
                }
        }
    }
}
