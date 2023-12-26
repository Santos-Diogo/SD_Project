package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import Shared.Defines;

public class Main 
{
    private static State server_state;
    private static ServerSocket serverSocket;


    public static void main (String[] args)
    {
        int port = (args.length > 0) ? Integer.parseInt(args[0]) : Defines.serverport;
        try {
            serverSocket = new ServerSocket(port);
            server_state= new State();
            System.out.println("Server port not specified, initializing server on port " + port);
            while(true)
            {
                Socket socket = serverSocket.accept();
                new Thread(new Handler(server_state, socket)).start();
            }   
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
