package Basico;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Set;

public class ServerHoroscopo {

    static Set<String> signosValidos = Set.of(
        "ARIES", "TAURO", "GEMINIS", "CANCER",
        "LEO", "VIRGO", "LIBRA", "ESCORPIO",
        "SAGITARIO", "CAPRICORNIO", "ACUARIO", "PISCIS"
    );

    static String[] predicciones = {
                "Hoy es un buen día para tomar decisiones importantes.",
                "En el trabajo, la paciencia será tu mejor aliada.",
                "La salud es tu prioridad, cuida de ti mismo.",
                "Un encuentro inesperado traerá alegría a tu vida."
            }; 

    public static void main(String[] args) throws IOException {
        ServerSocket server;

        try{
            server = new ServerSocket(Config.PORT_HOROSCOPO);

            System.out.println("Servidor Horoscopo> Iniciado correctamente.");
            System.out.println("Servidor Horoscopo> Escuchando peticiones en el puerto " + Config.PORT_HOROSCOPO + "...\n");
        } catch (IOException ex) {
            System.err.println("Servidor Horoscopo> Error al iniciar el servidor: " + ex.getMessage());
            return;
        }

        Socket socket; //socket para la comunicacion cliente servidor

        while (true) {

            try{
                socket = server.accept();
                System.out.println("Servidor Horoscopo> Conexión entrante aceptada.");

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                String signo = in.readLine().toUpperCase();
                
                if (!signosValidos.contains(signo)) {
                    out.println("Signo zodiacal no válido");
                    System.out.println("Servidor Horoscopo> Horóscopo enviado: Signo zodiacal no válido.");
                    socket.close();
                    continue;
                }

                String respuesta = predicciones[
                        new Random().nextInt(predicciones.length)
                ];

                out.flush();
                out.println(respuesta);

                System.out.println("Servidor Horoscopo> Horóscopo enviado: \"" + respuesta + "\"");

                out.close();
                in.close();
                socket.close();
            } catch (IOException ex) {
                System.err.println("Servidor Horoscopo> Error en la comunicación: " + ex.getMessage());
            }
            
        }

    }
}
