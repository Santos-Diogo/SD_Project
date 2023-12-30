package Protocol.Exec;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
        this.arg= arg;
        this.mem= mem;
        rl.lock();
        try
        {
            this.n_job= inc++;
        }
        finally
        {
            rl.unlock();
        }
    }

    public Request (byte[] arg, int mem, int n_job)
    {
        super(Type.EXEC_RQ);
        this.arg = arg;
        this.mem = mem;
        this.n_job = n_job;
    }

    @Override
    public void serialize (DataOutputStream out) throws IOException
    {
        super.serialize(out);
        out.writeInt(arg.length);
        out.write(arg);
        out.writeInt(mem);
        out.writeInt(n_job);
        out.flush();
    }

    public static Request deserialize (DataInputStream in) throws IOException
    {   
        int length = in.readInt();
        byte[] arg = new byte[length];
        in.read(arg, 0, length);
        int mem = in.readInt();
        int n_job = in.readInt();
        return new Request(arg, mem, n_job);
    }
}
