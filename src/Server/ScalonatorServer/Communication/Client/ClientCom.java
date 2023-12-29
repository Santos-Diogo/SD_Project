package Server.ScalonatorServer.Communication.Client;

import ThreadTools.ThreadControl;
import java.net.Socket;
import Server.ScalonatorServer.State;

public class ClientCom implements Runnable
{
    ThreadControl tc;
    Socket socket;
    State state;

    public ClientCom (ThreadControl tc, Socket socket, State state)
    {
        this.tc= tc;
        this.socket= socket;
        this.state= state;
    }

    public void run() 
    {
        while (this.tc.getRunning())
        {
            
        }
    }
}