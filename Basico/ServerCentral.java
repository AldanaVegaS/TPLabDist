package Basico;
import java.io.*;
import java.net.*;

public class ServerCentral {
    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket;

        try {
            serverSocket = new ServerSocket(Config.PORT); //socket de servidor para esperar peticiones de la red
            
            System.out.println("Servidor> Servidor iniciado"); //socket de servidor para esperar peticiones de la red
            System.out.println("Servidor> En espera de cliente en el puerto " + Config.PORT + ".");    
        } catch (IOException ex) {
            System.err.println("Servidor> No se pudo iniciar el servidor: " + ex.getMessage());
            return;
        }

        String signo, fecha;
        Socket clientSocket; //Socket de cliente
        while(true){
            
            try{
                clientSocket = serverSocket.accept();
                
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); //para leer lo que envie el cliente
                
                PrintStream output = new PrintStream(clientSocket.getOutputStream()); //para imprimir datos de salida
                
                System.out.println("Servidor> Cliente conectado."); //en espera de conexion, si existe la acepta

                String request = input.readLine(); //se lee peticion del cliente

                if (request != null) {
                    System.out.println("Servidor Central> Petición recibida: [" + request + "]");

                    String[] partes = request.split("\\|");
                        
                        //validacion de datos recibidos, se espera que el cliente mande algo con el formato "signo|fecha"
                        if (partes.length == 2) {
                            signo = partes[0];
                            fecha = partes[1];

                            String horoscopo = consultarServidorSecundario(Config.PORT_HOROSCOPO, signo);
                            String clima = consultarServidorSecundario(Config.PORT_CLIMA, fecha);
                            
                            String respuestaFinal = "Horóscopo: " + horoscopo + " | Clima: " + clima;
                            System.out.println("Servidor Central> Resultado armado: \"" + respuestaFinal + "\"");
                            
                            output.flush(); 
                            output.println(respuestaFinal);  

                        } else {
                            output.println("Error: Formato de petición incorrecto.");
                        }
                    }

                    output.close();
                    input.close();
                    clientSocket.close();
                    System.out.println("Servidor Central> Conexión con cliente cerrada correctamente.");

                //se procesa la peticion y se espera resultado  

                // /*CONSULTA HOROSCOPO */
                // Socket sh = new Socket("localhost", 5001);

                // PrintWriter outSH = new PrintWriter(sh.getOutputStream(), true);
                // BufferedReader inSH = new BufferedReader(
                //         new InputStreamReader(sh.getInputStream()));

                // outSH.println(request);
                // String horoscopo = inSH.readLine();

                // sh.close();

                /*CONSULTA CLIMA */
                /*Socket sp = new Socket("localhost", 5002);

                PrintWriter outSP = new PrintWriter(sp.getOutputStream(), true);
                BufferedReader inSP = new BufferedReader(
                        new InputStreamReader(sp.getInputStream()));

                outSP.println(request);
                String clima = inSP.readLine();

                sp.close();*/
                

                // //Se imprime en consola "servidor"
                // System.out.println("Servidor> Resultado de petición");                    
                // System.out.println("Servidor> \"" + horoscopo + "\"");
                
                // //se imprime en cliente
                // output.flush();//vacia contenido
                // output.println(horoscopo);                
                
                // //cierra conexion
                // clientSocket.close();
            } catch (IOException ex) {
                System.err.println("Servidor> Error al procesar la petición: " + ex.getMessage());
            }
        }    
    }

    private static String consultarServidorSecundario(int puerto, String dato) {
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
            System.err.println("Servidor Central> Fallo al contactar servidor en puerto: " + puerto);
        }
        
        return resultado;
    }


    //private static void procesarPeticion(String request) {
        /*CONSULTA HOROSCOPO */
        /*Socket sh = new Socket("localhost", 5001);

        PrintWriter outSH = new PrintWriter(sh.getOutputStream(), true);
        BufferedReader inSH = new BufferedReader(new InputStreamReader(sh.getInputStream()));

        outSH.println(request);
        String horoscopo = inSH.readLine();

        sh.close();*/



        /*CONSULTA CLIMA */
        /*Socket sp = new Socket("localhost", 5002);

        PrintWriter outSP = new PrintWriter(sp.getOutputStream(), true);
        BufferedReader inSP = new BufferedReader(new InputStreamReader(sp.getInputStream()));

        outSP.println(request);
        String clima = inSP.readLine();

        sp.close();*/
    //}
}
