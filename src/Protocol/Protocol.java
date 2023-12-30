package Protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Protocol 
{
    public enum Type {
        REG_RQ,
        REG_RP,
        LG_IN_RQ,
        LG_IN_RP,
        EXEC_RQ,
        EXEC_RP,
        STATUS_RQ,
        STATUS_RP
    }

    public Type type;

    public Protocol (Type type)
    {
        this.type = type;
    }

    public Protocol (Protocol copy)
    {
        this.type = copy.type;
    }

    
    public void serialize (DataOutputStream out) throws IOException
    {
        System.out.println(type.ordinal());
        out.writeInt(type.ordinal());
    }   

    public static Protocol deserialize (DataInputStream in) throws IOException
    {
        int type = in.readInt();
        System.out.println(type);
        return new Protocol(Type.values()[type]);
    }
}
