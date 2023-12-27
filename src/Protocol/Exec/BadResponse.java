package Protocol.Exec;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import Shared.ClientServer.ExecError;

public class BadResponse extends Response
{
    public int error_code;
    public String error_message;
    
    public BadResponse (int error_code, String error_message)
    {
        super(false);
        this.error_code= error_code;
        this.error_message= error_message;
    }

    @Override
    public void serialize (DataOutputStream out) throws IOException
    {
        out.writeInt(error_code);
        out.writeUTF(error_message);
    }

    public static BadResponse deserialize (DataInputStream in, Response packet)
    {
        //@TODO
        new BadResponse(null);
        return 
    }
}
