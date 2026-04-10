package Avanzado;
import java.io.*;
import java.net.*;

public class Cliente {

    public static void main(String[] args) throws IOException {
        boolean exit = false;
        String signo, fecha, request;

        Socket socket; //socket para la comunicacion cliente servidor    
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in)); //para leer lo que escriba el usuario

        try {            
            System.out.println("Cliente> Inicio");  

            while(!exit){  
                
                System.out.print("Cliente> Ingrese su signo zodiacal (o 'exit' para salir): ");
                signo = keyboard.readLine();
                
                if(signo.equals("exit")) {
                    exit = true;
                    System.out.println("Cliente> Fin de programa");
                    continue; //salta al final del while
                }

                System.out.print("Cliente> Ingrese la fecha a consultar (dd/mm/aaaa): ");
                fecha = keyboard.readLine();

                if(!fecha.matches("\\d{2}/\\d{2}/\\d{4}")) {
                    System.out.println("Cliente> Error: Formato de fecha incorrecto. Use dd/mm/aaaa.\n");
                    continue; //vuelve a pedir todo desde el principio
                }

                request = signo + "|" + fecha;
                socket = new Socket(Config.SERVER, Config.PORT); //abre socket                
                
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream())); //para leer lo que envie el servidor                 
                PrintStream output = new PrintStream(socket.getOutputStream()); //para imprimir datos del servidor          
                         
                output.println(request); //enviar la petición empaquetada al servidor
                
                String st = input.readLine();
                if(st != null)
                    System.out.println("Servidor Central> " + st);

                socket.close();
            }

       } catch (IOException ex) {        
         System.err.println("Cliente> " + ex.getMessage());   
       }
    }
}
