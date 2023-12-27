package Protocol.Authentication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import Protocol.Protocol;

public class LoginReply extends Protocol{
    public boolean success;
    public String message;

    public LoginReply(boolean success, String message){
        super(Type.LG_IN_RP);
        this.success=success;
        this.message=message;
    }

    @Override
    public void serialize (DataOutputStream out) throws IOException
    {
        super.serialize(out);
        out.writeBoolean(success);
        out.writeUTF(message);
        out.flush();
    }

    public static LoginReply deserialize (DataInputStream in) throws IOException
    {
        Protocol.deserialize(in);
        boolean success = in.readBoolean();
        String message = in.readUTF();
        return new LoginReply(success, message);
    }

}
