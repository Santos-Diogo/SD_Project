package Server.WorkerServer;

import sd23.*;
import Protocol.Exec.BadResponse;
import Protocol.Exec.GoodResponse;
import Protocol.Exec.Request;
import Server.Packet.Packet;
import Server.WorkerServer.State.Output;

public class Worker implements Runnable
{
    private State state;
    private Packet packet;


    public Worker (State state, Packet packet)
    {
        this.state= state;
        this.packet = packet;
    }

    /**
     * Execute the task
     * @param t task
     * @return task result
     */
    private Output exec (Packet t)
    {
        Request r= (Request) t.protocol;
        try
        {

            // executar a tarefa
            byte[] output = JobFunction.execute(r.arg);
            
            // return success or failure packages
            System.err.println("success, returned "+output.length+" bytes");
            return this.state.new Output(r.mem, new Packet(new GoodResponse(output), t.submitter));
        } 
        catch (JobFunctionException e)
        {
            System.err.println("job failed: code="+e.getCode()+" message="+e.getMessage());
            return this.state.new Output(r.mem, new Packet(new BadResponse(e.getCode(), e.getMessage()), t.submitter));
        }
    }

    public void run ()
    {
        try {
            this.state.output_queue.put(exec(this.packet));
        } catch (InterruptedException e) {{{{{{{{{{{{{{{}}}}}}}}}}}}}}}
    }
}
