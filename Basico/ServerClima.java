package Basico;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ServerClima {

    public static void main(String[] args) throws IOException {

        String[] predicciones = {
                    "El clima de hoy es soleado con temperaturas agradables.",
                    "Se esperan lluvias ligeras durante la tarde.",
                    "El clima de hoy es nublado con posibilidad de tormentas.",
                    "Se pronostica un día caluroso con alta humedad."
            };

        ServerSocket server;

        try{
            server = new ServerSocket(Config.PORT_CLIMA);

            System.out.println("Servidor Clima> Iniciado correctamente.");
            System.out.println("Servidor Clima> Escuchando peticiones en el puerto " + Config.PORT_CLIMA + "...\n");
        } catch (IOException ex) {
            System.err.println("Servidor Clima> Error al iniciar el servidor: " + ex.getMessage());
            return;
        }

        Socket socket; //socket para la comunicacion cliente servidor

        while (true) {

            try{
                socket = server.accept();
                System.out.println("Servidor Clima> Conexión entrante aceptada.");

                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

                String fecha = input.readLine().toUpperCase();

                if (!esFechaValida(fecha)) {
                    output.println("Fecha no válida.");
                    System.out.println("Servidor Clima> Pronóstico enviado: Fecha no válida.");
                    socket.close();
                    continue;
                }


                String respuesta = predicciones[obtenerIndice(fecha, predicciones.length)];

                output.flush();
                output.println(respuesta);
                
                System.out.println("Servidor Clima> Pronóstico enviado: \"" + respuesta + "\"");
            
                output.close();
                input.close();
                socket.close();

            } catch (IOException ex) {
                System.err.println("Servidor Clima> Error en la comunicación: " + ex.getMessage());
            }
        }
    }

    public static int obtenerIndice(String fechaStr, int tamanio) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fecha = LocalDate.parse(fechaStr, formatter);

        long numero = fecha.toEpochDay();

        return (int) (Math.abs(numero) % tamanio);
    }

    private static boolean esFechaValida(String fechaStr) {
    try {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate.parse(fechaStr, formatter);
        return true;
    } catch (DateTimeParseException e) {
        return false;
    }
}
}
