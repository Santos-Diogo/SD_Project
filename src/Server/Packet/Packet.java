package Server.Packet;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
    public int submitter;
    public static Lock lock_inc= new ReentrantLock();
    public static int inc= 0;

    public Packet (Type type, int submitter)
    {
        this.type= type;
        
        try
        {
        }

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
