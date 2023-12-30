package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import Protocol.Authentication.LoginReply;
import Protocol.Authentication.LoginRequest;
import Protocol.Authentication.RegistoReply;
import Protocol.Authentication.RegistoRequest;

public class HandleAuthentication
{
    public static LoginReply handleLogin (String username, String password, DataOutputStream out, DataInputStream in)
    {
        LoginRequest loginReq = new LoginRequest(username, password);
        try{
            loginReq.serialize(out);
            LoginReply loginRep = LoginReply.deserialize(in);
            return loginRep;
        } catch (IOException e) {
            e.printStackTrace();
            return new LoginReply(false, "Error in deserialization");
        }
    }

    public static RegistoReply handleRegister (String username, String password, DataOutputStream out, DataInputStream in)
    {
        RegistoRequest registerReq = new RegistoRequest(username, password);
        try{
            registerReq.serialize(out);
            RegistoReply registerRep = RegistoReply.deserialize(in);
            return registerRep;
        } catch (IOException e) {
            return new RegistoReply(false, "Error in deserialization");
        }
    }

}
