package Server.Packet;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import Protocol.Protocol;

public class Packet 
{
    public Protocol protocol;
    public int submitter;

    public Packet (Protocol protocol, int submitter)
    {
        this.protocol= protocol;
        this.submitter = submitter;
    }

    public void serialize (DataOutputStream out) throws IOException
    {
        protocol.serialize(out);
        out.writeInt(submitter);
    }

    public static Packet deserialize (DataInputStream in) throws IOException
    {
        Protocol protocol = Protocol.deserialize(in);
        int submitter = in.readInt();
        return new Packet (protocol, submitter);
    }
}
