package Client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import Protocol.Exec.Request;
import Protocol.Status.StatusREQ;
import Shared.LinkedBoundedBuffer;
import ThreadTools.ThreadControl;

public class HandleSend implements Runnable
{
 
    private Lock l;
    private LinkedBoundedBuffer<String> commands;
    private DataOutputStream out;
    private ThreadControl tc;


    public HandleSend (LinkedBoundedBuffer<String> commands, ThreadControl tc, DataOutputStream out)
    {
        this.l = new ReentrantLock();
        this.commands = commands;
        this.out = out;
        this.tc = tc;
    }

    private byte[] getContent(String file) throws IOException
    {
        Path file_path = Path.of(file);
        return Files.readAllBytes(file_path);
    }

    private void handleStatus ()
    {
        try {
            l.lock();
            new StatusREQ().serialize(out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            l.unlock();
        }
    }

    private void handleExec(String file, Integer mem)
    {
        Request request = null;
        try {
            byte[] job = getContent(file);
            request = new Request(job, mem);
            l.lock();
            request.serialize(out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            l.unlock();
        }
        System.out.println("\nJob #" + request.n_job + " submitted\n");
    }

    public void handle (String command)
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

    public void run () 
    {
        while(tc.getRunning())
        {   
            try {
                String command = commands.take();
                handle(command);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
