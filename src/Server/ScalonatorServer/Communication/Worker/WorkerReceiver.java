package Server.ScalonatorServer.Communication.Worker;

import java.io.DataInputStream;
import java.io.IOException;

import Protocol.Protocol;
import Protocol.Exec.BadResponse;
import Protocol.Exec.GoodResponse;
import Protocol.Exec.Response;
import Server.Packet.Packet;
import Server.ScalonatorServer.State;
import Shared.LinkedBoundedBuffer;
import ThreadTools.ThreadControl;

public class WorkerReceiver implements Runnable
{
    ThreadControl tc;
    DataInputStream input;
    State state;
    int my_num;

    WorkerReceiver (ThreadControl tc, DataInputStream input, State state, int my_num)
    {
        this.tc= tc;
        this.input= input;
        this.state= state;
        this.my_num= my_num;
    }

    public void run ()
    {
        while (this.tc.getRunning())
        {
            try
            {
                Protocol.deserialize(input);
                Response response = Response.deserialize(input);
                Protocol finalResponse = (response.success) ? GoodResponse.deserialize(input) : BadResponse.deserialize(input);
                Packet p= Packet.deserialize(input, finalResponse);
                LinkedBoundedBuffer<Protocol> output= this.state.getQueueClient(p.submitter);
                output.put(p.protocol);
                int mem= this.input.readInt();
                this.state.removeWorkerMem(p.submitter, -mem);
            }
            catch (InterruptedException e) {}
            catch (IOException e)
            {
                System.out.println("Worker Disconnected");
                this.state.removeWorker(this.my_num);
            }
        }
    }    
}
