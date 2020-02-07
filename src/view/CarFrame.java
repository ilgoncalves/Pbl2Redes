/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.CarController;
import controller.Controller;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import util.MainLoop;

/**
 *
 * @author 1513 IRON
 */
public class CarFrame extends JFrame {

    private MainLoop loop = new MainLoop(this, 60); //variável que guarda o loop da movimentação.
    private ArrayList<CarController> cars; //array contendo os carros presentes na via.
    private Controller controller;
    private Image background; //imagem de fundo que representa as vias e o cruzamento.

    /**
     * Contrutor que define características à tela. Aqui são definidos as
     * dimensoes da tela e desabilita a possibilidade de mudar o tamanho da
     * tela.
     *
     * @see JFrame
     */
    public CarFrame() {
        super("Cruzamento");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(482, 480);
        setResizable(false);
        this.controller = Controller.getInstance();
        this.cars = this.controller.getCars();
    }

    /**
     * Método que inicia o loop da aplicação responsável pela movimentação dos
     * veículos.
     *
     * @see MainLoop
     */
    public void startMainLoop() {
        //Iniciamos o main loop
        new Thread(loop, "Main loop").start();
    }

    /**
     * Método que carrega a imagem do plano de fundo que representa o ambiente
     * que os veículos se movem.
     *
     * @see Image
     */
    public void setup() {
        try {
            this.background = ImageIO.read(new File("background.png"));
        } catch (IOException ex) {
            Logger.getLogger(CarFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        repaint();
    }

    /**
     * Método que aciona a operação no veículo principal da aplicação (carro
     * deste usuário).
     *
     * @see ControllerCarro
     */
    public void processLogics() {
        this.cars = this.controller.getCars();
        if (controller.getCar(0) != null) {
            controller.getCar(0).action();
        }
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(background, 0, 0, null);

        for (CarController car : cars) {
            if (car != null) {
                car.draw((Graphics2D) g);
            }
        }
    }

    /**
     * Método que repinta o frame chamando a função paint.
     */
    public void paintScreen() {
        repaint();
    }

}
