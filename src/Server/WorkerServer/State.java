package Server.WorkerServer;

import Server.Shared.Packet.Packet;
import Shared.LinkedBoundedBuffer;

public class State
{
    LinkedBoundedBuffer<Packet> inputQueue;
    LinkedBoundedBuffer<Packet> outputQueue;

    public State ()
    {
        this.inputQueue= new LinkedBoundedBuffer<>();
        this.outputQueue= new LinkedBoundedBuffer<>();
    }
}
