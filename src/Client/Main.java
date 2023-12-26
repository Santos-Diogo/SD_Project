package Client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import Shared.Defines;

public class Main 
{
    static Scanner scanner= new Scanner (System.in);
    static Socket socket;
    static boolean authenticated = false;
    static Handler handler;

    private static void registerClient()
    {
        System.out.print("\n\nUsername: ");
        String username = scanner.nextLine();
        System.out.print("\nPassword: ");
        String password = scanner.nextLine();
        authenticated = handler.handleRegister(username, password);
    }

    private static void loginClient()
    {
        System.out.print("\nUsername: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        authenticated = handler.handleLogin(username, password);
    }

    private static String login_register_request ()
    {
        System.out.println("\nRegister an account or log-in with your own:");
        System.out.println("Commands: register, login");
        System.out.print("Command: ");
        return scanner.nextLine();
    }

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
            handler= new Handler(state, socket);
            String command;

            while (!authenticated)
            {
                command = login_register_request();
                if (command.equals("register"))
                    registerClient();
                else if (command.equals("login"))
                    loginClient();
            }
    
            // Handle commands
            while (!(command = command_request()).equals("quit")) 
            {
                handler.handle (command);
            } 
        } catch (IOException e) {
            System.err.println("Erro a estabelecer conexão");
        }
    }
}
