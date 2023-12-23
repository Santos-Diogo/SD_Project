package Protocol.Exec;

import Protocol.Protocol;

public class Response extends Protocol
{
    public int n_job;                      // Job the response is refering to
    public boolean success;                // If the job was successful or not

    public Response (boolean success)
    {
        super (Type.EXEC_RP);
        this.success= success;
    }
}
