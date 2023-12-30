package Server.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BadResponse extends Packet
{
    public int error_code;
    public String error_message;
    
    public BadResponse (int submitter, int error_code, String error_message)
    {
        super(Type.BDRESP, submitter);
        this.error_code= error_code;
        this.error_message= error_message;
    }

    @Override
    public void serialize (DataOutputStream out) throws IOException
    {
        super.serialize(out);
        out.writeInt(error_code);
        out.writeUTF(error_message);
    }

    public static BadResponse deserialize (DataInputStream in, Packet packet) throws IOException
    {
        return new BadResponse (in.readInt(), in.readInt(), new String (in.readUTF())); 
    }
}
