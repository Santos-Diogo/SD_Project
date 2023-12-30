package Server.ScalonatorServer;

import java.io.IOException;

import Protocol.Authentication.LoginReply;
import Protocol.Authentication.LoginRequest;
import Protocol.Authentication.RegistoReply;
import Protocol.Authentication.RegistoRequest;
import Server.Packet.Packet;
import ThreadTools.ThreadControl;

public class Scalonator {
    
    private State state;
    private ThreadControl tc;


    public Scalonator (ThreadControl tc, State state)
    {
        this.state = state;
        this.tc = tc;
    }

    private void handleRegReq (RegistoRequest packet)
    {
        if (state.usermanager.existsUser(packet.username))
        {
            try{
                new RegistoReply(false, "Username already exists.").serialize(out);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        state.usermanager.addUser(packet.username, packet.password);
        try {
            new RegistoReply(true, "User registered with success!").serialize(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Registered user: Username - " + packet.username + "; Password - " + packet.password);
    }

    private void handleLoginReq (Packet packet)
    {
        if (!state.usermanager.existsUser(packet.username))
        {
            try{
                new LoginReply(false, "Username does not exist.").serialize(out);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        if (!state.usermanager.checkPassword(packet.username, packet.password))
        {
            try{
                new LoginReply(false, "Password Incorrect!").serialize(out);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        try {
            new LoginReply(true, "Successful login").serialize(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("User '" + packet.username + "' logged in");
    }


    private void handle (Packet packet) throws InterruptedException
    {
        switch (packet.protocol.type) {
            case REG_RQ:
                handleRegReq();
                break;
            case LG_IN_RQ:
                handleLoginReq();
                break;
            case EXEC_RQ:
                state.to_worker.put(packet);
                break;
            default:
                break;
        }
    }

    public void run()
    {
        while (tc.getRunning())
        {
            try {
                Packet packet = state.to_scalonator.take();
                handle(packet);
            } catch (InterruptedException e) {
                System.err.println("ERROR");
                e.printStackTrace();
            }
        }
    }


}


