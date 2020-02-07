/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.Controller;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import util.Client;

/**
 * classe que auxilia o controller
 *
 * @author Igor
 */
public class Auxiliary {

    private ArrayList<Client> clients;//lista de todos os carros conectados
    private Controller controller;

    public Auxiliary(Controller controller) {
        this.clients = new ArrayList<Client>();
        this.controller = controller;
    }

    /**
     * conecta com o carro do usuario com o com o serversocket
     *
     * @param ip ip do server socket
     */
    public void startConection(String ip) {
        Client client = new Client(8080, ip);
        System.out.println("Me conectei com " + ip);
        new Thread(client).start();
        client.sendMsg("segundo");
        clients.add(client);
    }

    /**
     * manda msg para todos os carros conectados
     *
     * @param msg string
     */
    public void replicateMsg(String msg) {
        for (Client c : clients) {
            c.sendMsg(msg);
        }
    }

    /**
     * manda msg para todos os carros conectados
     *
     * @param msg objeto
     */
    public void replicateMsg(ArrayList<Object> msg) {
        for (Client c : clients) {
            c.sendMsg(msg);
        }
    }

    /**
     * Remove socket do carro apartir do ip
     *
     * @param ip
     */
    public void removeClient(String ip) {
        try {
            for (Client c : clients) {
                if (c.getIp().equals(ip)) {
                    clients.remove(c);
                }
            }
        } catch (ConcurrentModificationException e) {
            this.removeClient(ip);
        }
    }

    /**
     * metodo que pede uma lista de todos os ip que estão conectados na rede
     *
     * @param ip
     */
    public void firstConection(String ip) {
        Client client = new Client(8080, ip);
        System.out.println("Me conectei com " + ip);
        new Thread(client).start();
        client.sendMsg("primeiro");
        try {
            ArrayList<String> ips = (ArrayList<String>) client.getInput().readObject();
            for (String ipAtual : ips) {
                System.out.println(ipAtual);
                controller.startConection(ipAtual);
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Conexão perdida com o usuario");
        }
        clients.add(client);//adiciona o usuario que passou a lista na lista de usuarios conectados
    }

}
