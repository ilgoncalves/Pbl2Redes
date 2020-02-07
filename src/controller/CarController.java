/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import util.Car;
import util.Logic;
import util.Quadrant;
import view.Begin;

/**
 * Classe responsavel por controlar as ações dos carros
 *
 * @author Igor
 */
public class CarController {

    private float x; //posição x do carro
    private float y; //posição y do carro
    public Car car; //dados do carro a ser controlado
    private int screenHeight = 480;
    private int screenWidth = 482;
    private int direction;
    private float speed = 0.6f; //velocidade do carro
    private ArrayList<Quadrant> route;
    private int id; //id do carro a ser controlado
    private String origin;
    private String destiny;
    private Logic logic;
    private boolean atCrossroad = false; //boolean que verifica se o carro ja entrou no cruzamento

    /**
     * Construtor do meu carro
     *
     * @param id
     * @param origin
     * @param destiny
     */
    public CarController(int id, String origin, String destiny) {
        this.id = id;
        this.origin = origin;
        this.destiny = destiny;
        this.logic = new Logic(this);
        this.route = logic.calculateRoute(origin, destiny);//gera o trajeto do carro
        Begin.getInstance().show("Iniciando Carro " + "azul" + " em pista " + origin); //exibe mensagem
        this.setup();
    }

    /**
     * contrutor dos outros carros
     *
     * @param id
     * @param x
     * @param y
     * @param direction
     * @param route
     */
    public CarController(int id, float x, float y, int direction, ArrayList<Quadrant> route) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.route = route;
        String color;
        //verifica qual a cor do carro
        switch (this.id) {
            case 0:
                color = "azul";
                break;
            case 1:
                color = "amarelo";
                break;
            case 2:
                color = "verde";
                break;
            case 3:
                color = "preto";
                break;
            default:
                color = "vermelho";
                break;
        }
        Begin.getInstance().show("Iniciando Carro " + color + " em pista " + route.get(0).getName());
        this.setup2();
    }

    public int getId() {
        return this.id;
    }

    /**
     * cria o carro que se conecta com seu serversocket
     */
    private void setup2() {
        switch (direction) {
            case 0:
                car = new Car(this.x, this.y, direction);
                break;
            case 9:
                car = new Car(this.x, this.y, direction);
                break;
            case 6:
                car = new Car(this.x, this.y, direction);
                break;
            case 3:
                car = new Car(this.x, this.y, direction);
                break;
        }
        this.origin = route.get(0).getName();
        this.destiny = route.get(route.size() - 1).getName();
    }

    /**
     * cria seu carro
     */
    private void setup() {
        switch (origin) {
            case "A":
                this.x = 257;
                this.y = screenHeight;
                this.direction = 0;
                car = new Car(this.x, this.y, direction);
                break;
            case "B":
                this.x = screenWidth;
                this.y = 208;
                this.direction = 9;
                car = new Car(this.x, this.y, direction);
                break;
            case "C":
                this.x = 210;
                this.y = 0;
                this.direction = 6;
                car = new Car(this.x, this.y, direction);
                break;
            case "D":
                this.x = 0;
                this.y = 257;
                this.direction = 3;
                car = new Car(this.x, this.y, direction);
                break;
        }
        this.sendMessage();
    }

    /**
     * coloca a cor do carro
     *
     * @param g2d
     */
    public void draw(Graphics2D g2d) {
        switch (this.id) {
            case 0:
                g2d.setColor(Color.BLUE);
                break;
            case 1:
                g2d.setColor(Color.YELLOW);
                break;
            case 2:
                g2d.setColor(Color.GREEN);
                break;
            case 3:
                g2d.setColor(Color.BLACK);
                break;
            default:
                g2d.setColor(Color.RED);
                break;
        }

        g2d.fill(car.getRect());

    }

    public Rectangle2D getRect() {
        return this.car.getRect();
    }

    public ArrayList<Quadrant> getRoute() {
        return route;
    }

    /**
     * metodo que faz o carro se movimentar na tela e verifica se o carro ainda
     * esta no quadrante, caso não esteja ele remove o quadrante do trajeto
     */
    public void move() {
        switch (direction) {
            case 0:
                if (!route.get(0).stillQuadrantY(y)) {
                    if (route.size() > 1) {
                        Begin.getInstance().show("Carro azul saindo da pista " + route.get(0).getName());
                        route.remove(0);
                        Begin.getInstance().show("Carro azul entrando na pista " + route.get(0).getName());
                    }
                }
                y = y - speed;
                car.setXY(x, y);
                break;
            case 3:
                if (!route.get(0).stillQuadrantX(x)) {
                    if (route.size() > 1) {
                        Begin.getInstance().show("Carro azul saindo da pista " + route.get(0).getName());
                        route.remove(0);
                        Begin.getInstance().show("Carro azul entrando na pista " + route.get(0).getName());
                    }
                }
                x = x + speed;
                car.setXY(x, y);
                break;
            case 6:
                if (!route.get(0).stillQuadrantY(y)) {
                    if (route.size() > 1) {
                        Begin.getInstance().show("carro azul saindo da pista " + route.get(0).getName());
                        route.remove(0);
                        Begin.getInstance().show("Carro azul entrando na pista " + route.get(0).getName());
                    }
                }
                y = y + speed;
                car.setXY(x, y);
                break;
            case 9:
                if (!route.get(0).stillQuadrantX(x)) {
                    if (route.size() > 1) {
                        Begin.getInstance().show("Carro azul saindo da pista " + route.get(0).getName());
                        route.remove(0);
                        Begin.getInstance().show("Carro azul entrando na pista " + route.get(0).getName());
                    }
                }
                x = x - speed;
                car.setXY(x, y);
                break;
        }
        this.sendMessage();
        //manda msg com os dados do seu carrro
    }

    /**
     * metodo que manda a msg para os outros carros, com o x, y, direção, se
     * está dentro do cruzamento e o trajeto
     */
    private void sendMessage() {

        ArrayList<Object> msg = new ArrayList<Object>();

        msg.add(x);
        msg.add(y);
        msg.add(direction);
        msg.add(atCrossroad);
        msg.add(route.size());
        //ele separa o trajeto, mandando quafrante por quadrante
        for (Quadrant quadrant : route) {
            msg.add(quadrant);
        }
        Controller.getInstance().replicateMsg(msg);
    }

    /**
     * muda a direção do carro
     */
    public void turnLeft() {
        direction = direction - 3;
        if (direction == -3) {
            direction = 9;
        }
        car.turn();
    }

    /**
     * muda a direção do carro
     */
    public void turnRight() {
        direction = direction + 3;
        if (direction == 12) {
            direction = 0;
        }
        car.turn();
    }

    public boolean getAtCrossroad() {
        return atCrossroad;
    }

    public void setAtCrossroad(boolean atCrossroad) {
        this.atCrossroad = atCrossroad;
    }

    /**
     * método responsavel por escolher a ação a ser feita pelo carro, andar,
     * ficar parado esperando
     */
    public void action() {

        //quando o carro estiver na via principal ele vai andar sem parar, ate chegar no beira da pista
        if (origin.equals("A") && this.y > 317) {
            move();
        } else if (origin.equals("C") && this.y < 139) {
            move();
        } else if (origin.equals("B") && this.x > 318) {
            move();
        } else if (origin.equals("D") && this.x < 143) {
            move();
        }

        //verifica se o carro esta na via principal ou cruzamento
        if (route.get(0).getName().equals("A") || route.get(0).getName().equals("B") || route.get(0).getName().equals("C") || route.get(0).getName().equals("D")) {
            atCrossroad = false;
        } else {
            atCrossroad = true;
        }

        //verifica se tem conflito, uma possivel batida ao entrar no cruzamento
        if (!logic.conflict()) {//caso não ocorra conflito, o carro anda o sem parar ate o fim do cruzamento
            if (origin.equals("A") && destiny.equals("D")) {
                if ((y <= 208) && (direction == 0)) {
                    turnLeft();
                } else {
                    move();
                }
            } else if (origin.equals("A") && destiny.equals("B")) {
                if ((y <= 257) && (direction == 0)) {
                    turnRight();
                } else {
                    move();
                }
            } else if (origin.equals("A") && destiny.equals("C")) {
                move();
            }

            if (origin.equals("C") && destiny.equals("D")) {
                if ((y >= 208) && (direction == 6)) {
                    turnRight();
                } else {
                    move();
                }
            } else if (origin.equals("C") && destiny.equals("B")) {
                if ((y >= 257) && (direction == 6)) {
                    turnLeft();
                } else {
                    move();
                }
            } else if (origin.equals("C") && destiny.equals("A")) {
                move();
            }

            if (origin.equals("B") && destiny.equals("C")) {
                if ((x <= 257) && (direction == 9)) {
                    turnRight();
                } else {
                    move();
                }
            } else if (origin.equals("B") && destiny.equals("A")) {
                if ((x <= 210) && (direction == 9)) {
                    turnLeft();
                } else {
                    move();
                }
            } else if (origin.equals("B") && destiny.equals("D")) {
                move();
            }

            if (origin.equals("D") && destiny.equals("C")) {
                if ((x >= 257) && (direction == 3)) {
                    turnLeft();
                } else {
                    move();
                }
            } else if (origin.equals("D") && destiny.equals("A")) {
                if ((x >= 210) && (direction == 3)) {
                    turnRight();
                } else {
                    move();
                }
            } else if (origin.equals("D") && destiny.equals("B")) {
                move();
            }
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setXY(float x, float y, int direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        car.setXY(x, y, direction);

    }

    public void setRoute(ArrayList<Quadrant> route) {
        this.route = route;
    }

    public int getDirection() {
        return this.direction;
    }
}
