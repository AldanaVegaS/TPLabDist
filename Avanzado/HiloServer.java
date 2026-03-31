package Avanzado;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class HiloServer extends Thread {
    private Socket socket;
    private int idSession;

    public HiloServer(Socket socket, int id) {
        this.socket = socket;
        this.idSession = id;
    }

    @Override
    public void run() {
        String request, response, horoscopo, clima;

        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
            PrintStream output = new PrintStream(socket.getOutputStream());

            request = input.readLine();

            if (request != null) {

                //verificar si el resultado ya está en cache
                if (ServerCentral.cacheResultados.containsKey(request)) {
                    System.out.println("HiloServer " + this.idSession + "> Resultado en caché para: " + request);

                    String responseCache = ServerCentral.cacheResultados.get(request);

                    output.flush();
                    output.println(responseCache);

                } else {
                    //no está en caché, se consulta a los servidores secundarios y se guarda el resultado en cache.

                    System.out.println("HiloServer " + this.idSession + "> Resultado no se encuentra en caché para: " + request);

                    String[] parts = request.split("\\|");

                    if (parts.length == 2) {
                        String signo = parts[0];
                        String fecha = parts[1];
                        
                        horoscopo = ServerCentral.consultarServidorSecundario(Config.PORT_HOROSCOPO, signo);
                        clima = ServerCentral.consultarServidorSecundario(Config.PORT_CLIMA, fecha);

                        response = "Horóscopo para " + signo + ": " + horoscopo + " | Clima: " +  clima;

                        output.flush();
                        output.println(response);
                    } else {
                        output.println("HiloServer " + this.idSession + "> Error: Formato de solicitud incorrecto.");
                    }
                }

            }
            output.close();
            input.close();
            socket.close();

            System.out.println("HiloServer " + this.idSession + "> Cliente atendidido y desconectado.");

        } catch (IOException ex) {
            System.err.println("HiloServer " + this.idSession + "> Error: " + ex.getMessage());
        }
    }

}
