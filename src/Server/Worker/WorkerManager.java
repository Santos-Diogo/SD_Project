package Server.Worker;

import Server.State;
import java.lang.Thread;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.Set;
import java.util.HashSet;
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
    private ReentrantLock finished_lock;
    private Condition worker_finished;              // used to signal worker_manager that a thread as finished

    public WorkerManager (State state, ThreadControl tc)
    {
        this.state= state;
        this.tc= tc;
        this.threads= new HashSet<>();
        this.worker_queue= new LinkedBlockingQueue<>();
        this.finished_lock= new ReentrantLock();
        this.worker_finished= this.finished_lock.newCondition();
    }

    /**
     * add the tasks to the worker's queue as soon as some task in the set fits into the memory space
     * @param tasks
     */
    public void addQueue (Set<Task> tasks)
    {
        while (!tasks.isEmpty())
        {
            long mem= this.state.getAvailableMemory();
            
            // try to get a task that fits
            Task t= null;
            for (Task setTask: tasks)
            {
                // if task fits
                if (mem> setTask.mem)
                {
                    // set to execute the task and remove it from the task set
                    t= setTask;
                    tasks.remove(setTask);
                }
            }

            // if we found a task that fits
            if (t!= null)
            {
                // add task to exec queue
                this.worker_queue.add(t);
            }
            else
            {
                // wait for a worker to finish
                try
                {
                    this.worker_finished.await();
                }
                catch (InterruptedException e) {}
            }
        }
    }

    public void run ()
    {
        // setup worker threads
        for (int i= 0; i< Defines.MAX_WORKER_THREADS; i++)
        {
            Thread t= new Thread(new WorkerThread(worker_queue, state, tc, finished_lock, worker_finished));
            t.start();
            this.threads.add(t);
        }

        // send worker threads tasks
        Set<Task> tasks= new HashSet<>();
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
