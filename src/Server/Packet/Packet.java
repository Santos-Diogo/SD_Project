package Server.Packet;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet 
{
    public enum Type
    {
        REG,
        TASK,
        GDRESP,
        BDRESP
    }

    public Type type;

    public Packet (Type type)
    {
        this.type= type;
    }

    public void serialize (DataOutputStream out) throws IOException
    {
        out.writeInt(this.type.ordinal());
    }

    public static Packet deserialize (DataInputStream in) throws IOException
    {
        return new Packet (Type.values()[in.readInt()]);
    }
}
