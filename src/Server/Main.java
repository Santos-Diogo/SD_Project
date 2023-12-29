package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import Server.Worker.WorkerManager;
import Shared.Defines;
import ThreadTools.ThreadControl;

public class Main 
{
    private static State server_state;
    private static ServerSocket serverSocket;
    private static ThreadControl tc;

    public static void main (String[] args)
    {
        int port = (args.length > 0) ? Integer.parseInt(args[0]) : Defines.serverport;
        try 
        {
            tc = new ThreadControl();
            serverSocket = new ServerSocket(port);
            server_state= new State();
            new Thread (new WorkerManager(server_state, tc)).start();

            System.out.println("Server port not specified, initializing server on port " + port);
            
            while(true)
            {
                Socket socket = serverSocket.accept();
                new Thread(new Handler(server_state, socket)).start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally
        {
            tc.setRunning(false);
        }
    }
}
