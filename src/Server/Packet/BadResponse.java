package Server.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import Protocol.Exec.Response;

public class BadResponse extends Packet
{
    public int error_code;
    public String error_message;
    
    public BadResponse (int error_code, String error_message)
    {
        super(Type.BDRESP);
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

    public static BadResponse deserialize (DataInputStream in, Response packet) throws IOException
    {
        return new BadResponse (in.readInt(), new String (in.readUTF())); 
    }
}
