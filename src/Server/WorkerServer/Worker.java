package Server.WorkerServer;

import ThreadTools.ThreadControl;
import Protocol.Exec.BadResponse;
import Protocol.Exec.GoodResponse;
import Protocol.Exec.Response;
import Server.Packet.Task;

public class Worker implements Runnable
{
    private ThreadControl tc;
    private State state;

    public Worker (ThreadControl tc, State state)
    {
        this.tc= tc;
        this.state= state;
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
                Task t= this.state.inputQueue.take();
                this.state.outputQueue.put(exec(t));
            }
            catch (InterruptedException e) {}
        }
    }
}
