package Server;

import java.util.concurrent.BlockingQueue;

import Protocol.Protocol;
import Protocol.Exec.Response;
import ThreadTools.ThreadControl;

class WorkerThread implements Runnable
{
    private BlockingQueue<Task> input;
    private BlockingQueue<Protocol> output;
    private ThreadControl tc;

    WorkerThread (BlockingQueue<Task> input, BlockingQueue<Protocol> output, ThreadControl tc)
    {
        this.input= input;
        this.tc= tc;
    }

    private Response exec (Task t)
    {
        try
        {
            // executar a tarefa
            byte[] output = JobFunction.execute(t.arg);
            
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
                Task t= this.input.take();
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
