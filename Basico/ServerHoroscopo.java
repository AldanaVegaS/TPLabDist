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
    private final static int PORT = 5001;

    static Set<String> signosValidos = Set.of(
        "ARIES", "TAURO", "GEMINIS", "CANCER",
        "LEO", "VIRGO", "LIBRA", "ESCORPIO",
        "SAGITARIO", "CAPRICORNIO", "ACUARIO", "PISCIS"
);


    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(PORT);
        System.out.println("SH listo...");

        while (true) {

            Socket socket = server.accept();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String signo = in.readLine().toUpperCase();
            //System.out.println("SH> Petición recibida: " + signo);
            if (!signosValidos.contains(signo)) {
                out.println("Signo zodiacal no válido");
                socket.close();
                continue;
            }

            String[] predicciones = {
                    "Hoy es un buen día para tomar decisiones importantes.",
                    "En el trabajo, la paciencia será tu mejor aliada.",
                    "La salud es tu prioridad, cuida de ti mismo.",
                    "Un encuentro inesperado traerá alegría a tu vida."
            };

            String respuesta = predicciones[
                    new Random().nextInt(predicciones.length)
            ];

            out.println(respuesta);

            socket.close();
        }

    }
}
