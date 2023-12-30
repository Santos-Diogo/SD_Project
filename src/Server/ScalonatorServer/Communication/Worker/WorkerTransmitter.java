package Server.ScalonatorServer.Communication.Worker;

import java.io.DataOutputStream;
import java.io.IOException;

import Server.Packet.Packet;
import Shared.LinkedBoundedBuffer;
import ThreadTools.ThreadControl;

public class WorkerTransmitter implements Runnable
{
    private ThreadControl tc;
    private LinkedBoundedBuffer<Packet> input;
    private DataOutputStream output;

    WorkerTransmitter (ThreadControl tc, LinkedBoundedBuffer<Packet> input, DataOutputStream output)
    {
        this.tc= tc;
        this.input= input;
        this.output= output;
    }

    public void run ()
    {
        while (this.tc.getRunning())
        {
            try
            {
                Packet p= this.input.take();
                p.serialize(output);
                output.flush();
            }
            catch (InterruptedException e) {}
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
