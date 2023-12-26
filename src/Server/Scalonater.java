package Server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import Protocol.Protocol;
import Shared.Defines;
import ThreadTools.ThreadControl;

import java.util.Set;
import java.util.TreeSet;

public class Scalonater implements Runnable
{
    BlockingQueue<Task> inputQueue;
    BlockingQueue<Protocol> outputQueue;
    Set<Thread> worker_threads;
    ThreadControl self_tc;                          // atribute to control own execution
    ThreadControl lower_tc;                         // atribute to control lower thread's execution

    public Scalonater (BlockingQueue<Task> inputQueue, ThreadControl tc)
    {
        this.inputQueue= inputQueue;
        this.self_tc= tc;
        this.lower_tc= new ThreadControl();
    }

    public void run ()
    {
        // intialize the worker threads
        for (int i= 0; i< Defines.MAX_WORKER_THREADS; i++)
        {
            Thread t= new Thread(new WorkerThread (inputQueue, outputQueue, lower_tc));
            this.worker_threads.add(t);
        }

        //wait
        while (this.self_tc.getRunning())
        {
        }

        // send termination signal and wait for worker threads to finish work
        this.lower_tc.setRunning(false);
        for (Thread t: this.worker_threads)
        {
            try
            {
                t.join();
            }
            //we keep on trying to join with the worker threads
            catch (InterruptedException e)
            {
            }
        }
    }
}
