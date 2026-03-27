import java.io.*;
import java.net.*;

public class Cliente {

    private final static int PORT = 5000;

    private final static String SERVER = "localhost";
    public static void main(String[] args) throws IOException {
        boolean exit=false;//bandera para controlar ciclo del programa
        Socket socket;//Socket para la comunicacion cliente servidor   
            
        try {            
            System.out.println("Cliente> Inicio");  
            while( !exit ){//ciclo repetitivo                                
                socket = new Socket(SERVER, PORT);//abre socket                
                
                //Para leer lo que envie el servidor      
                BufferedReader input = new BufferedReader( new InputStreamReader(socket.getInputStream()));                
                
                //para imprimir datos del servidor
                PrintStream output = new PrintStream(socket.getOutputStream());                
                
                //Para leer lo que escriba el usuario            
                BufferedReader brRequest = new BufferedReader(new InputStreamReader(System.in));            
                System.out.println("Cliente> Escriba comando");                
                
                //captura comando escrito por el usuario
                String request = brRequest.readLine();                
                
                //manda peticion al servidor
                output.println(request); 
                
                //captura respuesta e imprime
                String st = input.readLine();
                if( st != null ) System.out.println("Servidor> " + st );    
                if(request.equals("exit")){//terminar aplicacion
                    exit=true;                  
                    System.out.println("Cliente> Fin de programa");    
                }  
                socket.close();
            }                                    
       } catch (IOException ex) {        
         System.err.println("Cliente> " + ex.getMessage());   
       }
    }
}
