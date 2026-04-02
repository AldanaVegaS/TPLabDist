package Avanzado;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.Set;

public class HiloServerHoroscopo extends Thread {
    
    private int idSession;
    private Socket clientSocket;
    private Set<String> signosValidos;
    private String[] predicciones;

    public HiloServerHoroscopo(Socket clientSocket, int idSession) {
        this.clientSocket = clientSocket;
        this.idSession = idSession;
        this.signosValidos = ServerHoroscopo.signosValidos;
        this.predicciones = ServerHoroscopo.predicciones;
    }
    
    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String signo = in.readLine().toUpperCase();
            
            if (!signosValidos.contains(signo)) {
                out.println("Signo zodiacal no válido");
                System.out.println("Servidor Horóscopo " + this.idSession + "> Signo no válido");
                clientSocket.close();
                return;
            } 
            
            String respuesta = predicciones[new Random().nextInt(predicciones.length)];

            out.flush();
            out.println(respuesta);

            System.out.println("Servidor Horóscopo " + this.idSession + "> Horóscopo enviado: \"" + respuesta + "\"");

            out.close();
            in.close();

            clientSocket.close();
        } catch (IOException ex) {
            System.err.println("Servidor Horoscopo " + this.idSession + "> Error en la comunicación: " + ex.getMessage());
        }
    }

}
