package Client;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import Protocol.Protocol;
import Protocol.Exec.BadResponse;
import Protocol.Exec.GoodResponse;
import Protocol.Exec.Response;
import Protocol.Status.StatusREP;
import ThreadTools.ThreadControl;

public class HandleReceive implements Runnable{
    
    private DataInputStream in;
    private ThreadControl tc;
    private String writtingDir;
    private Client client;


    public HandleReceive (DataInputStream in, ThreadControl tc, String dir, Client client)
    {
        this.in = in;
        this.tc = tc;
        this.writtingDir = dir;
        this.client = client;
    }

    private void handleStatus (StatusREP received)
    {
        try {
            StatusREP packet = StatusREP.deserialize(in);
            System.out.println("Memory available: " + packet.available_memory);
            System.out.println("Number of Pending tasks: " + packet.number_of_tasks);
        } catch (IOException e) {
            System.err.println("Error in deserialization");
            e.printStackTrace();
        }
    }

    private void handleBadExec(BadResponse received)
    {
        System.out.println("Job #" + received.n_job + " failed to execute. Error code: " + received.error_code + "; Message: " + received.error_message);
    }

    private void handleGoodExec (GoodResponse received)
    {
            new Thread(() -> {
                String finalDir = writtingDir + "/output_" + received.n_job;
                try(FileOutputStream fos = new FileOutputStream(finalDir)) {
                    fos.write(received.response);
                } catch (IOException e) {
                    System.err.println("Error in writting file");
                    e.printStackTrace();
                }
            }).start();
    }

    private void handleExec (Response received)
    {
        try {
            if (!received.success)
                handleBadExec(BadResponse.deserialize(in));
            else
                handleGoodExec(GoodResponse.deserialize(in));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    private void handle (Protocol received)
    {
        try {
            switch (received.type)
            {
                case EXEC_RP:
                {
                    handleExec(Response.deserialize(in));
                    break;
                }
                case STATUS_RP:
                {
                    handleStatus(StatusREP.deserialize(in));
                }
                default : {}
            }
        } catch (IOException e) {
            System.err.println("Error in deserialization");
            e.printStackTrace();
        }
    }

    public void run()
    {
        while (tc.getRunning())
        {   
            try {
                Protocol received = Protocol.deserialize(in);
                handle(received);
            } catch (IOException e) { 
                client.quit();
                System.err.println("Lost connection to server, please press enter to quit");
                break; 
            }   
        }
    }
}
