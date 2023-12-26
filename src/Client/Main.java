package Client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import Shared.Defines;

public class Main 
{
    static Scanner scanner= new Scanner (System.in);
    static Socket socket;

    /** 
    * @return String
    */
    private static String command_request() 
    {
        System.out.println("\nType your desired command:");
        return scanner.nextLine();
    }
    
    public static void main (String[] args)
    {
        String addr = "localhost";
        int port = Defines.serverport;
        if (args.length == 2) {addr = args[0]; port = Integer.parseInt(args[1]);}
        try {
            socket = new Socket(addr, port);
            State state= new State();
            Handler handler= new Handler(state, socket);
    
            // Handle commands
            String command;
            while (!(command = command_request()).equals("quit")) 
            {
                handler.handle (command);
            } 
        } catch (IOException e) {
            System.err.println("Erro a estabelecer conexão");
        }
    }
}
