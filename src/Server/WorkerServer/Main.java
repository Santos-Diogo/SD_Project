package Server.WorkerServer;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Shared.Defines;
import ThreadTools.ThreadControl;


public class Main 
{
    static Scanner scanner = new Scanner (System.in);

    public static String commandRequest ()
    {
        System.out.println("\nType your desired command:");
        System.out.println("quit - terminate the exec");
        System.out.print("Command: ");
        return scanner.nextLine();
    }

    public static void main (String[] args)
    {
        // get the scalonator's ip adress
        String scalonator_adr= args[0];

        // get own memory
        int mem= Integer.valueOf(args[1]);

        try
        {
            // connect to the scalonator
            Socket socket= new Socket(scalonator_adr, Server.Shared.Defines.scalonator_worker_port);
            DataInputStream input= new DataInputStream(socket.getInputStream());
            DataOutputStream output= new DataOutputStream(socket.getOutputStream());

            // write mem to scalonator
            output.writeInt(mem);


            // create state, reciever, worker and transmitter
            ThreadControl tc= new ThreadControl();
            State state= new State(mem);

            List<Thread> threads= new ArrayList<>();
            
            threads.add(new Thread (new Receiver(tc, state, input)));
            threads.add(new Thread (new WorkerManager(tc, state)));
            threads.add(new Thread (new Transmitter (tc, output, state)));

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

            socket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }    
}
