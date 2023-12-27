package Protocol.Exec;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import Protocol.Protocol;

public class Response extends Protocol
{
    public int n_job;                      // Job the response is refering to
    public boolean success;                // If the job was successful or not

    public Response (boolean success, int n_job)
    {
        super (Type.EXEC_RP);
        this.n_job = n_job;
        this.success= success;
    }

    public Response (Response copy)
    {
        super(copy.type);
        this.n_job = copy.n_job;
        this.success = copy.success;
    }

    public Response (boolean success)
    {
        super (Type.EXEC_RP);
        this.success= success;
    }

    public Response (boolean success, int n_job, Protocol packet)
    {
        super (packet);
        this.n_job = n_job;
        this.success= success;
    }

    @Override
    public void serialize (DataOutputStream out) throws IOException
    {
        super.serialize(out);
        out.writeInt(n_job);
        out.writeBoolean(success);
        out.flush();
    }

    public static Response deserialize (DataInputStream in) throws IOException
    {
        Protocol packet = Protocol.deserialize(in);
        int n_job = in.readInt();
        boolean success = in.readBoolean();
        return new Response(success, n_job, packet);
    }
}
