package Client;

import Protocol.Exec.Request;

public class Client 
{
    /**
     * Produces a job to send to the Server
     * @param job
     */
    Request sendExec (byte[] job)
    {
        Request r= new Request(job);
        return r;
    }
}
