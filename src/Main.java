//La idea de este código es implementar conceptos de multithreading en una ronda de mates: hay un cebador y todos los amigos quieren tomar 3 mates. Cada
// uno de los amigos tiene su thread. Usa semáforos y sincronización para lograrlo. También usan una bolsa de facturas que comparten entre todos los hilos.

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {
        final int numFriends = 5;
        Semaphore cebadorSemaphore = new Semaphore(1); // Semáforo para controlar el acceso al cebador
        Semaphore mateSemaphore = new Semaphore(1); // Semáforo para controlar la espera de los amigos por el mate

        List<String> facturas = new ArrayList<>();
        facturas.add("Medialuna dulce");
        facturas.add("Torta Negra");
        facturas.add("De Pastelera");
        facturas.add("Cañoncito DDL");
        facturas.add("Medialuna Salada");
        facturas.add("Bola de Fraile");
        Collections.shuffle(facturas);
        Friend[] friends = new Friend[numFriends];
        Thread th[] = new Thread[numFriends];
        // Crear y empezar los hilos de los amigos
        for (int i = 0; i < numFriends; i++) {
            friends[i] = new Friend(i, cebadorSemaphore, mateSemaphore,facturas);
            th[i] = new Thread(friends[i]);
            th[i].start();
            System.out.println("Amigo " + i + " creado");
        }

        // Esperar a que todos los hilos de los amigos terminen
        try {
            for (Friend friend : friends) {
                th[friend.getId()].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("La ronda de mate terminó.");
    }
}