package Protocol.Exec;

public class GoodResponse extends Response
{
    public byte[] response;

    GoodResponse (byte[] response)
    {
        super(true);
        this.response= response;
    }
}
