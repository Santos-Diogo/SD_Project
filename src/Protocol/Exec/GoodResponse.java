package Protocol.Exec;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class GoodResponse extends Response
{
    public byte[] response;

    public GoodResponse (byte[] response)
    {
        super(true);
        this.response= response;
    }

    public GoodResponse (byte[] response, Response packet)
    {
        super(packet);
        this.response = response;
    }

    @Override
    public void serialize (DataOutputStream out) throws IOException
    {
        super.serialize(out);
        out.writeInt(response.length);
        out.write(response);
        out.flush();
    }

    public static GoodResponse deserialize (DataInputStream in, Response packet) throws IOException
    {
        int length = in.readInt();
        byte[] response = new byte[length];
        in.read(response, 0, length);
        return new GoodResponse(response);
    }
}

