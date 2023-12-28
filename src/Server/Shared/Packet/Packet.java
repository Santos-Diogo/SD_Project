package Server.Shared.Packet;

import java.io.DataOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.DataInputStream;

import Protocol.Protocol;

public class Packet 
{
    public Type type;
    public int submitter;
    public int task_number;

    public enum Type
    {
        TASK,
        RESP
    }

    public Packet (Type type, int submitter, int task_number)
    {
        this.type= type;
        this.submitter= submitter;
        this.task_number= task_number;
    }

    public void serialize (DataOutputStream out) throws IOException
    {
        out.writeInt(this.type.ordinal());
        out.writeInt(this.submitter);
        out.writeInt(this.task_number);
    }

    public static Packet deserialize (DataInputStream in) throws IOException
    {
        return new Packet(Type.values()[in.readInt()], in.readInt(), in.readInt());
    }
}
