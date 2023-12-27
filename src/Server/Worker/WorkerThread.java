package Server.Worker;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import sd23.*;
import Protocol.Protocol;
import Protocol.Exec.Response;
import Server.State;
import Server.Task.Task;
import ThreadTools.ThreadControl;
import Protocol.Exec.GoodResponse;
import Protocol.Exec.BadResponse;

class WorkerThread implements Runnable
{
    private BlockingQueue<Task> input;
    private State state;
    private ThreadControl tc;
    private Condition finished;

    public WorkerThread (BlockingQueue<Task> input, State state, ThreadControl tc, Condition finished)
    {
        this.input= input;
        this.state= state;
        this.tc= tc;
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
                System.out.println("depois do take workerthread");
                //execute
                Response r= exec(t);
                //send result
                this.state.addResult(t.submitter, r);
                //mark as finished
                this.finished.signalAll();
            }
            // we keep on trying to take tasks
            catch (InterruptedException e)
            {
            }
        }
    }
}
