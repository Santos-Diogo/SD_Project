package Server.WorkerServer;

import java.io.DataOutputStream;
import java.io.IOException;

import Protocol.Exec.Response;
import Server.WorkerServer.State.Output;
import ThreadTools.ThreadControl;

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
                Output outp_pack= this.state.output_queue.take();
                this.output.writeInt(outp_pack.mem);
                outp_pack.p.serialize(output);
                this.output.flush();
            }
            catch (InterruptedException e){}
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
