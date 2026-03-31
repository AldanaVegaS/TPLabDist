package Avanzado;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

public class ServerCentral {
    
    public static ConcurrentHashMap<String, String> cacheResultados = new ConcurrentHashMap<>();
    public static Cache<String, String> cache = Caffeine.newBuilder()
                .maximumSize(5) // máximo 3 elementos
                .expireAfterWrite(2, TimeUnit.SECONDS) // expira en 2 segundos
                .build();
    public static void main(String[] args) throws IOException {

        int idSession = 1;
        ServerSocket serverSocket;

        try {
            serverSocket = new ServerSocket(Config.PORT); //socket de servidor para esperar peticiones de la red
            
            System.out.println("Servidor> Servidor iniciado."); //socket de servidor para esperar peticiones de la red

            while (true) {
                System.out.println("Servidor> En espera de cliente en el puerto " + Config.PORT + ".");   
                
                Socket clientSocket = serverSocket.accept(); //espera a que un cliente se conecte y crea un nuevo hilo para atenderlo

                System.out.println("Servidor> Nuevo cliente conectado.");

                HiloServer thread = new HiloServer(clientSocket, idSession); //espera a que un cliente se conecte y crea un nuevo hilo para atenderlo
                thread.start();
                idSession++;
            }

        } catch (IOException ex) {
            System.err.println("Servidor> No se pudo iniciar el servidor: " + ex.getMessage());
            return;
        }  
    }

    public static String consultarServidorSecundario(int puerto, String dato) {
        String resultado = "Servidor no disponible";
        
        try (
            Socket socket = new Socket("localhost", puerto);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            out.println(dato);
            
            String respuesta = in.readLine(); //espera la respuesta
            if (respuesta != null) {
                resultado = respuesta;
            }
        } catch (IOException e) {
            System.err.println("Servidor Central> Fallo al contactar servidor en puerto: " + Config.PORT + ": " + e.getMessage());
        }
        
        return resultado;
    }
}
