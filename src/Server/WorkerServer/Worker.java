package Server.WorkerServer;

import ThreadTools.ThreadControl;
import Server.Shared.Packet.*;

public class Worker implements Runnable
{
    private ThreadControl tc;
    private State state;

    public Worker (ThreadControl tc, State state)
    {
        this.tc= tc;
        this.state= state;
    }

    public void run ()
    {
        while (this.tc.getRunning())
        {
            try
            {
                Packet p= this.state.inputQueue.take();
                this.state.outputQueue.add(handle(p));
            }
            catch (InterruptedException e) {}
        }
    }
}
