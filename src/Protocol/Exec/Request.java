package Protocol.Exec;

import java.util.concurrent.locks.ReentrantLock;

import Protocol.Protocol;

public class Request extends Protocol
{
    public byte[] arg;                                      // Arguments of the exec
    public int mem;                                         // Memory needed
    public int n_job;                                       // Number of the job
    private static int inc= 0;                              // Incrementing the number of the job
    private static ReentrantLock rl= new ReentrantLock();   // Used to ensure exclusion in the constructor

    public Request (byte[] arg, int mem)
    {
        super (Type.EXEC_RQ);
        try
        {
            this.arg= arg;
            this.mem= mem;
            rl.lock();
            this.n_job= inc++;
        }
        finally
        {
            rl.unlock();
        }
    }
}
