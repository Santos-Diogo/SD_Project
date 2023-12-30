package Server.ScalonatorServer.Communication.Worker;

import java.io.DataInputStream;
import java.io.IOException;

import Protocol.Protocol;
import Server.ScalonatorServer.State;
import Shared.LinkedBoundedBuffer;
import ThreadTools.ThreadControl;

public class WorkerReceiver implements Runnable
{
    ThreadControl tc;
    DataInputStream input;
    State state;

    WorkerReceiver (ThreadControl tc, DataInputStream input, State state)
    {
        this.tc= tc;
        this.input= input;
        this.state= state;
    }

    public void run ()
    {
        while (this.tc.getRunning())
        {
            try
            {
                Protocol p= Protocol.deserialize(input);
                LinkedBoundedBuffer<Protocol> output= this.state.getMap(p.);
                output.put(p);
            }
            catch (InterruptedException e) {}
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }    
}
