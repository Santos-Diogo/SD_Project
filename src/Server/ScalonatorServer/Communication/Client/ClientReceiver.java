package Server.ScalonatorServer.Communication.Client;

import ThreadTools.ThreadControl;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import Protocol.Protocol;
import Protocol.Exec.Request;
import Server.ScalonatorServer.State;

class ClientReceiver implements Runnable
{
    private ThreadControl tc;
    private DataInputStream input;
    private State state;

    ClientReceiver (ThreadControl tc, DataInputStream input, State state)
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
                this.state.to_scalonator.put(Protocol.deserialize(input));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }    
}
