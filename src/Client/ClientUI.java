package Client;

import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

import Shared.Defines;

public class ClientUI {

    private static Scanner scanner = new Scanner(System.in);
    private static boolean authenticated = false;
    private static IClient client;
    private static String user;


    private static boolean registerClient()
    {
        System.out.print("\nUsername: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        Optional<String> result = client.register(username, password);
        if (result.isPresent())
        {
            System.out.println('\n' + result.get());
            return false;
        }
        return true;
    }

    private static void loginClient()
    {
        System.out.print("\nUsername: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        Optional<String> result = client.login(username, password);
        if (result.isPresent())
        {
            System.out.println('\n' + result.get());
            EnterWait();
            return;
        }
        user = username;
        authenticated = true;
    }

    private static void ClearTerminal ()
    {
        System.out.print("\033[H\033[2J");
    }

    private static void EnterWait ()
    {
         
        System.out.print("\u001B[999;1HPress Enter to continue");
        scanner.nextLine();
    }

    private static void command_request() 
    {
        System.out.println("\nType your desired command:\n");
        System.out.println("status - Memory available and number of pending tasks");
        System.out.println("exec <file> <mem> - Request to execute task based on <file> that uses <mem> memory in bytes");
        System.out.println("quit - Exit the program");
    }

    private static void execute ()
    {
        ClearTerminal();
        System.out.println("Welcome " +  user + '\n');
        System.out.println("\nDirectory to write the output files (leave in blank for the current directory)");
        System.out.print("Directory: ");
        String scannerDir = scanner.nextLine();
        client.setWrittingDir((scannerDir.equals("")) ? System.getProperty("user.dir") : scannerDir);
        command_request();
        String command;
        while (!(command = scanner.nextLine()).equals("quit")) 
        {
            client.handle(command);
        } 
    }

    private static void authenticate ()
    {
        while (!authenticated)
        {
            ClearTerminal();
            String command;
            System.out.println("\nRegister an account or log-in with your own:");
            System.out.println("Commands: register, login, quit");
            System.out.print("Command: ");
            command = scanner.nextLine();
            if (command.equals("register"))
            {
                if(registerClient())
                    System.out.println("\nRegistered Successfully!");
                EnterWait();
            }
            else if (command.equals("login"))
                loginClient();
            else if (command.equals("quit"))
                return;
            else 
            {
                System.out.println("Not an available command");
                EnterWait();
            }
        }
    }

    private static void connect()
    {
        boolean connected = false;
        while (!connected)
        {
            try {
                ClearTerminal();
                String server = Defines.defaultServer;
                int port = Defines.serverport;
                System.out.println("Please specify the address of the server (leave in blank for default '" + server + "')");
                System.out.print("Server: ");
                String scannerServer = scanner.nextLine();
                server = (!scannerServer.equals("")) ? scannerServer : server;
                System.out.println("\nPlease specify the port (leave in blank for default '" + port + "')");
                System.out.print("Port: ");
                String scannerPort = scanner.nextLine();
                port = (!scannerPort.equals("")) ? Integer.parseInt(scannerPort) : port;
                client = new Client(port, server);
                connected = true;
                System.out.println("\nSuccessfully connected to server");
            } catch (IOException e) {
                System.out.println("\nUnable to connect to server with specified address/port");
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid number for port (Please use integers)");
            }
            EnterWait();
        }
    }

    public static void main (String[] args)
    {
        connect();
        authenticate();
        if (authenticated)
            execute();
    }
}
