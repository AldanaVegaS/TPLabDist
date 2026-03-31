package Avanzado;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.function.Function;

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

                String cacheKey = request.trim().toUpperCase();

                String responseCache = ServerCentral.cacheResultados.get(cacheKey);

                if (responseCache != null) {
                    //cache hit, se devuelve el resultado almacenado
                    System.out.println("HiloServer " + this.idSession + "> Resultado en caché para: " + request);
                    
                } else {
                    responseCache = ServerCentral.cacheResultados.computeIfAbsent(cacheKey, new Function<String, String>() {
            
                        public String apply(String key) {
                            
                            System.out.println("HiloServer " + idSession + "> Resultado no se encuentra en caché. Consultando servidores...");

                            String[] parts = request.split("\\|");

                            if (parts.length == 2) {
                                String signo = parts[0];
                                String fecha = parts[1];
                                
                                String horoscopo = ServerCentral.consultarServidorSecundario(Config.PORT_HOROSCOPO, signo);
                                String clima = ServerCentral.consultarServidorSecundario(Config.PORT_CLIMA, fecha);

                                return "Horóscopo para " + signo + ": " + horoscopo + " | Clima: " + clima;
                            } else {
                                return "Error: Formato de solicitud incorrecto.";
                            }
                        }
                    });

                    output.flush();
                    output.println(responseCache);

                    System.out.println("HiloServer " + this.idSession + "> Respuesta enviada al cliente: " + responseCache);
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
