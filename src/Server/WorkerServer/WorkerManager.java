package Server.WorkerServer;

import ThreadTools.ThreadControl;

import java.util.ArrayList;
import java.util.List;

import Server.Packet.Task;

public class WorkerManager implements Runnable
{
    public ThreadControl tc;
    public State state;
    public List<Thread> thread_list;

    public WorkerManager (ThreadControl tc, State state)
    {
        this.tc= tc;
        this.state= state;
        this.thread_list= new ArrayList<>();
    }

    public void run ()
    {
        //wait for termination
        while (this.tc.getRunning())
        {
            try
            {
                Task t= this.state.manager_queue.take();
                
            }
            catch (InterruptedException e) {}
        }   
    }
}
