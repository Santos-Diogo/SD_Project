package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import Protocol.Protocol;
import ThreadTools.ThreadControl;

public class Receiver implements Runnable
{
    private ObjectInputStream in;
    private SocketManager manager;
    private ThreadControl tc;

    Receiver (SocketManager manager, Socket s, ThreadControl tc)
    {
        try
        {     
            this.in= new ObjectInputStream (s.getInputStream());
            this.manager= manager;
            this.tc= tc;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void handle (Protocol p)
    {
        BlockingQueue<Protocol> queue= manager.getInputQueue();
        queue.add(p);
    }

    public void run ()
    {
        while (this.tc.getRunning())
        {
            try
            {
                Protocol p= (Protocol) in.readObject();
                if (this.tc.getRunning())
                    handle (p);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}

