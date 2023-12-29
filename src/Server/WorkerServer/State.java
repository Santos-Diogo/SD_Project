package Server.WorkerServer;

import Protocol.Exec.Response;
import Server.Packet.Task;
import Shared.LinkedBoundedBuffer;

public class State
{
    public LinkedBoundedBuffer<Task> inputQueue;
    public LinkedBoundedBuffer<Response> outputQueue;
    public long max_mem;

    public State (long mem)
    {
        this.inputQueue= new LinkedBoundedBuffer<>();
        this.outputQueue= new LinkedBoundedBuffer<>();
        this.max_mem= mem;
    }
}
