package Server.WorkerServer;

import java.io.DataOutputStream;
import java.io.IOException;

import Protocol.Exec.Response;
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
        // register with own memory
        try
        {
            output.writeLong(state.max_mem);
        }
        catch (IOException e)
        {
            System.err.println("Worker couldnt regist to the scalonator");
            e.printStackTrace();
        }
        
        while (this.tc.getRunning())
        {
            try
            {
                Response r= this.state.outputQueue.take();
                r.serialize(output);
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
