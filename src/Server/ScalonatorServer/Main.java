package Server.ScalonatorServer;

import java.net.Socket;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class Main
{
    public static void main (String[] args)
    {
        try
        {
            // print self machine ip
            System.out.println(InetAddress.getLocalHost());

            // setup state
            State state = new State();

            // setup client com-handler
            

            // setup scalonator

            // setup worker com-handler


        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
