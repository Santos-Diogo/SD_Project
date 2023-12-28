package Server.Worker;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import Protocol.Protocol;
import Protocol.Exec.Response;
import Server.State;
import Server.Task.Task;
import sd23.*;
import Shared.LinkedBoundedBuffer;
import ThreadTools.ThreadControl;
import Protocol.Exec.GoodResponse;
import Protocol.Exec.BadResponse;

class WorkerThread implements Runnable
{
    private LinkedBoundedBuffer<Task> input;
    private State state;
    private ThreadControl tc;
    private ReentrantLock finished_lock;
    private Condition finished;

    public WorkerThread (LinkedBoundedBuffer<Task> input, State state, ThreadControl tc, ReentrantLock finished_lock, Condition finished)
    {
        this.input= input;
        this.state= state;
        this.tc= tc;
        this.finished_lock= finished_lock;
        this.finished= finished;
    }

    /**
     * Execute the task
     * @param t task
     * @return task result
     */
    private Response exec (Task t)
    {
        try
        {
            // executar a tarefa
            byte[] output = JobFunction.execute(t.arg);
            
            // return success or failure packages
            System.err.println("success, returned "+output.length+" bytes");
            return new GoodResponse(output);
        } 
        catch (JobFunctionException e) 
        {
            System.err.println("job failed: code="+e.getCode()+" message="+e.getMessage());
            return new BadResponse(e.getCode(), e.getMessage());
        }
    }

    public void run ()
    {
        while (this.tc.getRunning())
        {
            try
            {
                //take task
                Task t= this.input.take();
                //execute
                Response r= exec(t);
                //send result
                this.state.addResult(t.submitter, r);
                //mark as finished
                this.finished_lock.lock();
                this.finished.signalAll();
            }
            // we keep on trying to take tasks
            catch (InterruptedException e)
            {
            }
            finally
            {
                this.finished_lock.unlock();
            }
        }
    }
}
