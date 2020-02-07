/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.Thread.sleep;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Igor
 */
public class Client implements Runnable {

    private int port;//minha porta
    private String ip;//meu ip
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket client;
    private boolean sendMsg;//boolean para controlar quando chega uma msg nova
    private Object msg;//msg a ser enviada

    public Client(int port, String ip) {
        this.port = port;
        this.ip = ip;
        try {
            client = new Socket(ip, port);
            output = new ObjectOutputStream(client.getOutputStream());
            input = new ObjectInputStream(client.getInputStream());
        } catch (IOException ex) {//Caso ocorra um erro na comunicação
            controller.Controller.getInstance().removeIp(ip);
            try {
                client.close();
            } catch (IOException ex1) {
            }
        }
    }

    public String getIp() {
        return ip;
    }

    @Override
    public void run() {
        while (true) {
            try {//sleep para dar o tempo de alterar o valor da variavel
                sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (sendMsg) {//verifica se tem msg nova
                try {
                    output.writeObject(msg);//envia a msg para o serversocket
                    sendMsg = false;
                } catch (IOException ex) {
                    controller.Controller.getInstance().removeIp(ip);
                    try {
                        client.close();
                        return;
                    } catch (IOException ex1) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                }
            }
        }
    }

    public ObjectInputStream getInput() {
        return input;
    }

    /**
     * Método que altera o valor da variavel e altera o valor da variavel msg,
     * para a msg atual
     *
     * @param msg
     */
    public void sendMsg(Object msg) {
        this.msg = msg;
        this.sendMsg = true;
    }

}
