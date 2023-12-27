package Server;

import java.util.concurrent.BlockingQueue;

import Protocol.Protocol;
import Protocol.Exec.Response;
import Server.Task.Task;
import ThreadTools.ThreadControl;

class WorkerThread implements Runnable
{
    private State server_state;
    private ThreadControl tc;

    WorkerThread (State server_state, ThreadControl tc)
    {
        this.server_state= server_state;
        this.tc= tc;
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
            
            // utilizar o resultado ou reportar o erro
            System.err.println("success, returned "+output.length+" bytes");

            // send result to the results queue

        } 
        catch (JobFunctionException e) 
        {
            System.err.println("job failed: code="+e.getCode()+" message="+e.getMessage());     
        }
    }

    public void run ()
    {
        while (this.tc.getRunning())
        {
            try
            {
                //take task
                Task t= this.server_state.taskQueue.take();
                //execute
                Response r= exec(t);
                //send result
                server_state.addResult(t.submitter, r);
            }
            // we keep on trying to take tasks
            catch (InterruptedException e)
            {
            }
        }
    }
}
