package Server.Shared.Packet;

public class Register extends Packet
{
    public long max_mem;
    
    public Register (long mem)
    {
        super (Type.REG);
        this.max_mem= mem;
    }
}
