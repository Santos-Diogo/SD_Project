package Server.WorkerServer;

import ThreadTools.ThreadControl;

import java.util.ArrayList;
import java.util.List;

import Server.Shared.Defines;

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
        //create threads
        for (int i= 0; i< Defines.MAX_WORKER_THREADS; i++)
        {
            this.thread_list.add(new Thread(new Worker(tc, state)));
        }

        //wait for termination
        tc.waitTerm();
        for (Thread t: this.thread_list)
        {
            try
            {
                t.interrupt();
                t.join();
            }
            catch (InterruptedException e) {}
        }
    }
}
