package Server;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import Protocol.Protocol;
import ThreadTools.ThreadControl;

public class Sender implements Runnable
{
    private BlockingQueue<Protocol> outputQueue;
    private ObjectOutputStream out;
    private ThreadControl tc;

    Sender (Socket socket, BlockingQueue<Protocol> outputQueue, ThreadControl tc)
    {
        try
        {
            this.outputQueue= outputQueue;
            this.out= new ObjectOutputStream(socket.getOutputStream());
            this.tc= tc;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void run ()
    {
        while (tc.getRunning())
        {
            try
            {
                out.writeObject(outputQueue.take());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
