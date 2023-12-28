package Server.WorkerServer;

import Server.Shared.Packet.Packet;
import java.util.concurrent.BlockingQueue;

public class State implements Runnable
{
    BlockingQueue<Packet> outputQueue;
    BlockingQueue<Packet> inputQueue;


    public State ()
    {

    }

    public void run ()
    {

    }
}
