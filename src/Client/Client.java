package Client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import Protocol.Authentication.LoginReply;
import Protocol.Authentication.RegistoReply;
import Shared.Defines;
import Shared.LinkedBoundedBuffer;
import ThreadTools.ThreadControl;

public class Client implements IClient
{
    private Socket socket;
    private LinkedBoundedBuffer<String> commands;
    private List<Thread> sendingThreads;
    private Thread receivingThread;
    private DataOutputStream out;
    private DataInputStream in;
    private ThreadControl tc;


    public Client (Integer port, String addr) throws IOException
    {
        this.socket = new Socket(addr, port);
        this.commands = new LinkedBoundedBuffer<>();
        this.sendingThreads = new ArrayList<>();
        out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        this.tc = new ThreadControl();
        for (int i = 0; i < Defines.MAX_CLIENT_THREADS; i++)
        {
            Thread t = new Thread(new HandleSend(commands, tc, out));
            sendingThreads.add(t);
            t.start();
        }
    }

    public Optional<String> register(String username, String password)
    {
        RegistoReply reply = HandleAuthentication.handleRegister(username, password, out, in);
        if (!reply.success)
            return Optional.of(reply.message);
        return Optional.empty();
    }

    public Optional<String> login(String username, String password)
    {
        LoginReply reply = HandleAuthentication.handleLogin(username, password, out, in);
        if (!reply.success)
            return Optional.of(reply.message);
        return Optional.empty();
    }

    public void setWrittingDir (String writtingDir)
    {
        this.receivingThread = new Thread(new HandleReceive(in, tc, writtingDir, this));
        this.receivingThread.start();
    }

    public void handle (String command)
    {
        try {
            commands.put(command);
        } catch (InterruptedException e) {
            System.err.println("Couldn't handle command");
            e.printStackTrace();
        }
    }

    public void quit()
    {
        try{
            tc.setRunning(false);
            for (Thread t : sendingThreads)
            {
                t.interrupt();
                t.join();
            }
            in.close();
            receivingThread.interrupt();
            receivingThread.join();
        } catch(Exception e) {
        } finally {  
            try {
                out.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean serverRunning ()
    {
        return tc.getRunning();
    }
}
