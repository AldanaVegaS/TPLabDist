package Basico;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class ServerClima {
    private final static int PORT = 5002;
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(PORT);
        System.out.println("SH listo...");

        while (true) {

            Socket socket = server.accept();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String fecha = in.readLine().toUpperCase();
            //System.out.println("SH> Petición recibida: " + fecha);
        

            String[] predicciones = {
                    "El clima de hoy es soleado con temperaturas agradables.",
                    "Se esperan lluvias ligeras durante la tarde.",
                    "El clima de hoy es nublado con posibilidad de tormentas.",
                    "Se pronostica un día caluroso con alta humedad."
            };

            String respuesta = predicciones[
                    new Random().nextInt(predicciones.length)
            ];

            out.println(respuesta);

            socket.close();
        }

    }
}
