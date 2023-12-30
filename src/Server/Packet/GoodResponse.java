package Server.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class GoodResponse extends Packet
{
    public byte[] response;

    public GoodResponse (byte[] response, int submitter)
    {
        super(Type.GDRESP, submitter);
        this.response= response;
    }

    public void serialize (DataOutputStream out) throws IOException
    {
        super.serialize(out);
        out.writeInt(response.length);
        out.write(response);
    }

    public static GoodResponse deserialize (DataInputStream in, Packet packet) throws IOException
    {
        int length = in.readInt();
        byte[] response = new byte[length];
        in.read(response, 0, length);
        return new GoodResponse(response, packet.submitter);
    }
}
