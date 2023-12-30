package Server.ScalonatorServer;

import Protocol.Protocol;
import Protocol.Authentication.LoginReply;
import Protocol.Authentication.LoginRequest;
import Protocol.Authentication.RegistoReply;
import Protocol.Authentication.RegistoRequest;
import Server.Packet.Packet;
import Shared.LinkedBoundedBuffer;
import ThreadTools.ThreadControl;

public class Scalonator implements Runnable {
    
    private State state;
    private ThreadControl tc;


    public Scalonator (ThreadControl tc, State state)
    {
        this.state = state;
        this.tc = tc;
    }

    private void handleRegReq (Packet packet)
    {
        RegistoRequest request = (RegistoRequest) packet.protocol;
        LinkedBoundedBuffer<Protocol> client = state.getMap(packet.submitter);
        if (state.usermanager.existsUser(request.username))
        {
            try{
                client.put(new RegistoReply(false, "Username already exists."));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        state.usermanager.addUser(request.username, request.password);
        try {
            client.put(new RegistoReply(true, "User registered with success!"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Registered user: Username - " + request.username + "; Password - " + request.password);
    }

    private void handleLoginReq (Packet packet)
    {
        LoginRequest request = (LoginRequest) packet.protocol;
        LinkedBoundedBuffer<Protocol> client = state.getMap(packet.submitter);
        if (!state.usermanager.existsUser(request.username))
        {
            try{
                client.put(new LoginReply(false, "Username does not exist."));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        if (!state.usermanager.checkPassword(request.username, request.password))
        {
            try{
                client.put(new LoginReply(false, "Password Incorrect!"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        try {
            client.put(new LoginReply(true, "Successful login"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("User '" + request.username + "' logged in");
    }

    private void handleStatusReq (Packet packet)
    {
        int memoryAvailable = state.getMemoryAvailable();
    }


    private void handle (Packet packet) throws InterruptedException
    {
        switch (packet.protocol.type) {
            case EXEC_RQ:
                state.to_worker.put(packet);
                break;
            case STATUS_RQ:
                handleStatusReq(packet); 
            case LG_IN_RQ:
                handleLoginReq(packet);
                break;
            case REG_RQ:
                handleRegReq(packet);
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


