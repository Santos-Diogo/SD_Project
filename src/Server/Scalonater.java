package Server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import Shared.Defines;

import java.util.Set;

public class Scalonater implements Runnable
{
    BlockingQueue<Task> inputQueue;
    BlockingQueue<Task> consumeQueue;
    Set<Thread> worker_threads;
    ThreadControl self_tc;                          // atribute to control own execution
    ThreadControl lower_tc;                         // atribute to control lower thread's execution

    public Scalonater (BlockingQueue<Task> inputQueue, ThreadControl tc)
    {
        this.inputQueue= inputQueue;
        this.consumeQueue= new LinkedBlockingQueue<>();
        this.self_tc= tc;
        this.lower_tc= new ThreadControl();
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
        while (this.self_tc.getRunning())
        {
            // drain available tasks into the scalonater and get the scalonated tasks
            Set<Task> tasks;
            inputQueue.drainTo(tasks);
            Set<Task> scalonated= scalonate(tasks);
            
            // send the scalonated tasks to the worker threads
            // check documentation for sets to see if any kind of order is perserved 
            // when we drain the tasks and when we add the scalonated tasks to the queue
            for (Task t: scalonated)
            {
                this.consumeQueue.add(t);
            }
        }

        // send termination signal and wait for worker threads to finish work
        this.lower_tc.set....();
        for (Thread t: this.worker_threads)
        {
            t.join();
        }
    }
}
