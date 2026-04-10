package Avanzado;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class HiloServerClima extends Thread {
    
    private Socket socket;
    private int idSession;
    private String[] predicciones = ServerClima.predicciones;

    public HiloServerClima(Socket socket, int idSession) {
        this.socket = socket;
        this.idSession = idSession;
    }

    @Override
    public void run() {
        try{
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            String fecha = input.readLine().toUpperCase();

            if (!ServerClima.esFechaValida(fecha)) {
                output.println("Fecha no válida.");
                System.out.println("Servidor Clima " + this.idSession + "> Pronóstico enviado: Fecha no válida.");
                socket.close();
                return;
            }

            String respuesta = this.predicciones[ServerClima.obtenerIndice(fecha, this.predicciones.length)];

            output.flush();
            output.println(respuesta);
            
            System.out.println("Servidor Clima " + this.idSession + "> Pronóstico enviado: \"" + respuesta + "\"");
        
            output.close();
            input.close();

        } catch (IOException ex) {
            System.err.println("Servidor Clima " + this.idSession + "> Error en la comunicación: " + ex.getMessage());
        }
    }
    
}
