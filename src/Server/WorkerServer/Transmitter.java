package Server.WorkerServer;

import java.io.DataOutputStream;
import java.io.IOException;

import ThreadTools.ThreadControl;
import Server.Shared.Packet.Packet;

public class Transmitter implements Runnable
{
    private ThreadControl tc;
    private DataOutputStream output;
    private State state;

    public Transmitter (ThreadControl tc, DataOutputStream output, State state)
    {
        this.tc= tc;
        this.output= output;
        this.state= state;
    }

    public void run ()
    {
        while (this.tc.getRunning())
        {
            try
            {
                Packet p= this.state.outputQueue.take();
                p.serialize(output);
                output.flush();
            }
            catch (InterruptedException e){}
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}