package Server.Worker;

import Server.State;
import java.lang.Thread;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import Server.Task.Task;
import Shared.Defines;
import ThreadTools.ThreadControl;

public class WorkerManager implements Runnable
{
    private State state;
    private ThreadControl tc;
    private Set<Thread> threads;
    private BlockingQueue<Task> worker_queue;
    private Condition worker_finished;              // used to signal worker_manager that a thread as finished

    public WorkerManager (State state, ThreadControl tc)
    {
        this.state= state;
        this.tc= tc;
        this.threads= new TreeSet<>();
        this.worker_queue= new LinkedBlockingQueue<>();
        this.worker_finished= new ReentrantLock().newCondition();
    }

    /**
     * add the tasks to the worker's queue as soon as some task in the set fits into the memory space
     * @param tasks
     */
    public void addQueue (Set<Task> tasks)
    {
        while (!tasks.isEmpty())
        {
            /* se o espaco que existe na memoria for suficiente para alguma task metemos essa task
            senao ficamos a espera ate uma worker thread acabar usando a condition do worker_finished */
        }
    }

    public void run ()
    {
        // setup worker threads
        for (int i= 0; i< Defines.MAX_WORKER_THREADS; i++)
        {
            Thread t= new Thread(new WorkerThread(worker_queue, state, tc, worker_finished));
            t.start();
            this.threads.add(t);
        }

        // send worker threads tasks
        Set<Task> tasks= new TreeSet<>();
        while (this.tc.getRunning())
        {
            // get available tasks
            state.taskQueue.drainTo(tasks);
            // add the tasks to the workers queue when possible
            addQueue(tasks);
        }

        // wrap things up
        for (Thread t: this.threads)
        {
            try
            {
                t.join();
            }
            catch (InterruptedException e){}
        }
    }
}   
