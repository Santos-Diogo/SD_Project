package Server.Shared.Packet;

import java.io.DataOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.DataInputStream;

import Protocol.Protocol;

public class Packet 
{
    public Type type;
    

    public enum Type
    {
        REG,
        TASK,
        RESP
    }

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
        return new Packet(Type.values()[in.readInt()]);
    }
}
