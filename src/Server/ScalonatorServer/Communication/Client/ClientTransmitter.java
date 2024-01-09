package Server.ScalonatorServer.Communication.Client;

import java.io.DataOutputStream;
import java.io.IOException;

import Protocol.Protocol;
import ThreadTools.ThreadControl;
import Shared.LinkedBoundedBuffer;

public class ClientTransmitter implements Runnable
{
    private ThreadControl tc;
    private LinkedBoundedBuffer<Protocol> input;
    private DataOutputStream out;

    ClientTransmitter (ThreadControl tc, DataOutputStream out, LinkedBoundedBuffer<Protocol> input)
    {
        this.tc= tc;
        this.input= input;
        this.out= out;
    }

    public void run ()
    {
        while (this.tc.getRunning())
        {
            try
            {
                Protocol p= this.input.take();
                p.serialize(out);
                //out.flush();
            }
            catch (InterruptedException e) {}
            catch (IOException e)
            {
                e.printStackTrace();
                break;
            }
        }
    }   
}
