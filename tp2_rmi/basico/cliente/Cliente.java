package TPLabDist.tp2_rmi.basico.cliente;

import TPLabDist.tp2_rmi.Config;
import java.util.Scanner;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Cliente {
    public static void main(String[] args) {
        try {

            boolean exit = false;
            String signo, fecha;
            Scanner scanner = new Scanner(System.in);

            // conectarse al registro y obtener el stub del servicio remoto
            Registry registry = LocateRegistry.getRegistry(Config.SERVER, Config.PORT);
            InterfazCentral stubCentral = (InterfazCentral) registry.lookup("ServicioCentral");

            while(!exit){ 
                System.out.print("Cliente> Ingrese su signo zodiacal (o 'exit' para salir): ");
                signo = scanner.nextLine();
                    
                if(signo.equals("exit")) {
                    exit = true;
                    System.out.println("Cliente> Fin de programa");
                    continue; //salta al final del while
                }

                System.out.print("Cliente> Ingrese la fecha a consultar (dd/mm/aaaa): ");
                fecha = scanner.nextLine();

                if(!fecha.matches("\\d{2}/\\d{2}/\\d{4}")) {
                    System.out.println("Cliente> Error: Formato de fecha incorrecto. Use dd/mm/aaaa.\n");
                    continue; //vuelve a pedir todo desde el principio
                }

                // buscar el objeto remoto por el nombre que le dimos en el servidor
                String respuesta = stubCentral.consultarPredicciones(signo, fecha);
                System.out.println("Cliente> Respuesta recibida: \n" + respuesta);
            }

            scanner.close();
        } catch (Exception e) {
            System.err.println("Error en el cliente: " + e.toString());
            e.printStackTrace();
        }
    }
}