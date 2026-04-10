package Avanzado;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ServerClima {

    public static String[] predicciones = {
        "El clima de hoy es soleado con temperaturas agradables.",
        "Se esperan lluvias ligeras durante la tarde.",
        "El clima de hoy es nublado con posibilidad de tormentas.",
        "Se pronostica un día caluroso con alta humedad."
    };

    public static void main(String[] args) {

        int idSession = 1;
        ServerSocket server;
        Socket socket; //socket para la comunicacion cliente servidor

        try{
            server = new ServerSocket(Config.PORT_CLIMA);

            System.out.println("Servidor Clima> Iniciado correctamente.");

            while (true) {
                System.out.println("Servidor Clima> Escuchando peticiones en el puerto " + Config.PORT_CLIMA + "...\n");
                socket = server.accept();
                System.out.println("Servidor Clima> Conexión entrante aceptada.");

                HiloServerClima thread = new HiloServerClima(socket, idSession);
                thread.start();

                idSession++;
            }

        } catch (IOException ex) {
            System.err.println("Servidor Clima> Error al iniciar el servidor: " + ex.getMessage());
            return;
        }
    }

    public static int obtenerIndice(String fechaStr, int tamaño) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fecha = LocalDate.parse(fechaStr, formatter);

        long numero = fecha.toEpochDay();

        return (int) (Math.abs(numero) % tamaño);
    }

    public static boolean esFechaValida(String fechaStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate.parse(fechaStr, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
