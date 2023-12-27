package Client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

import Protocol.Authentication.LoginReply;
import Protocol.Authentication.LoginRequest;
import Protocol.Authentication.RegistoReply;
import Protocol.Authentication.RegistoRequest;
import Protocol.Exec.BadResponse;
import Protocol.Exec.GoodResponse;
import Protocol.Exec.Request;
import Protocol.Exec.Response;
import Protocol.Status.StatusREP;
import Protocol.Status.StatusREQ;

public class Handler
{
    State state;                // We handle commands and update the state
    DataInputStream in;
    DataOutputStream out;

    public Handler (State state, Socket socket) throws IOException
    {
        this.state= state;
        in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    private byte[] getContent(String file) throws IOException
    {
        Path file_path = Path.of(file);
        return Files.readAllBytes(file_path);
    }

    private void handleExecSuc (Response packet) throws IOException
    {
        GoodResponse response = GoodResponse.deserialize(in, packet);
        System.out.println("Success, returned " + response.response.length + " bytes");
    }

    private void handleExecFail (Response packet) throws IOException
    {
        BadResponse response = BadResponse.deserialize(in, packet);
        System.err.println("job failed: code="+response.error_code+" message="+response.error_message);
    }


    //primeira versão; numa próxima provavelmente o melhor será ter isto numa thread para evitar casos em q ler o ficheiro demora demasiado tempo
    private void handleExec(String file, Integer mem)
    {
        try {
            byte[] job = getContent(file);
            Request msg = new Request(job, mem);
            msg.serialize(out);
            Response packet = Response.deserialize(in);
            if(packet.success)
                handleExecSuc(packet);
            else
                handleExecFail(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleStatus ()
    {
        try {
            new StatusREQ().serialize(out);
            StatusREP packet = StatusREP.deserialize(in);
            System.out.println("Memory available: " + packet.available_memory);
            System.out.println("Number of Pending tasks: " + packet.number_of_tasks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean handleLogin (String username, String password)
    {
        LoginRequest loginReq = new LoginRequest(username, password);
        try{
            loginReq.serialize(out);
            LoginReply loginRep = LoginReply.deserialize(in);
            System.out.println(loginRep.message);
            return loginRep.success;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean handleRegister (String username, String password)
    {
        RegistoRequest registerReq = new RegistoRequest(username, password);
        try{
            registerReq.serialize(out);
            RegistoReply registerRep = RegistoReply.deserialize(in);
            System.out.println(registerRep.message);
            return registerRep.success;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    void handle (String command)
    {
        switch (command.split("\\s+")[0])
        {
            case "exec":
            {
                handleExec(command.split("\\s+")[1], Integer.parseInt(command.split("\\s+")[2]));
                break;
            }
            case "status":
            {
                handleStatus();
                break;
            }
            default:
                System.out.println("Command not supported");
        }
    }
}
