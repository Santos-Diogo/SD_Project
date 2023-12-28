package Server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import Protocol.Protocol;
import Protocol.Authentication.*;
import Protocol.Exec.Request;
import Protocol.Exec.Response;
import Protocol.Status.StatusREP;
import Server.Task.TaskMaker;
import Shared.LinkedBoundedBuffer;

public class Handler implements Runnable
{
    private State server_state;
    private TaskMaker task_maker;
    private LinkedBoundedBuffer<Response> task_result;
    private String user;
    private DataInputStream in;
    private DataOutputStream out;
    
    
    public Handler (State server_state, Socket clientSocket) throws IOException
    {
        // set the server state, get a taskMaker and get the task result queue
        this.server_state= server_state;
        this.task_maker= new TaskMaker();
        this.task_result= this.server_state.registerSubmitter(this.task_maker.submitter);
        user = null;
        in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
        out = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
    }


    // Método para tratar requisições de status. Retorna uma resposta com o estado atual do serviço.
    // Não aceita nada por parametro pois o StatusREQ (para já pelo menos) não tem qualquer conteúdo
    private void handleStatusRequest() 
    {
        long availableMemory = this.server_state.getAvailableMemory();
        int pendingTasks = this.server_state.taskQueue.size();
        try {
            new StatusREP(availableMemory, pendingTasks).serialize(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleExec (Request packet)
    {
        try
        {
            // send a task request
            this.server_state.taskQueue.put(task_maker.newTask(packet.arg, packet.mem));
            // get a task result
            this.task_result.take().serialize(out);
        }
        catch (Exception e)
        {
            System.out.println("Interrupted in handleExec return");
        }
    }

    private void handleRegisterRequest (RegistoRequest packet)
    {
        if (server_state.existsUser(packet.username))
        {
            try{
                new RegistoReply(false, "Username already exists.").serialize(out);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        server_state.addUser(packet.username, packet.password);
        user = packet.username;
        try {
            new RegistoReply(true, "User registered with success!\nWelcome " + user).serialize(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Registered user: Username - " + packet.username + "; Password - " + packet.password);
        System.out.println("User '" + user + "' logged in");
    }

    private void handleLoginRequest (LoginRequest packet)
    {
        if (!server_state.existsUser(packet.username))
        {
            try{
                new LoginReply(false, "Username does not exist.").serialize(out);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        if (!server_state.checkPassword(packet.username, packet.password))
        {
            try{
                new LoginReply(false, "Password Incorrect!").serialize(out);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        user = packet.username;
        try {
            new LoginReply(true, "Welcome " +  user).serialize(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("User '" + user + "' logged in");
    }

    private void handle (Protocol packet)
    {
        try 
        {
            switch (packet.type) 
            {
                case EXEC_RQ:
                    handleExec(Request.deserialize(in));
                    break;
                case STATUS_RQ:
                    handleStatusRequest();
                    break;
                case LG_IN_RQ:
                    handleLoginRequest(LoginRequest.deserialize(in));
                    break;
                case REG_RQ:
                    handleRegisterRequest(RegistoRequest.deserialize(in));
                    break;
                default:
                    break;
            }
        } 
        catch (IOException e) 
        {
            System.err.println("Error deserializing packet");
        }
    }


    public void run()
    {
        try 
        {
            while(true)
            {
                handle(Protocol.deserialize(in));
            }
        }
        catch (IOException e) 
        {
            System.err.println("fds");
        }
    }
}
