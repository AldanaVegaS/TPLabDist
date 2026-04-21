package Medio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;

public class ServerHoroscopo {

    public static Set<String> signosValidos = Set.of(
        "ARIES", "TAURO", "GEMINIS", "CANCER",
        "LEO", "VIRGO", "LIBRA", "ESCORPIO",
        "SAGITARIO", "CAPRICORNIO", "ACUARIO", "PISCIS"
    );
    
    public static String[] predicciones = {
        "Hoy es un buen día para tomar decisiones importantes.",
        "En el trabajo, la paciencia será tu mejor aliada.",
        "La salud es tu prioridad, cuida de ti mismo.",
        "Un encuentro inesperado traerá alegría a tu vida."
    };

    public static void main(String[] args) {

        int idSession = 1;

        try{
            ServerSocket server = new ServerSocket(Config.PORT_HOROSCOPO);

            System.out.println("Servidor Horoscopo> Servidor iniciado.");

            while (true) {
                System.out.println("Servidor Horoscopo> En espera de cliente en el puerto " + Config.PORT_HOROSCOPO + ".");

                Socket clientSocket = server.accept();

                System.out.println("Servidor Horoscopo> Nuevo cliente conectado.");

                HiloServerHoroscopo thread = new HiloServerHoroscopo(clientSocket, idSession);
                thread.start();
                idSession++;
            }

        } catch (IOException ex) {
            System.err.println("Servidor Horoscopo> No se pudo iniciar el servidor: " + ex.getMessage());
            return;
        }
    }
}
