package Client;

import java.io.IOException;
import java.net.Socket;
import java.util.Optional;

import Protocol.Authentication.LoginReply;
import Protocol.Authentication.RegistoReply;

public class Client implements IClient
{
    private Socket socket;
    private Handler handler;

    public Optional<String> register(String username, String password)
    {
        RegistoReply reply = handler.handleRegister(username, password);
        if (!reply.success)
            return Optional.of(reply.message);
        return Optional.empty();
    }

    public Optional<String> login(String username, String password)
    {
        LoginReply reply = handler.handleLogin(username, password);
        if (!reply.success)
            return Optional.of(reply.message);
        return Optional.empty();
    }

    public void handle (String command)
    {
        handler.handle(command);
    }
    
    public Client (Integer port, String addr) throws IOException
    {
        this.socket = new Socket(addr, port);
        this.handler= new Handler(socket);
    }

}
