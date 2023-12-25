package Server;

import java.util.concurrent.BlockingQueue;

import Protocol.Protocol;
import Protocol.Exec.Response;
import ThreadTools.ThreadControl;

class WorkerThread implements Runnable
{
    private BlockingQueue<Task> tasks;
    private ThreadControl tc;
    private BlockingQueue<Protocol> output;

    WorkerThread (BlockingQueue<Task> tasks, ThreadControl tc)
    {
        this.tasks= tasks;
        this.tc= tc;
    }

    private Response exec (Task t)
    {
        try
        {

            // executar a tarefa
            byte[] output = JobFunction.execute(job);
            
            // utilizar o resultado ou reportar o erro
            System.err.println("success, returned "+output.length+" bytes");
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
                Task t= this.tasks.take();
                Protocol p= exec(t);
                output.add(p);
            }
            // we keep on trying to take tasks
            catch (InterruptedException e)
            {
            }
        }
    }
}
