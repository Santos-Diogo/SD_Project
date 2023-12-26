package Server.Task;

import java.util.concurrent.locks.ReentrantLock;


public class TaskMaker 
{
    private static int inc= 0;
    private static ReentrantLock l= new ReentrantLock();
    public int submitter;                           // submitter number
    public int task_inc;                            // task increment number

    public TaskMaker ()
    {
        try
        {
            this.task_inc= 0;
            l.lock();
            this.submitter= inc++;
        }
        finally
        {
            l.unlock();
        }
    }

    /**
     * Returns a task marked with submitter and numbered to self task_increment
     * @param arg
     * @return
     */
    public Task newTask (byte[] arg)
    {
        return new Task(submitter, this.task_inc++, arg);
    }
}
