package Server.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Register extends Packet
{
    int max_mem;

    public Register (int max_mem)
    {
        super (Type.REG, -42069);
        this.max_mem= max_mem;
    }

    @Override
    public void serialize (DataOutputStream out) throws IOException
    {
        super.serialize(out);
        out.writeInt(this.max_mem);
    }

    public static Register deserialize (DataInputStream in) throws IOException
    {
       return new Register(in.readInt());
    }
}
