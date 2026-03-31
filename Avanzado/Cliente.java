package Avanzado;

import java.util.ArrayList;

public class Cliente {
    public static void main(String[] args) {

        ArrayList<Thread> clients = new ArrayList<Thread>(); //creación de clientes

        clients.add(new HiloCliente(1, "Aries", "10/05/2026"));
        clients.add(new HiloCliente(2, "Tauro", "15/08/2026"));
        clients.add(new HiloCliente(3, "Geminis", "20/11/2026"));
        clients.add(new HiloCliente(4, "Aries", "10/05/2026"));
        clients.add(new HiloCliente(5, "Tauro", "15/08/2026"));
        clients.add(new HiloCliente(6, "Capricornio", "25/12/2026"));

        for (Thread client : clients) {
            client.start();
        }
    }
}
