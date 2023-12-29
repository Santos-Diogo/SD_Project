package Server.Packet;

import java.io.DataOutputStream;
import java.io.IOException;

public class Register extends Packet
{
    int max_mem;

    public Register (int max_mem)
    {
        super (Type.REG);
        this.max_mem= max_mem;
    }

    public void serialize (DataOutputStream out) throws IOException
    {
        out.writeInt(this.max_mem);
    }
}
