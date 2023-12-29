package Server.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Task extends Packet
{
    public int submitter;           // task submitter id
    public int num;                 // task id
    public int mem;                 // mem
    public byte[] arg;              // Task's arguments

    /**
     * 
     * @param submitter
     * @param num
     * @param mem
     * @param arg
     */
    public Task(int submitter, int num, int mem, byte[] arg) 
    {
        super(Type.TASK);
        this.submitter= submitter;
        this.num= num;
        this.mem = mem;
        this.arg= arg;
    }

    public void serialize (DataOutputStream out) throws IOException
    {
        super.serialize(out);
        out.writeInt(this.submitter);
        out.writeInt(this.num);
        out.writeInt(this.mem);
        out.writeInt(this.arg.length);
        out.write(arg);
    }

    public static Task deserialize (DataInputStream in) throws IOException
    {
        Packet.deserialize(in);
        int submitter= in.readInt();
        int num= in.readInt();
        int mem= in.readInt();
        int length= in.readInt();
        byte[] payload = new byte[length];
        in.read(payload, 0, length);
        return new Task(submitter, num, mem, payload);
    }
}
