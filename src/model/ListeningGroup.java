/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.Controller;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Classe que ouve tudo que foi dito no grupo multicast
 *
 * @author 1513 IRON
 */
public class ListeningGroup implements Runnable {

    private int port;
    private String ip;
    private Controller controller;

    public ListeningGroup(String ip, int port, Controller controller) {
        this.port = port;
        this.ip = ip;
        this.controller = controller;
    }

    @Override
    public void run() {
        while (true) {
            try {
                MulticastSocket mcs = new MulticastSocket(port);
                InetAddress grp = InetAddress.getByName(ip);//grupo multicast
                mcs.joinGroup(grp);
                byte rec[] = new byte[256];
                DatagramPacket pkg = new DatagramPacket(rec, rec.length);
                mcs.receive(pkg);
                String data = new String(pkg.getData());
                controller.firstConection(data);
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

}
