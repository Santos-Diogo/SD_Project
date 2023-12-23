package Server;

import Protocol.Exec.Request;

public class Server 
{
    public void handleExec (Request packet)
    {
        try 
        {
            // execute task and get output
            byte[] output = JobFunction.execute(packet.job);
        }
        catch (JobFunctionException e)
        {
            System.err.println("job failed: code="+e.getCode()+" message="+e.getMessage());
            // send error to client (TODO)
        }
    }
}
