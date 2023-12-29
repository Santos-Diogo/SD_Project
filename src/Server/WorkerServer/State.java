package Server.WorkerServer;

import Server.Shared.Packet.Packet;
import Shared.LinkedBoundedBuffer;

public class State
{
    public LinkedBoundedBuffer<Packet> inputQueue;
    public LinkedBoundedBuffer<Packet> outputQueue;
    public long max_mem;

    public State (long mem)
    {
        this.inputQueue= new LinkedBoundedBuffer<>();
        this.outputQueue= new LinkedBoundedBuffer<>();
        this.max_mem= mem;
    }
}
