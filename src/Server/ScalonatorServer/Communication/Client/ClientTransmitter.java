package Server.ScalonatorServer.Communication.Client;

import java.io.DataOutputStream;
import java.io.IOException;

import Server.ScalonatorServer.State;
import ThreadTools.ThreadControl;

public class ClientTransmitter implements Runnable
{
    private ThreadControl tc;
    private State state;
    private DataOutputStream out;

    ClientTransmitter (ThreadControl tc, State state, DataOutputStream out)
    {
        this.tc= tc;
        this.state= state;
        this.out= out;
    }

    public void run ()
    {
        while (this.tc.getRunning())
        {
            
            try
            {
                this.state.to_client.take().serialize(out);
            }
            catch (InterruptedException e) {}
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }   
}
