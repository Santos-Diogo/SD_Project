package Protocol.Exec;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
        super.serialize(out);
        out.writeInt(error_code);
        out.writeUTF(error_message);
        out.flush();
    }

    public static BadResponse deserialize (DataInputStream in) throws IOException
    {
        return new BadResponse (in.readInt(), in.readUTF()); 
    }
}
