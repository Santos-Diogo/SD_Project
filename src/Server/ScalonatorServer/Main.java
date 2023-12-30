package Server.ScalonatorServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import java.net.InetAddress;

import Server.ScalonatorServer.Communication.Client.ClientComManager;
import Server.ScalonatorServer.Communication.Worker.WorkerComManager;
import ThreadTools.ThreadControl;

public class Main
{
    static Scanner scanner= new Scanner(System.in);

    public static String commandRequest ()
    {
        System.out.println("\nType your desired command:");
        System.out.println("quit - terminate the exec");
        System.out.print("Command: ");
        return scanner.nextLine();
    }

    public static void main (String[] args)
    {
        try
        {
            // print self machine ip
            System.out.println(InetAddress.getLocalHost());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        List<Thread> threads= new ArrayList<>();

        State state = new State();
        ThreadControl tc= new ThreadControl();

        threads.add(new Thread(new ClientComManager(tc, state)));
        threads.add(new Thread(new Scalonator(tc, state)));
        threads.add(new Thread(new WorkerComManager(tc, state)));

        for (Thread t: threads)
        {
            t.start();
        }

        // wait for termination command
        while (commandRequest()!= "quit") {} 
        
        // terminate and cleanup
        tc.setRunning(false);
        for (Thread t: threads)
        {
            t.interrupt();
            try
            {
                t.join();
            }
            catch (InterruptedException e) {}
        }
    }
}
