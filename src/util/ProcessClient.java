/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import controller.CarController;
import controller.Controller;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import view.Begin;

/**
 * Classe responsavel para se comunicar com os outros carros através de conexão
 * tcp
 *
 * @author Igor
 */
public class ProcessClient implements Runnable {

    private static int id_counter = 1; //contador que indica o ID de cada carro, cada carro tem um ID diferente que é utilizado para identidicar o carro
    private Controller controller;
    private Socket client;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Server server; //servidor de cada usuario que recebe conexões de outros usuarios
    private String ip; //ip do usuario conectado
    private int id; //ID do respectivo carro
    private int modifier = 0; // variavel utilizada saber se é a primeira conexão de um usuario, para adiciona-lo na tela
    private String color;
    private Quadrant currentQuadrant; //Quarda o quadrante do usuario

    ProcessClient(Socket client) {
        this.id = id_counter;
        this.id_counter++;
        this.controller = Controller.getInstance();//pega instancia do controler atual
        this.client = client;
        this.ip = this.client.getInetAddress().getHostAddress();
        this.server = server;
        this.currentQuadrant = new Quadrant("");
        try {
            output = new ObjectOutputStream(client.getOutputStream());
            input = new ObjectInputStream(client.getInputStream());
        } catch (IOException ex) {
        }
    }

    /**
     * envia a lista de todos os ips de computadores que estão conectados com
     * meu serversocket
     */
    private void sendIps() {
        try {
            String msg = (String) input.readObject();
            if (msg.equals("primeiro")) {
                ArrayList<String> aux = new ArrayList<>();
                aux.addAll(controller.getIps());
                aux.remove(this.ip);
                output.writeObject(aux);
            }
        } catch (IOException ex) {
            //caso ocorra algum erro de comunição, o carro é retirado da tela
            Begin.getInstance().show("Carro " + color + " se desconectou");
            Controller.getInstance().removeCar(id);
            return;
        } catch (ClassNotFoundException ex) {
            return;
        }

    }

    @Override
    public void run() {

        sendIps();//envia a lista de ip que esta conectado com ele
        while (true) {
            try {
                ArrayList<Quadrant> route = new ArrayList<>();
                //sempre o usuario vai receber os dados do carro:posição do x e y, a direção e se o carro está parado ou andando, para que o usuario saiba e possa representar isso na tela
                //todas essas informações são recebidas através de um array
                ArrayList<Object> message = (ArrayList<Object>) input.readObject();
                float x = (float) message.get(0);
                float y = (float) message.get(1);
                int direction = (int) message.get(2);
                boolean stoped = (boolean) message.get(3);
                //o trajeto é enviado quadrante por quadrante, e assim a junção é feita nessa instrução abaixo
                int routeSize = (int) message.get(4);
                for (int j = 5; j < routeSize + 5; j++) {
                    Quadrant quadrant = (Quadrant) message.get(j);
                    route.add(quadrant);
                }

                if (modifier == 0) {//coloca carro na tela
                    controller.addCar(id, x, y, direction, route);
                    if (id == 1) {
                        color = "amarelo";
                    } else if (id == 2) {
                        color = "verde";
                    } else if (id == 3) {
                        color = "preto";
                    } else {
                        color = "vermelho";
                    }

                    Begin.getInstance().show("iniciando carro " + color + " na pista " + route.get(0).getName());
                    currentQuadrant = route.get(0);
                    modifier = 1;
                } else {//atualiza os dados do carro
                    CarController currentCar = controller.getCar(this.id);
                    currentCar.setXY(x, y, direction);
                    currentCar.setRoute(route);

                    if (!currentQuadrant.getName().equals(route.get(0).getName())) {//verifica se o carro ainda está no quadrante para pode exibir a msg
                        Begin.getInstance().show("Carro " + color + " saindo da pista " + currentQuadrant.getName());
                        Begin.getInstance().show("Carro " + color + " entrando  na pista " + route.get(0).getName());
                        currentQuadrant = route.get(0);
                    }
                    currentCar.setAtCrossroad(stoped);

                }

            } catch (IOException ex) {
                //caso ocorra algum erro de comunição, o carro é retirado da tela
                Begin.getInstance().show("Carro " + color + " se desconectou");
                Controller.getInstance().removeCar(id);
                return;
            } catch (ClassNotFoundException ex) {
                return;
            }
        }
    }
}
