package util;

import java.util.logging.Level;
import java.util.logging.Logger;
import view.CarFrame;

/**
 * Método que executa numa thread o loop responsável por chamar a movimentaçãoe
 * e parte lógica da aplicação. Ela executa o frame a cada intervalo de tempo
 * determinado no contrutor.
 *
 * @author Igor
 */
public class MainLoop implements Runnable {

    private CarFrame game;
    private long desiredUpdateTime;
    private boolean running;

    private long afterTime;
    private long beforeTime = System.currentTimeMillis();

    private long overSleepTime = 0;

    /**
     * Construtor que recebe o frame e o numero de atualizacoes por segundo que
     * o usuario deseja.
     *
     * @param carroFrame frame que o loop controla.
     * @param ups número desejado de updates por segundo.
     *
     */
    public MainLoop(CarFrame carroFrame, int ups) {
        super();

        this.game = carroFrame;
        this.desiredUpdateTime = 1000000000L / ups;

    }

    /**
     * Método que dorme a thread por determinado tempo em nanosegundos.
     * Necessário para igualizar o tempo do loop de todos os usuários, levando
     * em conta o tempo de processamento.
     *
     * @param nanos
     * @throws InterruptedException
     */
    private void sleep(long nanos) throws InterruptedException {
        long beforeSleep = System.nanoTime();
        Thread.sleep(nanos / 1000000L, (int) (nanos % 1000000L));
        overSleepTime = System.nanoTime() - beforeSleep - nanos;
    }

    /**
     * Método que calcula o tempo gasto para processamento e subtrai do tempo
     * total definido para o loop. Padronizando o tempo de cada volta.
     */
    private long calculateSleepTime() {
        return desiredUpdateTime - (afterTime - beforeTime);
    }

    /**
     * Executa o a classe main loop, gravando o tempo antes e depois do
     * processamento. Cada volta do loop gasta aproximadamente o mesmo tempo.
     */
    @Override
    public void run() {
        game.setup();
        while (true) {
            beforeTime = System.nanoTime();//grava o tempo antes de processar logicas

            //Executa a logica e desenha os carros no carroFrame
            game.processLogics();
            game.paintScreen();

            afterTime = System.nanoTime();//guarda o tempo após o processamento.

            long sleepTime = calculateSleepTime();

            if (sleepTime >= 0) {
                try {
                    sleep(sleepTime);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MainLoop.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
