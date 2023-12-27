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
    private static WorkerManager worker_manager;
    private static ThreadControl tc;

    public static void main (String[] args)
    {
        int port = (args.length > 0) ? Integer.parseInt(args[0]) : Defines.serverport;
        try 
        {
            serverSocket = new ServerSocket(port);
            server_state= new State();
            worker_manager= new WorkerManager(server_state, tc);

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
