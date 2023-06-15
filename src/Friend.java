import java.util.List;
import java.util.concurrent.*;

import java.util.Random;
public class Friend implements Runnable{
    private int id;
    private Semaphore cebadorSemaphore;
    private Semaphore mateSemaphore;
    private List<String> facturas;
    private int tomadas;
    public Friend(int id, Semaphore cebadorSemaphore, Semaphore mateSemaphore, List<String> facturas) {
        this.id = id;
        this.cebadorSemaphore = cebadorSemaphore;
        this.mateSemaphore = mateSemaphore;
        this.facturas = facturas;
        this.tomadas = 0;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (tomadas > 2){
                    System.out.println("Amigo " + id + " ya tomó 3 mates, no quiere tomar más.");
                    break;
                }
                Random random = new Random();
                boolean comiendoFactura = false;
                if (cebadorSemaphore.availablePermits() > 0) {
                    // El cebador cebará mate al resto
                    cebadorSemaphore.acquire();
                    System.out.println("Amigo " + id + " es el cebador y va a cebar mate al resto.");
                }
                // Verificar si estoy comiendo una factura
                if (!facturas.isEmpty()){
                    synchronized (facturas) {
                        if (random.nextDouble() > 0.65) {
                            comiendoFactura = true;
                            String factura = facturas.remove(0);
                            System.out.println("Amigo " + id + " está comiendo la factura: " + factura + ", va a saltear el mate");
                            if (facturas.isEmpty()){
                                System.out.println("Amigo " + id + " dice: no hay mas facturas, me comí la última");
                            }
                        }
                        else {
                            System.out.println("Amigo " + id + " dice: no quiero facturas, quiero un mate.");
                        }
                    }
                }
                if (!comiendoFactura) {
                    tomarMate();
                }
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void tomarMate() throws InterruptedException {
        System.out.println("Amigo " + id + " está esperando para tomar mate. ");
        mateSemaphore.acquire();
        System.out.println("Amigo " + id + " está tomando mate.");
        // Simular el tiempo de tomar mate
        Thread.sleep(6000);
        System.out.println("Amigo " + id + " terminó de tomar mate. Ya lleva " + (tomadas+1) + " mates.");
        // Liberar el permiso del mate
        pasarMate();
        mateSemaphore.release();
        tomadas++;
        Thread.sleep(3000);
    }
    private void pasarMate() throws InterruptedException {
        System.out.println("Amigo " + id + " pasa el mate");
    }

    public int getId() {
        return id;
    }
}