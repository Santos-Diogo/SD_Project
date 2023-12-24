package Server;

import java.util.concurrent.BlockingQueue;

import Shared.Defines;

import java.util.Set;

public class Scalonater implements Runnable
{
    BlockingQueue<Task> inputQueue;
    BlockingQueue<Task> consumeQueue;
    Set<Thread> worker_threads;
    ThreadControl tc;

    public Scalonater (BlockingQueue<Task> inputQueue, ThreadControl tc)
    {
        this.inputQueue= inputQueue;
    }

    private Set<Task> scalonate (Set<Task> input_tasks)
    {
        //we can do come salonation after
        return input_tasks;
    }

    public void run ()
    {
        // intialize the worker threads
        for (int i= 0; i< Defines.MAX_WORKER_THREADS; i++)
        {
            Thread t= new Thread(new WorkerThread (consumeQueue));
            this.worker_threads.add(t);
        }

        // run this on a timer
        while (this.tc.getRunning())
        {
            //drain available tasks into the scalonater and get the scalonated tasks
            Set<Task> tasks;
            inputQueue.drainTo(tasks);
            scalonate();

            //send the scalonated tasks to the worker threads

        }

        // join up with the other threads
        for (Thread t: this.worker_threads)
        {
            t.join();
        }
    }
}
