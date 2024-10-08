package Client;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import Protocol.Protocol;
import Protocol.Exec.GoodResponse;
import Protocol.Exec.Response;
import Protocol.Status.StatusREP;
import ThreadTools.ThreadControl;

public class HandleReceive implements Runnable{
    
    private DataInputStream in;
    private ThreadControl tc;
    private Lock l;
    private int outputNumber;
    private String writtingDir; 


    public HandleReceive (DataInputStream in, ThreadControl tc, String dir)
    {
        this.in = in;
        this.tc = tc;
        this.l = new ReentrantLock();
        this.outputNumber = 0;
        this.writtingDir = dir;
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

    private void handleExec (Response received)
    {
        //Implementar badresponse
        try {
            GoodResponse response = GoodResponse.deserialize(in);
            new Thread(() -> {
                l.lock();
                int thisFile = outputNumber++;
                l.unlock();
                String finalDir = writtingDir + "/output_" + thisFile;
                try(FileOutputStream fos = new FileOutputStream(finalDir)) {
                    fos.write(response.response);
                } catch (IOException e) {
                    System.err.println("Error in writting file");
                    e.printStackTrace();
                }
            }).start();
        } catch (IOException e) {
            System.err.println("Error in deserialization");
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
                System.err.println("Error in deserialization");
                e.printStackTrace();
            }
        }
    }
}
