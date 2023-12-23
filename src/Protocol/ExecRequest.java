package Protocol;

public class ExecRequest extends Protocol
{
    public byte[] arg;                  // Arguments of the exec
    public int nJob;                    // Number of the job
    public static int inc= 0;           // Incrementing the number of the job

    public ExecRequest (byte[] arg)
    {
        super (Type.EXEC_RQ);
        this.nJob= inc++;
    }
}
