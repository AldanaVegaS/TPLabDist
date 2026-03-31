package Medio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class HiloCliente extends Thread {
    private int idSession;
    private String signo;
    private String fecha;

    public HiloCliente(int id, String signo, String fecha) {
        this.idSession = id;
        this.signo = signo;
        this.fecha = fecha;
    }

    @Override
    public void run() {

        String request;

        Socket socket; //socket para la comunicacion cliente servidor    
       
        try {            
                
            System.out.println("Cliente " + idSession + "> Iniciando peticion automática: " + signo + " - " + fecha);  
                
            request = signo + "|" + fecha;
            socket = new Socket(Config.SERVER, Config.PORT); 
            
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));                
            PrintStream output = new PrintStream(socket.getOutputStream());          
                    
            output.println(request); 
            
            String st = input.readLine();
            if(st != null) {
                System.out.println("Cliente " + idSession + " recibe del Servidor Central> " + st);
            }
            
            output.close();
            input.close();
            socket.close();

       } catch (IOException ex) {        
         System.err.println("Cliente " + idSession + "> " + ex.getMessage());   
       }
    }
}
