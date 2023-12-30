package Server.ScalonatorServer;

import java.util.Map;
import java.util.Set;

import Protocol.Protocol;
import Protocol.Authentication.LoginReply;
import Protocol.Authentication.LoginRequest;
import Protocol.Authentication.RegistoReply;
import Protocol.Authentication.RegistoRequest;
import Protocol.Exec.Request;
import Server.Packet.Packet;
import Server.ScalonatorServer.State.WorkerData;
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
        LinkedBoundedBuffer<Protocol> client = state.getQueueClient(packet.submitter);
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
        LinkedBoundedBuffer<Protocol> client = state.getQueueClient(packet.submitter);
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

    private void scalonate (Packet packet)
    {
        Request request = (Request) packet.protocol;
        Set<Map.Entry<Integer, WorkerData>> workers = state.getWorkersWithMem(request.mem);
        //Use best-fit
        int best_worker = -1;
        int min_memory = -1;
        for(Map.Entry<Integer, WorkerData> w: workers)
        {
            if (w.getValue().available_mem < min_memory)
            {
                min_memory = w.getValue().available_mem;
                best_worker = w.getKey();
            }
        }
        try {
            state.getQueueWorker(best_worker).put(packet);
        } catch (InterruptedException e)
        {
            System.err.println("Could not update worker queue");
            e.printStackTrace();
        }
        state.removeWorkerMem(best_worker, request.mem);
    }

    private void handle (Packet packet) throws InterruptedException
    {
        switch (packet.protocol.type) {
            case EXEC_RQ:
                scalonate(packet);
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


