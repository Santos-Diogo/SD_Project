package Server.WorkerServer;

import Protocol.Exec.Request;
import Protocol.Exec.Response;
import Server.Packet.Packet;
import Shared.LinkedBoundedBuffer;

class State
{
    LinkedBoundedBuffer<Packet> input_queue;
    LinkedBoundedBuffer<Packet> output_queue;
    long max_mem;

    State (long mem)
    {
        this.input_queue= new LinkedBoundedBuffer<>();
        this.output_queue= new LinkedBoundedBuffer<>();
        this.max_mem= mem;
    }
}
