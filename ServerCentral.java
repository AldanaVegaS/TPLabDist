import java.io.*;
import java.net.*;

public class ServerCentral {
    private final static int PORT = 5000;
    public static void main(String[] args) throws IOException {
     try {
            //Socket de servidor para esperar peticiones de la red
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor> Servidor iniciado");    
            System.out.println("Servidor> En espera de cliente...");    
            //Socket de cliente
            Socket clientSocket;
            while(true){
                //en espera de conexion, si existe la acepta
                clientSocket = serverSocket.accept();
                System.out.println("Servidor> Cliente conectado"); 
                
                //Para leer lo que envie el cliente
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                
                //para imprimir datos de salida                
                PrintStream output = new PrintStream(clientSocket.getOutputStream());
                
                //se lee peticion del cliente
                String request = input.readLine();
                System.out.println("Cliente> petición [" + request +  "]");
                
                //se procesa la peticion y se espera resultado  
                /*CONSULTA HOROSCOPO */
                Socket sh = new Socket("localhost", 5001);

                PrintWriter outSH = new PrintWriter(sh.getOutputStream(), true);
                BufferedReader inSH = new BufferedReader(
                        new InputStreamReader(sh.getInputStream()));

                outSH.println(request);
                String horoscopo = inSH.readLine();

                sh.close();

                /*CONSULTA CLIMA */
                /*Socket sp = new Socket("localhost", 5002);

                PrintWriter outSP = new PrintWriter(sp.getOutputStream(), true);
                BufferedReader inSP = new BufferedReader(
                        new InputStreamReader(sp.getInputStream()));

                outSP.println(request);
                String clima = inSP.readLine();

                sp.close();*/
                

                //Se imprime en consola "servidor"
                System.out.println("Servidor> Resultado de petición");                    
                System.out.println("Servidor> \"" + horoscopo + "\"");
                
                //se imprime en cliente
                output.flush();//vacia contenido
                output.println(horoscopo);                
                
                //cierra conexion
                clientSocket.close();
            }    
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
