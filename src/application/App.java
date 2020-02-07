/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import controller.Controller;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Scanner;
import model.ListeningGroup;
import util.Server;
import view.Begin;

/**
 *
 * @author Igor
 */
public class App {

    public static void main(String[] args) {
        String ip = null;

        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                // filters out 127.0.0.1 and inactive interfaces
                if (iface.isLoopback() || !iface.isUp()) {
                    continue;
                }

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();

                    // *EDIT*
                    if (addr instanceof Inet6Address) {
                        continue;
                    }

                    ip = addr.getHostAddress();
                    System.out.println(iface.getDisplayName() + " " + ip);
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

//        ip = get_ip().getHostAddress();
        System.out.println("Seu Ip é " + ip);
//        try {
//            ip = InetAddress.getLocalHost().getHostAddress();
//        } catch (UnknownHostException ex) {
//            System.out.println("Não foi possivel verificar ip");
//        }
        Controller controller = Controller.newController(ip);

        Begin initialScreen = Begin.newController(controller);

        Server serverSocket = new Server(8080);

        new Thread(serverSocket).start();

        ListeningGroup enr = new ListeningGroup("224.0.0.0", 12347, controller);
        new Thread(enr).start();

        String[] dados = new String[3];
        dados[0] = "224.0.0.0";
        dados[1] = "12347";
        dados[2] = ip;
        try {
            byte[] b = dados[2].getBytes();
            InetAddress addr = InetAddress.getByName(dados[0]);
            DatagramSocket ds = new DatagramSocket();
            DatagramPacket pkg = new DatagramPacket(b, b.length, addr, Integer.parseInt(dados[1]));
            ds.send(pkg);
        } catch (Exception e) {
            System.out.println("Nao foi possivel enviar a mensagem");
        }
        Scanner teclado = new Scanner(System.in);
        while (true) {
            controller.replicateMsg(teclado.nextLine());
        }
    }

//    private static InetAddress get_ip() {
//        NetworkInterface n = null;
//        try {
//            n = NetworkInterface.getByIndex(0);
//        } catch (SocketException ex) {
//            System.out.println("ERROR " + ex);
//        }
//        while (n != null) {
//            Enumeration e = n.getInetAddresses();
//            int pos = 0;
//            while (e.hasMoreElements()) {
//                InetAddress i = (InetAddress) e.nextElement();
//                System.out.println("ip" + i.getHostAddress());
//                pos++;
//            }
//        }
//        return null;
//    }
}
