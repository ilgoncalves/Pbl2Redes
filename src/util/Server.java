/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import controller.Controller;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * classe com o serversocket
 *
 * @author Igor
 */
public class Server implements Runnable {

    private int port;
    private Controller controller;
    private ArrayList<Socket> clients;

    public Server(int port) {
        this.controller = Controller.getInstance();
        this.port = port;
        clients = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            ServerSocket servidor = new ServerSocket(this.port);
            while (true) {
                System.out.println("entrou aqui ");
                Socket client = servidor.accept();
                String ip = client.getInetAddress().getHostAddress();
                System.out.println(ip + " se conectou comigo");
                ProcessClient tc = new ProcessClient(client);
                new Thread(tc).start();
                controller.startConection(ip);
            }
        } catch (IOException ex) {

        }
    }
}
