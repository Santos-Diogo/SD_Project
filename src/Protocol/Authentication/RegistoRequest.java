package Protocol.Authentication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import Protocol.Protocol;

public class RegistoRequest extends Protocol{
    public String username;
    public String password;

    public RegistoRequest(String username, String password){
        super(Type.REG_RQ);
        this.username = username;
        this.password =password;
    }

    @Override
    public void serialize(DataOutputStream out) throws IOException
    {
        super.serialize(out);
        out.writeUTF(username);
        out.writeUTF(password);
        out.flush();
    }

    public static RegistoRequest deserialize (DataInputStream in) throws IOException
    {
        String username = in.readUTF();
        String password = in.readUTF();
        return new RegistoRequest(username, password);
    }

}
